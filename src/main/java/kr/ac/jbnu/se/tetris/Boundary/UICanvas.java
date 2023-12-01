package kr.ac.jbnu.se.tetris.Boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class UICanvas extends JPanel implements CanvasInterface{
    static final int BOARD_SIZE_W = 350;
    static final int BOARD_SIZE_H = 700;
    ImageIcon gifImage;
    String gifImagePath;
    public UICanvas() throws IOException {
        setOpaque(false);
        setPreferredSize(new Dimension(BOARD_SIZE_W,BOARD_SIZE_H));
        BackPanel.addTask(this, new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },20);
        setImage();
    }
    @Override
    public void paintComponent(Graphics g){
        gifImage.paintIcon(this, g, 0, 0);
    }
    @Override
    public void setImage() throws IOException {
        gifImagePath = "./src/main/java/kr/ac/jbnu/se/tetris/Resource/Image/uiGif.gif";
        gifImage = new ImageIcon(ImageIO.read(new File(gifImagePath)));
        scaleImage();
    }
    void scaleImage() {
        Image img = gifImage.getImage();
        Image scaledImg = img.getScaledInstance(UICanvas.BOARD_SIZE_W, UICanvas.BOARD_SIZE_H, Image.SCALE_SMOOTH);
        gifImage = new ImageIcon(scaledImg);
    }
}
