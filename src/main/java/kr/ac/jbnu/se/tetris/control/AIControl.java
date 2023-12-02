package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.entity.Entity;
import kr.ac.jbnu.se.tetris.entity.Point;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import java.util.Arrays;
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
        double bigWeight = Integer.MIN_VALUE;
        int bigWeightRotate = -1;

        for (int j = 0; j < curPiece.getNumOfRotate(); j++) {
            Object[] ret = move(curPiece);

            if (bigWeight < (double) ret[0]) {
                bigWeight = (double) ret[0];
                bigWeightRotate = j;
                goodPosition.setX(((Entity) ret[1]).getCurX());
                goodPosition.setY(((Entity) ret[1]).getCurY());
            }

            // 블럭 회전
            curPiece.rotateRight(canvas.getBoard());
        }

        // 가장 최적의 위치 좌표와 화전 횟수
        int[] returnData = new int[3];
        returnData[0] = goodPosition.getX();
        returnData[1] = goodPosition.getY();
        returnData[2] = bigWeightRotate;
        return returnData;
    }

    private Object[] move(Entity curPiece) {
        double bigWeight = Integer.MIN_VALUE;
        Entity block;
        Entity bigWeightBlock;
        Entity tempBlock;
        boolean endWeight;

        block = new Entity(Tetrominoes.NO_SHAPE);
        block.copyEntity(curPiece);
        moveLeft(block);
        bigWeightBlock = new Entity(Tetrominoes.NO_SHAPE); //ret[1] 구문 이니셜라이징이 필요하므로 넣어둠.
        tempBlock = new Entity(Tetrominoes.NO_SHAPE);
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

    private void deleteBlock(Entity shape) {
        int curX = shape.getCurX();
        int curY = shape.getCurY();
        for (int i = 0; i < shape.getShapeArr().length; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = Tetrominoes.NO_SHAPE;
        }
    }

    private void placeShape(Entity shape) {
        int curX = shape.getCurX();
        int curY = shape.getCurY();
        for (int i = 0; i < 4; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * TETRIS_CANVAS_W + x;
            canvas.getBoard()[idx] = shape.getShape();
        }
    }

    public boolean moveDown(Entity tempBlock) {
        int newY = tempBlock.getCurY();
        while (newY > 0) {
            if (!canvas.tryMove(tempBlock, tempBlock.getCurX(), newY - 1))
                break;
            --newY;
        }
        return true;
    }

    private boolean moveRight(Entity shape) {
        return !canvas.tryMove(shape, shape.getCurX() + 1, shape.getCurY());
    }

    private void moveLeft(Entity shape) {
        int newX = shape.getCurX();
        while (newX > 0) {
            if (!canvas.tryMove(shape, newX - 1, shape.getCurY()))
                break;
            --newX;
        }
    }

    private void setWeight(double[] weight) {
        this.weight = Arrays.copyOf(weight,weight.length);
    }

    public void setDefaultWeight() {
        //계산된 가중치값
        weight[0] = -0.8882324104022858;
        weight[1] = 0.3221180915759138;
        weight[2] = -0.2322970072064213;
        weight[3] = 0.2309138814220062;
    }
}