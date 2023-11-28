package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Boundary.UICanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.io.IOException;
import java.util.TimerTask;

public class NormalModeHandler implements GameModeHandler {
    private final TetrisCanvas canvas;
    public static final String KEY_CONTROL_LABEL = "KeyControl";
    public NormalModeHandler() {
        this.canvas = new TetrisCanvas();
    }

    @Override
    public void startGame() throws IOException {
        FrameMain.getBackPanel().push(new UICanvas());
        connectCanvas();
        FrameMain.getBackPanel().push(canvas);
        canvas.start();
    }
    @Override
    public void connectCanvas() { KeyControl.updatePlayer(getCanvas()); }
    @Override
    public TetrisCanvas getCanvas() { return this.canvas; }
    @Override
    public void initiateTrigger(){
        BackPanel.addTask("Canvas drop logic", new TimerTask() {
            @Override
            public void run() {
                canvas.actionTrigger();
            }
        }, 400);
    }
}
