package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;
import kr.ac.jbnu.se.tetris.Tetris;

import java.io.IOException;

public class AIModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    private NormalModeHandler AI;
    public AIModeHandler() throws IOException {
        super();
        AI = new NormalModeHandler();
        this.canvas = new TetrisCanvasAI();
    }
    @Override
    public void startGame() throws IOException {
        AI.startGame();
        this.connectCanvas();
        FrameMain.getInstance().getBackPanel().push(canvas);
        canvas.start();
        AI.getCanvas().requestFocusInWindow();
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(this.canvas);
    }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
}
