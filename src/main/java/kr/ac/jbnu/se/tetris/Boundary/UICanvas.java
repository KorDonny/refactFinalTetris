package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.Handler.AIModeHandler;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        gifImagePath = "./src/main/java/kr/ac/jbnu/se/tetris/Resource/Image/uiGif.gif";
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
    public void setImage() throws IOException {
        gifImage = new ImageIcon(ImageIO.read(new File(gifImagePath)));
        Image img = gifImage.getImage();
        Image scaledImg = img.getScaledInstance(UICanvas.BOARD_SIZE_W, UICanvas.BOARD_SIZE_H, Image.SCALE_SMOOTH);
        gifImage = new ImageIcon(scaledImg);
    }
}
