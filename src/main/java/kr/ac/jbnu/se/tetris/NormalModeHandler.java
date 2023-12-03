package kr.ac.jbnu.se.tetris;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class NormalModeHandler implements GameModeHandler {
    private final TetrisCanvas canvas;
    private final UICanvas uiCanvas;
    public NormalModeHandler() throws IOException {
        this.canvas = new TetrisCanvas();
        uiCanvas = new UICanvas();
        connectCanvas();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        InGamePage.getInstance().add(canvas);
        InGamePage.getInstance().add(uiCanvas);
        canvas.setUICanvas(uiCanvas);
        canvas.start();
    }
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    public TetrisCanvas getCanvas() { return this.canvas; }
    public UICanvas getUiCanvas() {
        return uiCanvas;
    }
}
