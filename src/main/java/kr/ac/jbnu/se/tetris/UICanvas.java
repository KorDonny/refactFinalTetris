package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.TimerTask;

import static kr.ac.jbnu.se.tetris.CanvasScoreInterface.player1Score;
import static kr.ac.jbnu.se.tetris.CanvasScoreInterface.player2Score;

public class UICanvas extends JPanel{
    public static final int BOARD_SIZE_W = 350;
    public static final int BOARD_SIZE_H = 700;
    private final CanvasBuff uiBuff;
    private PreviewBlock mainPreview = null;
    private PreviewBlock subPreview = null;
    private final WorkFlow uiCanvasWorks = new WorkFlow(this);
    public UICanvas() throws IOException {
        setOpaque(false);
        setPreferredSize(new Dimension(BOARD_SIZE_W,BOARD_SIZE_H));
        if(!(this instanceof TetrisCanvas)){
            TimerManager.addTask(uiCanvasWorks, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },100);
            initScoreDisplay();
        }
        uiBuff = new CanvasBuff();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (mainPreview != null && mainPreview.checkReady()){
            mainPreview.drawPreview(g);
        }
        if (subPreview != null && subPreview.checkReady()){
            subPreview.drawPreview(g);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(uiBuff.getSprite(), 0, 0, null);
    }
    public PreviewBlock getPreview(int previewNum) {
        if (mainPreview == null) {
            mainPreview = new PreviewBlock(previewNum, false);
            return mainPreview;
        } else if (subPreview == null) {
            subPreview = new PreviewBlock(previewNum, true);
            return subPreview;
        }
        return null;
    }
    private void initScoreDisplay(){
        CanvasScoreInterface.setScoreAttribute();
        if(GameMenuPage.getMode() == GameMode.MULTI) add(player2Score);
        add(player1Score);
    }
}
