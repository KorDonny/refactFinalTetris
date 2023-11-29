package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.io.IOException;
import java.util.TimerTask;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    NormalModeHandler normal;
    public LocalModeHandler() throws IOException {
        super();
        this.normal = new NormalModeHandler();
        this.canvas = new TetrisCanvas();
    }
    @Override
    public void startGame() throws IOException, InterruptedException {
        normal.startGame();
        FrameMain.getBackPanel().push(canvas);
        connectCanvas();
        canvas.start();
        initiateTrigger();
    }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public void initiateTrigger(){
        BackPanel.addTask(this.canvas, new TimerTask() {
            @Override
            public void run() {
                try {
                    getCanvas().actionTrigger();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 400);
    }
}
