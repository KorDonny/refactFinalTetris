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
        //해당 주석구문은 추후 고려해볼 계획으로, 리팩토링시 넘어가셔도 됩니다.
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
                     Thread.currentThread().interrupt();
                     throw new RuntimeException(e);
                 }
             }
         },0,400);
    }
}
