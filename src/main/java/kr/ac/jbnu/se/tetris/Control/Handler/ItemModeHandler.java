package kr.ac.jbnu.se.tetris.Control.Handler;

//import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public class ItemModeHandler extends NormalModeHandler implements GameModeHandler {
    private final Timer itemTimer;
    public ItemModeHandler() throws IOException {
        super();
        // 아이템 타이머 생성 및 리스너 등록
        itemTimer = new Timer(10000, e -> removeRandomLine());
        // statusbar의 텍스트 변경을 모니터링하는 리스너 등록
//        TestMonitor.getScoreBar(false).addPropertyChangeListener("text", evt -> {
//            if ("game over".equals(evt.getNewValue())) {
//                itemTimer.stop(); // statusbar의 텍스트가 "game over"로 변경되면 아이템 타이머를 중지
//            }
//        });
    }
    @Override
    public void startGame() throws IOException, InterruptedException {
        super.startGame();
        itemTimer.start();
    }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public TetrisCanvas getCanvas() { return super.getCanvas(); }
    // 랜덤한 가로줄 또는 세로줄 제거
    private void removeRandomLine() {
        Random random = new Random();
        int item = random.nextInt(2); // 0 또는 1 (가로줄 또는 세로줄)

        if (item == 0) {
            removeRandomRow();
        } else {
            removeRandomColumn();
        }
    }
    // 랜덤한 가로줄 제거
    private void removeRandomRow() {
        int rowToRemove = (int) (Math.random() * TetrisCanvas.BoardHeight);
        for (int i = 0; i < TetrisCanvas.BoardWidth; i++) {
            getCanvas().board[rowToRemove * TetrisCanvas.BoardWidth + i] = Tetrominoes.NoShape;
        }
        getCanvas().repaint();
    }
    // 랜덤한 세로줄 제거
    private void removeRandomColumn() {
        int colToRemove = (int) (Math.random() * TetrisCanvas.BoardWidth);
        for (int i = 0; i < TetrisCanvas.BoardHeight; i++) {
            getCanvas().board[i * TetrisCanvas.BoardWidth + colToRemove] = Tetrominoes.NoShape;
        }
        getCanvas().repaint();
    }
}