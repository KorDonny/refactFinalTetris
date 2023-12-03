package kr.ac.jbnu.se.tetris.entity;

import kr.ac.jbnu.se.tetris.entity.numeric.Tetrominoes;

import java.awt.*;

import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_W;

public class PreviewBlock {

    private final int previewNum;
    private static final int RECT_WIDTH = 20; // 각 블록의 넓이
    private static final int RECT_HEIGHT = 20; // 각 블록의 높이
    private Block[] previewList;
    private int correctionValue = 5;
    private boolean readyFlag = false;

    public PreviewBlock(int previewNum, boolean isSub) {
        this.previewNum = previewNum;
        previewList = new Block[previewNum];
        if (isSub) {
            this.correctionValue = BOARD_SIZE_W - (4 * RECT_WIDTH) - 5;
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
        Color tmpcolor = shape.getColor();
        g.setColor(tmpcolor);
        g.fillRect(x + 1, y + 1, RECT_WIDTH - 2, RECT_HEIGHT - 2);
        g.setColor(tmpcolor.brighter());
        int x2 = x + RECT_WIDTH - 1;
        int y2 = y + RECT_HEIGHT - 1;
        g.drawLine(x, y2, x, y);
        g.drawLine(x, y, x2, y);
        g.setColor(tmpcolor.darker());
        g.drawLine(x + 1, y2, x2, y2);
        g.drawLine(x2, y2, x2, y + 1);
    }
    public void drawPreview(Graphics g) {
        for (int num = 0; num < previewNum; num++){
            for (int i = 0; i < 4; ++i) {
                int x = 1 + previewList[num].x(i);
                int y = 2 - previewList[num].y(i);
                drawSquare(g,
                        x * RECT_WIDTH + correctionValue,
                        y * RECT_HEIGHT + (4 * num * RECT_HEIGHT) + num * 5,
                        previewList[num].getShape());
            }
        }
    }
    public void updatePreviewList(Block[] previewList) {
        this.previewList = previewList;
    }
    public void setReadyFlagTrue() {
        readyFlag = true;
    }
    public void setReadyFlagFalse() {
        readyFlag = false;
    }
    public boolean checkReady() {
        return readyFlag;
    }
}
