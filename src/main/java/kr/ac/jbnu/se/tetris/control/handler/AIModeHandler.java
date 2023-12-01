package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.InGamePage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.control.KeyControl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AIModeHandler extends NormalModeHandler implements GameModeHandler{
    private TetrisCanvas canvas;
    private NormalModeHandler ai;
    public AIModeHandler() throws IOException {
        super();
        this.ai = new NormalModeHandler();
        this.canvas = new TetrisCanvasAI();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        ai.startGame();
        this.connectCanvas();
        InGamePage.getInstance().add(canvas);
        canvas.start();
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(canvas);
    }
    @Override
    public TetrisCanvas getCanvas() { return canvas; }
}
