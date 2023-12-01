package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.boundary.TetrisCanvas.TETRIS_CANVAS_W;

public class SurvivalModeHandler extends NormalModeHandler implements GameModeHandler {
    private final Timer lineAdditionTimer;
    private final Random random;
    public SurvivalModeHandler() throws IOException {
        super();
        this.random = new Random();
        this.lineAdditionTimer = new Timer(2000, e -> doLogic()); // 2초마다 한 줄 추가
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        super.startGame();
        lineAdditionTimer.start();
    }
    @Override
    public TetrisCanvas getCanvas() {
        return super.getCanvas();
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(getCanvas());
    }
    public void doLogic() {
        if (!getCanvas().isStarted()) return;
        int holePosition = random.nextInt(TETRIS_CANVAS_W); // 뚫린 위치
        for (int x = 0; x < TETRIS_CANVAS_W; x++) {
            for (int y = TETRIS_CANVAS_H - 1; y > 0; y--) {
                Tetrominoes shape = getCanvas().shapeAt(x, y - 1);
                getCanvas().getBoard()[y * TETRIS_CANVAS_W + x] = shape;
            }
        }
        // 랜덤한 위치에 한 칸 뚫린 라인 생성
        for (int x = 0; x < TETRIS_CANVAS_W; x++) {
            if (x != holePosition) {
                getCanvas().getBoard()[x] = Tetrominoes.GRAY_LINE_SHAPE;
            } else {
                getCanvas().getBoard()[x] = Tetrominoes.NO_SHAPE;
            }
        }
    }
}