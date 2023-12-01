package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.InGamePage;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Boundary.UICanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class NormalModeHandler implements GameModeHandler {
    private final TetrisCanvas canvas;
    public NormalModeHandler() throws IOException {
        this.canvas = new TetrisCanvas();
    }

    @Override
    public void startGame() throws IOException, InterruptedException, ExecutionException {
        connectCanvas();
        InGamePage.getInstance().add(canvas);
        InGamePage.getInstance().add(new UICanvas());
        canvas.start();
        initiateTrigger();
    }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
    @Override
    public void initiateTrigger(){
//        BackPanel.addTask(this.canvas, new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    canvas.actionTrigger();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, 400);
         BackPanel.getTimer().scheduleAtFixedRate(new TimerTask() {
             @Override
             public void run() {
                 try {
                     canvas.actionTrigger();
                 } catch (InterruptedException | ExecutionException e) {
                     throw new RuntimeException(e);
                 }
             }
         },0,400);
    }
}
