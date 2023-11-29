package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;

import java.io.IOException;
/** 상속받는 클래스 생성자에 꼭 modeType = this;를 명세해줘야함. */
public interface GameModeHandler {
    void startGame() throws IOException, InterruptedException;
    void connectCanvas();
    TetrisCanvas getCanvas();
    void initiateTrigger();
}