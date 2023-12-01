package kr.ac.jbnu.se.tetris.control.handler;

//import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_W;

public class ItemModeHandler extends NormalModeHandler implements GameModeHandler {
    private final Timer itemTimer;
    Random random;
    public ItemModeHandler() throws IOException {
        super();
        // 아이템 타이머 생성 및 리스너 등록
        itemTimer = new Timer(10000, e -> removeRandomLine());
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        super.startGame();
        random = new Random();
        itemTimer.start();
    }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public TetrisCanvas getCanvas() { return super.getCanvas(); }
    // 랜덤한 가로줄 또는 세로줄 제거
    private void removeRandomLine() {
        int item = random.nextInt(2); // 0 또는 1 (가로줄 또는 세로줄)

        if (item == 0) {
            removeRandomRow();
        } else {
            removeRandomColumn();
        }
    }
    // 랜덤한 가로줄 제거
    private void removeRandomRow() {
        int rowToRemove = (int) (Math.random() * TETRIS_CANVAS_H);
        for (int i = 0; i < TETRIS_CANVAS_W; i++) {
            getCanvas().getBoard()[rowToRemove * TETRIS_CANVAS_W + i] = Tetrominoes.NO_SHAPE;
        }
        getCanvas().repaint();
    }
    // 랜덤한 세로줄 제거
    private void removeRandomColumn() {
        int colToRemove = (int) (Math.random() * TETRIS_CANVAS_W);
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            getCanvas().getBoard()[i * TETRIS_CANVAS_W + colToRemove] = Tetrominoes.NO_SHAPE;
        }
        getCanvas().repaint();
    }
}