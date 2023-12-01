package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface GameModeHandler {
    void startGame() throws IOException, InterruptedException, ExecutionException;
    void connectCanvas();
    TetrisCanvas getCanvas();
    void initiateTrigger();
}