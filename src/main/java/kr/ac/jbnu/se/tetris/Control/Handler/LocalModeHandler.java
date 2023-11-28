package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.io.IOException;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    NormalModeHandler normal;
    public LocalModeHandler() throws IOException {
        super();
        this.normal = new NormalModeHandler();
        this.canvas = new TetrisCanvas();
        KeyControl.updatePlayer(this.canvas);
    }
    @Override
    public void startGame() throws IOException {
        normal.startGame();
        FrameMain.getInstance().getBackPanel().push(canvas);
        connectCanvas();
        canvas.start();
    }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
