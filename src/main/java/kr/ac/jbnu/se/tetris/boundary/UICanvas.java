package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.entity.CanvasBuff;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class UICanvas extends JPanel{
    public static final int BOARD_SIZE_W = 350;
    public static final int BOARD_SIZE_H = 700;
    private CanvasBuff uiBuff;
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
    public void paintComponent(Graphics g){
        g.drawImage(uiBuff.getSprite(), 0, 0, null);
    }
}
