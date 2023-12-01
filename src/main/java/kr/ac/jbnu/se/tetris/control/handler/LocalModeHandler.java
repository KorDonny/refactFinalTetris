package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.InGamePage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class LocalModeHandler extends NormalModeHandler implements GameModeHandler{
    private final TetrisCanvas canvas;
    NormalModeHandler normal;
    public LocalModeHandler() throws IOException {
        super();
        this.normal = new NormalModeHandler();
        this.canvas = new TetrisCanvas();
    }
    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        normal.startGame();
        InGamePage.getInstance().add(canvas);
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
        BackPanel.getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    canvas.actionTrigger();
                } catch (InterruptedException | ExecutionException e) {
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        },0,400);
    }
}
