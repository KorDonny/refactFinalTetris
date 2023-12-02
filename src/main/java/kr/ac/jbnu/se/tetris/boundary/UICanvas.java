package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.entity.CanvasBuff;
import kr.ac.jbnu.se.tetris.entity.Entity;
import kr.ac.jbnu.se.tetris.entity.GameMode;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;

public class UICanvas extends JPanel{
    public static final int BOARD_SIZE_W = 350;
    public static final int BOARD_SIZE_H = 700;
    private CanvasBuff uiBuff;
    private Preview mainPreview = null;
    private Preview subPreview = null;
    private JLabel player1Score;
    private JLabel player2Score;
    public UICanvas() {
        setOpaque(false);
        setPreferredSize(new Dimension(BOARD_SIZE_W,BOARD_SIZE_H));
        BackPanel.addTask(this, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },20);
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

    public Preview getPreview(int previewNum) {
        if (mainPreview == null) {
            mainPreview = new Preview(previewNum, false);
            return mainPreview;
        } else if (subPreview == null) {
            subPreview = new Preview(previewNum, true);
            return subPreview;
        }
        return null;
    }
    private void initScoreDisplay(){
        player1Score = new JLabel("0");
        player1Score.setOpaque(false);
        //player1Score.setFont(new Font());
        player1Score.setForeground(Color.RED);
        add("Player1 Score Board", player1Score);
        if(GameMenuPage.getMode() == GameMode.AI||GameMenuPage.getMode() == GameMode.MULTI){
            player2Score = new JLabel("0");
            player2Score.setOpaque(false);
            player2Score.setForeground(Color.BLUE);
            add("Player2 Score Board",player2Score);
        }
    }

    class Preview {

        private final int previewNum;
        private Entity[] previewList;
        private final int squareWidth = 20; // 각 블록의 넓이
        private final int squareHeight = 20; // 각 블록의 높이
        private int correctionValue = 5;
        private boolean ReadyFlag = false;
        public Preview(int previewNum, boolean isSub) {
            this.previewNum = previewNum;
            previewList = new Entity[previewNum];
            if (isSub) {
                this.correctionValue = BOARD_SIZE_W - (4 * squareWidth) - 5;
            }
        }
        private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
            Color tmpcolor = shape.getColor();
            g.setColor(tmpcolor);
            g.fillRect(x + 1, y + 1, squareWidth - 2, squareHeight - 2);
            g.setColor(tmpcolor.brighter());
            int x2 = x + squareWidth - 1;
            int y2 = y + squareHeight - 1;
            g.drawLine(x, y2, x, y);
            g.drawLine(x, y, x2, y);
            g.setColor(tmpcolor.darker());
            g.drawLine(x + 1, y2, x2, y2);
            g.drawLine(x2, y2, x2, y + 1);
        }
        public void drawPreview(Graphics g) {
            for (int num = 0; num < previewNum; num++) {
                for (int i = 0; i < 4; ++i) {
                    int x = 1 + previewList[num].x(i);
                    int y = 2 - previewList[num].y(i);
                    drawSquare(g,
                            x * squareWidth + correctionValue,
                            y * squareHeight + (4 * num * squareHeight) + num * 5,
                            previewList[num].getShape());
                }
            }
        }
        public void updatePreviewList(Entity[] previewList) { this.previewList = previewList; }
        public void setReadyFlagTrue() { ReadyFlag = true; }
        public void setReadyFlagFalse() { ReadyFlag = false; }
        public boolean checkReady() { return ReadyFlag; }
    }
}
