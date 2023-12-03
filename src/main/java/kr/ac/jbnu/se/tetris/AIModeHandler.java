package kr.ac.jbnu.se.tetris;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AIModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    public AIModeHandler() throws IOException {
        super();
        canvas = new TetrisCanvasAI();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        super.startGame();
        connectCanvas();
        InGamePage.getInstance().add(canvas);
        canvas.start();
    }
}
