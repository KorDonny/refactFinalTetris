package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.TetrisCanvas.TETRIS_CANVAS_W;

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
    // 랜덤한 가로줄 또는 세로줄 제거
    private void removeRandomLine() {
        if (!this.getCanvas().isStarted()) {
            this.itemTimer.stop();
        } else {
            int item = this.random.nextInt(2);
            if (item == 0) {
                this.removeRandomRow();
            } else {
                this.removeRandomColumn();
            }
        }
    }
    // 랜덤한 가로줄 제거 기존 int형 캐스팅시엔 소숫점 무조건 폐기
    private void removeRandomRow() {
        int rowToRemove = random.nextInt(TETRIS_CANVAS_H-1);
        for (int i = 0; i < TETRIS_CANVAS_W; i++) {
            getCanvas().getBoard()[rowToRemove * TETRIS_CANVAS_W + i] = Tetrominoes.NO_SHAPE;
        }
        getCanvas().repaint();
    }
    // 랜덤한 세로줄 제거
    private void removeRandomColumn() {
        int colToRemove = random.nextInt(TETRIS_CANVAS_W-1);
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            getCanvas().getBoard()[i * TETRIS_CANVAS_W + colToRemove] = Tetrominoes.NO_SHAPE;
        }
        getCanvas().repaint();
    }
}