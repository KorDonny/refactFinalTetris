package kr.ac.jbnu.se.tetris.control.handler;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.InGamePage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.boundary.UICanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;

import java.io.IOException;
import java.util.TimerTask;
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
        initiateTrigger();
    }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
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

    public UICanvas getUiCanvas() {
        return uiCanvas;
    }
}
