package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.page.InGamePage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    public LocalModeHandler() throws IOException {
        super();
        this.canvas = new TetrisCanvas();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        super.startGame();
        InGamePage.getInstance().add(canvas);
        canvas.setUICanvas(super.getUiCanvas());
        connectCanvas();
        canvas.start();
    }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
}
