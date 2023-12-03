package kr.ac.jbnu.se.tetris;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface GameModeHandler {
    void startGame() throws IOException, InterruptedException, ExecutionException;
    void connectCanvas();
    TetrisCanvas getCanvas();
}