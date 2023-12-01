package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.entity.Entity;
import kr.ac.jbnu.se.tetris.entity.Point;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import java.util.Random;

import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_W;

public class AIControl {
    Random random;
    Calculator calculator;
    TetrisCanvasAI canvas;

    private double[] weight = new double[4];

    public AIControl(TetrisCanvasAI canvas) {
        this.canvas = canvas;
        random = new Random();
        calculator = new Calculator(canvas);

        setDefaultWeight();
    }

    public AIControl(TetrisCanvasAI canvas, double[] weight) {
        this.canvas = canvas;
        random = new Random();
        calculator = new Calculator(canvas);

        setWeight(weight);
    }

    public int[] findGoodPosition(Entity curPiece) {
        Point goodPosition = new Point(0, 0);
        double big_weight = Integer.MIN_VALUE;
        int big_weight_rotate = -1;

        for (int j = 0; j < curPiece.getNumOfRotate(); j++) {
            Object[] ret = move(curPiece);

            if (big_weight < (double) ret[0]) {
                big_weight = (double) ret[0];
                big_weight_rotate = j;
                goodPosition.x = ((Entity) ret[1]).getCurX();
                goodPosition.y = ((Entity) ret[1]).getCurY();
            }

            // 블럭 회전
            curPiece.rotateRight();
        }

        // 가장 최적의 위치 좌표와 화전 횟수
        int[] returnData = new int[3];
        returnData[0] = goodPosition.x;
        returnData[1] = goodPosition.y;
        returnData[2] = big_weight_rotate;
        return returnData;
    }

    private Object[] move(Entity curPiece) {
        double big_weight = Integer.MIN_VALUE;
        Entity block, big_weight_block, temp_block;
        boolean end_right = false;

        block = new Entity(Tetrominoes.NoShape);
        block.copyEntity(curPiece);
        move_left(block);
        big_weight_block = new Entity(Tetrominoes.NoShape); //ret[1] 구문 이니셜라이징이 필요하므로 넣어둠.
        temp_block = new Entity(Tetrominoes.NoShape);
        while (true) {
            temp_block.copyEntity(block);
            if (move_down(temp_block)) {
                // 블럭을 아래로 다 내렸을 경우, 현재 가중치 값 계산
                placeShape(temp_block);
                double fitness = calculator.blockFitness(weight);
                if (big_weight < fitness) {
                    big_weight = fitness;
                    big_weight_block.copyEntity(temp_block);
                }
                deleteBlock(temp_block);
            }
            // 그 후, 우측으로 이동
            end_right = move_right(block);
            if (end_right) {
                break;
            }
        }
        Object[] ret = new Object[2];
        ret[0] = big_weight;
        ret[1] = big_weight_block;
        return ret;
    }

    private void deleteBlock(Entity shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        //for(int i = 0; i < shape.coords.length; i++) { origin code
        for (int i = 0; i < shape.getShapeArr().length; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = Tetrominoes.NoShape;
        }
    }

    private void placeShape(Entity shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        for (int i = 0; i < 4; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = shape.getShape();
        }
    }

    public boolean move_down(Entity temp_block) {
        int newY = temp_block.getCurY();
        while (newY > 0) {
            if (!canvas.tryMove(temp_block, temp_block.getCurX(), newY - 1))
                break;
            --newY;
        }
        return true;
    }

    private boolean move_right(Entity shape) {
        return !canvas.tryMove(shape, shape.position[0] + 1, shape.position[1]);
    }

    private void move_left(Entity shape) {
        int newX = shape.position[0];
        while (newX > 0) {
            if (!canvas.tryMove(shape, newX - 1, shape.position[1]))
                break;
            --newX;
        }
    }

    private void setWeight(double[] weight) {
        for(int i = 0; i < 4; i++){
            this.weight[i] = weight[i];
        }
    }

    public void setDefaultWeight() {
        //계산된 가중치값
        weight[0] = -0.8882324104022858;
        weight[1] = 0.3221180915759138;
        weight[2] = -0.2322970072064213;
        weight[3] = 0.2309138814220062;
    }
}