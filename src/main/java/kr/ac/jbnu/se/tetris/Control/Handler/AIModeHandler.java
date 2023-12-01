package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.InGamePage;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AIModeHandler extends NormalModeHandler implements GameModeHandler{
    private static TetrisCanvas canvas;
    private NormalModeHandler AI;
    public AIModeHandler() throws IOException {
        super();
        AI = new NormalModeHandler();
        this.canvas = new TetrisCanvasAI();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        AI.startGame();
        this.connectCanvas();
        InGamePage.getInstance().add(canvas);
        canvas.start();
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(this.canvas);
    }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
