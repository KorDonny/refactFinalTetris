package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface GameModeHandler {
    void startGame() throws IOException, InterruptedException, ExecutionException;
    void connectCanvas();
    TetrisCanvas getCanvas();
    void initiateTrigger();
}