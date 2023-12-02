package kr.ac.jbnu.se.tetris.entity;

import java.util.Arrays;
import java.util.Random;

import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_W;

public class Entity {
    /**
     * Boundary클래스의 curX=0, curY=1가 Entity에게 승계
     */
    private int[] position = new int[2];
    /**
     * 블럭 형상 정보 식별자
     */
    private Tetrominoes shape;
    /**
     * 블럭 형상 정보 배열 n칸 x=0 ,y=1 좌표
     */
    protected int[][] coords;
    private Random random = new Random();
    public Entity(Tetrominoes shape) {
        initFunc(shape);
    }
    public void copyEntity(Entity entity) {
        this.shape = entity.getShape();
        setPosition(entity.getCurX(), entity.getCurY());
        setShapeArr(entity.getShapeArr());
    }
    /**
     * Tetrominoes에 고정 정보들에 대한 접근 및 복사
     */
    private void initFunc(Tetrominoes shape) {
        this.shape = shape;
        this.coords = shape.getShapeArr();
        setPosition(TETRIS_CANVAS_W / 2 + 1, TETRIS_CANVAS_H - 1 - maxY());
    }
    public void updateCurX(int x) {
        this.position[0] = x;
    }
    public void updateCurY(int y) {
        this.position[1] = y;
    }
    public int getCurX() {
        return this.position[0];
    }
    public int getCurY() {
        return this.position[1];
    }
    /**
     * 형상 배열 index행 x값=0 인덱싱후 리턴
     */
    public int x(int index) {
        return coords[index][0];
    }
    /**
     * 형상 배열 index행 y값=1 인덱싱후 리턴
     */
    public int y(int index) {
        return coords[index][1];
    }
    /**
     * 블럭 형상 식별자 반환 Tetrominoes 타입
     */
    public Tetrominoes getShape() {
        return shape;
    }
    /**
     * 블럭 형상 배열 반환
     */
    public int[][] getShapeArr() {
        return coords;
    }
    public void setShapeArr(int[][] coords) {
        this.coords = Arrays.copyOf(coords,coords.length);
    }
    /**
     * 블록의 가장 왼쪽 칸의 x좌표를 반환
     */
    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }
    /**
     * 최저높이 Y값 리턴
     */
    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }
    /**
     * 최고높이 Y값 리턴
     */
    public int maxY() { // 블록의 가장 위쪽 칸의 y좌표를 반환 - 구현 동기가 맞는지 확인 요청
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.max(m, coords[i][1]);
        }
        return m;
    }
    /**
     * 복사된 entity를 newPiece에 전달, tryMove가 처리. 좌회전
     */
    public void rotateLeft(Tetrominoes[] board) {
        if (getShape() == Tetrominoes.SQUARE_SHAPE) // 블록이 사각형인 경우 종료
            return;
        int[][] result = new int[4][2];
        for (int i = 0; i < 4; ++i) {
            result[i][0] = coords[i][1];
            result[i][1] = -coords[i][0];
            int x = result[i][0]+position[0];
            int y = result[i][1]-position[1];
            if(x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H){
                return;
            }
            if(board[(y * TETRIS_CANVAS_W) + x] != Tetrominoes.NO_SHAPE){
                return;
            }
        }
        setShapeArr(result);
    }
    /**
     * 복사된 entity를 newPiece에 전달, tryMove가 처리. 우회전
     */
    public void rotateRight(Tetrominoes[] board) {
        if (getShape() == Tetrominoes.SQUARE_SHAPE) // 블록이 사각형인 경우 종료
            return;
        int[][] result = new int[4][2];
        for (int i = 0; i < 4; ++i) {
            result[i][0] = -coords[i][1];
            result[i][1] = coords[i][0];
            int x = result[i][0]+position[0];
            int y = result[i][1]-position[1];
            if(x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H){
                return;
            }
            if(board[(y * TETRIS_CANVAS_W) + x] != Tetrominoes.NO_SHAPE){
                return;
            }
        }
        setShapeArr(result);
    }
    /**
     * 블럭 초기화시점에 랜덤화
     * 현재 사용 블럭은 1~7까지의 enum들만 사용.
     * 그외의 것들은 기능적 블럭
     */
    public void setRandomShape() {
        int x = random.nextInt(6) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values();
        initFunc(values[x]);
    }
    //추가된 코드
    public int getNumOfRotate() {
        switch (getShape()) {
            case T_SHAPE:
            case L_SHAPE:
            case MIRRORED_L_SHAPE:
                return 4;
            case Z_SHAPE:
            case S_SHAPE:
            case LINE_SHAPE:
                return 2;
            case SQUARE_SHAPE:
                return 1;
            case NO_SHAPE:
            default:
                return 0;
        }
    }
    public void setPosition(int x, int y) {
        updateCurX(x);
        updateCurY(y);
    }
}
