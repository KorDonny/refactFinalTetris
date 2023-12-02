package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.entity.Entity;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;

import java.awt.*;

public class Preview {

    private final int previewNum;
    private Entity[] previewList;
    private final int squareWidth = 20; // 각 블록의 넓이
    private final int squareHeight = 20; // 각 블록의 높이
    private final int BOARD_SIZE_W;
    private final int BOARD_SIZE_H;
    private int correctionValue = 5;
    private boolean ReadyFlag = false;

    public Preview(int previewNum, boolean isSub, int BOARD_SIZE_W, int BOARD_SIZE_H) {
        this.previewNum = previewNum;
        previewList = new Entity[previewNum];
        this.BOARD_SIZE_W = BOARD_SIZE_W;
        this.BOARD_SIZE_H = BOARD_SIZE_H;
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
        for (int num = 0; num < previewNum; num++){
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

    public void updatePreviewList(Entity[] previewList) {
        this.previewList = previewList;
    }

    public void setReadyFlagTrue() {
        ReadyFlag = true;
    }

    public void setReadyFlagFalse() {
        ReadyFlag = false;
    }

    public boolean checkReady() {
        return ReadyFlag;
    }
}
