package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.entity.Entity;
import kr.ac.jbnu.se.tetris.entity.Tetrominoes;
import org.checkerframework.checker.units.qual.C;

import javax.swing.*;
import java.awt.*;

public class Preview extends JPanel{

    private final int previewNum;
    private Entity[] previewList;

    private final int PREVIEW_CANVAS_W = 4;
    private final int PREVIEW_CANVAS_H;

    static final int PREVIEW_SIZE_W = 120; // 지금:120->30, 원래:350->1개의 넓이 35
    static final int PREVIEW_SIZE_H = 600; // 지금:600->30, 원래:700->1개의 높이 31.818181...

    private final int squareWidth;
    private final int squareHeight;

    public Preview(int previewNum) {
        this.previewNum = previewNum;
        previewList = new Entity[previewNum];
        PREVIEW_CANVAS_H = 4 * previewNum;
        squareWidth = PREVIEW_SIZE_W / PREVIEW_CANVAS_W;
        squareHeight = PREVIEW_SIZE_H / PREVIEW_CANVAS_H;
        setPreferredSize(new Dimension(PREVIEW_SIZE_W, PREVIEW_SIZE_H));
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int num = 0; num < previewNum; num++){
            for (int i = 0; i < 4; ++i) {
                int x = 1 + previewList[num].x(i);
                int y = (PREVIEW_CANVAS_H - 4 - num * 4) + 1 - previewList[num].y(i);
                drawSquare(g, x * squareWidth, (PREVIEW_CANVAS_H - y - 1) * squareHeight,
                        previewList[num].getShape());
            }
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

    public void drawPreview(Entity[] previewList) {
        this.previewList = previewList;
        repaint();
    }
}
