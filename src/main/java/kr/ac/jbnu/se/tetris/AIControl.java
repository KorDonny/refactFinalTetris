package kr.ac.jbnu.se.tetris;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static kr.ac.jbnu.se.tetris.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.TetrisCanvas.TETRIS_CANVAS_W;

public class AIControl {
    Calculator calculator;
    TetrisCanvasAI canvas;

    private double[] weight = new double[4];

    public AIControl(TetrisCanvasAI canvas) {
        this.canvas = canvas;
        calculator = new Calculator();
        setDefaultWeight();
    }

    public int[] findGoodPosition(Block curPiece) {
        Point goodPosition = new Point(0, 0);
        double bigWeight = Integer.MIN_VALUE;
        int bigWeightRotate = -1;

        for (int j = 0; j < curPiece.getNumOfRotate(); j++) {
            Object[] ret = move(curPiece);

            if (bigWeight < (double) ret[0]) {
                bigWeight = (double) ret[0];
                bigWeightRotate = j;
                goodPosition.setX(((Block) ret[1]).getCurX());
                goodPosition.setY(((Block) ret[1]).getCurY());
            }

            // 블럭 회전
            curPiece.rotateRight();
        }

        // 가장 최적의 위치 좌표와 화전 횟수
        int[] returnData = new int[3];
        returnData[0] = goodPosition.getX();
        returnData[1] = goodPosition.getY();
        returnData[2] = bigWeightRotate;
        return returnData;
    }

    private Object[] move(Block curPiece) {
        double bigWeight = Integer.MIN_VALUE;
        Block block;
        Block bigWeightBlock;
        Block tempBlock;
        boolean endWeight;

        block = new Block(Tetrominoes.NO_SHAPE);
        block.copyEntity(curPiece);
        moveLeft(block);
        bigWeightBlock = new Block(Tetrominoes.NO_SHAPE); //ret[1] 구문 이니셜라이징이 필요하므로 넣어둠.
        tempBlock = new Block(Tetrominoes.NO_SHAPE);
        while (true) {
            tempBlock.copyEntity(block);
            if (moveDown(tempBlock)) {
                // 블럭을 아래로 다 내렸을 경우, 현재 가중치 값 계산
                placeShape(tempBlock);
                double fitness = calculator.blockFitness(weight);
                if (bigWeight < fitness) {
                    bigWeight = fitness;
                    bigWeightBlock.copyEntity(tempBlock);
                }
                deleteBlock(tempBlock);
            }
            // 그 후, 우측으로 이동
            endWeight = moveRight(block);
            if (endWeight) {
                break;
            }
        }
        Object[] ret = new Object[2];
        ret[0] = bigWeight;
        ret[1] = bigWeightBlock;
        return ret;
    }

    private void deleteBlock(Block shape) {
        int curX = shape.getCurX();
        int curY = shape.getCurY();
        for (int i = 0; i < shape.getShapeArr().length; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = Tetrominoes.NO_SHAPE;
        }
    }

    private void placeShape(Block shape) {
        int curX = shape.getCurX();
        int curY = shape.getCurY();
        for (int i = 0; i < 4; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = shape.getShape();
        }
    }

    public boolean moveDown(Block tempBlock) {
        int newY = tempBlock.getCurY();
        while (newY > 0) {
            if (!canvas.tryMove(tempBlock, tempBlock.getCurX(), newY - 1))
                break;
            --newY;
        }
        return true;
    }

    private boolean moveRight(Block shape) {
        return !canvas.tryMove(shape, shape.getCurX() + 1, shape.getCurY());
    }

    private void moveLeft(Block shape) {
        int newX = shape.getCurX();
        while (newX > 0) {
            if (!canvas.tryMove(shape, newX - 1, shape.getCurY()))
                break;
            --newX;
        }
    }

    public void setDefaultWeight() {
        //계산된 가중치값
        weight[0] = -0.8882324104022858;
        weight[1] = 0.3221180915759138;
        weight[2] = -0.2322970072064213;
        weight[3] = 0.2309138814220062;
    }
    class Calculator{
        public Double blockFitness(double[] weight) {
            double result = 0.0;

            int hc = holeCount();
            int cl = completeLine();

            int[] height = new int[TETRIS_CANVAS_H];
            int ah = aggregateHeight(height);
            int b = bumpiness(height);

            result += ah * weight[0];
            result += hc * weight[1];
            result += b * weight[2];
            result += cl * weight[3];

            return result;
        }

        // 완성된 줄을 찾는 메소드
        private int completeLine() {
            int ret = 0;
            for (int i = 0; i < TETRIS_CANVAS_H; i++) {
                int j;
                for (j = 0; j < TETRIS_CANVAS_W; j++) {
                    if (canvas.getBoard()[j * TETRIS_CANVAS_W + i] == Tetrominoes.NO_SHAPE)
                        break;
                }

                if (j == TETRIS_CANVAS_W)
                    ret++;
            }

            return ret;
        }

        private int bumpiness(int[] height) {
            int ret = 0;
            for (int i = 1; i < TETRIS_CANVAS_W; i++) {
                ret += Math.abs(height[i - 1] - height[i]);
            }
            return ret;
        }

        private int aggregateHeight(int[] height) {
            for (int i = 0; i < TETRIS_CANVAS_W; i++) {
                int high = TETRIS_CANVAS_H - 1;
                while (high >= 0) {
                    if (canvas.getBoard()[high * TETRIS_CANVAS_W + i] != Tetrominoes.NO_SHAPE) {
                        break;
                    }
                    high--;
                }
                height[i] = high + 1;
            }

            int ret = 0;
            for (int i = 0; i < TETRIS_CANVAS_H; i++) {
                ret += height[i];
            }
            return ret;
        }

        // 테트리스 블럭들 사이에 존재하는 구멍을 구하는 메소드
        private int holeCount() {
            boolean[][] visited = new boolean[TETRIS_CANVAS_H][TETRIS_CANVAS_W];

            // 테트리스 보드판에 0이 아닌 지점을 찾는다.
            for (int i = 0; i < TETRIS_CANVAS_H; i++) {
                for (int j = 0; j < TETRIS_CANVAS_W; j++) {
                    if (canvas.getBoard()[i * TETRIS_CANVAS_W + j] != Tetrominoes.NO_SHAPE)
                        visited[i][j] = true;
                }
            }

            // (0,4)를 기준으로 해서 bfs를 진행한다.
            bfs(visited);

            int ret = 0;

            // bfs를 진행하고 boolean 값이 false인 값은 구멍이다.
            for (int i = 0; i < TETRIS_CANVAS_H; i++) {
                for (int j = 0; j < TETRIS_CANVAS_W; j++) {
                    if (!visited[i][j])
                        ret++;
                }
            }

            return ret;
        }

        private void bfs(boolean[][] visited) {
            int[] ud = { -1, 0, 1, 0 };
            int[] rl = { 0, 1, 0, -1 };

            Queue<Point> q = new LinkedList<>();
            q.add(new Point(0, 4));
            visited[0][4] = true;

            while (!q.isEmpty()) {
                Point cur = q.poll();

                for (int i = 0; i < 4; i++) {
                    int nx = cur.getX() + ud[i];
                    int ny = cur.getY() + rl[i];

                    if (nx < 0 || nx >= TETRIS_CANVAS_H || ny < 0 || ny >= TETRIS_CANVAS_W || visited[nx][ny])
                        continue;

                    visited[nx][ny] = true;
                    q.add(new Point(nx, ny));
                }
            }
        }
    }
}