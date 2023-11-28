package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;

import java.io.IOException;

public interface GameModeHandler {
    void startGame() throws IOException;
    void connectCanvas();
    public TetrisCanvas getCanvas();
}