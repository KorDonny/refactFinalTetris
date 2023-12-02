package kr.ac.jbnu.se.tetris.boundary;

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
    private String gifImagePath = "./src/main/java/kr/ac/jbnu/se/tetris/resource/image/uiGif.gif";
    private Preview preview = null;
    private boolean preview1Flag = false;

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
    public void paint(Graphics g) {
        super.paint(g);

        if (preview1Flag){
            preview.drawPreview(g);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        gifImage.paintIcon(this, g, 0, 0);
    }
    @Override
    public void setImage() throws IOException {
        gifImage = new ImageIcon(ImageIO.read(new File(gifImagePath)));
        scaleImage();
    }
    void scaleImage() {
        Image img = gifImage.getImage();
        Image scaledImg = img.getScaledInstance(UICanvas.BOARD_SIZE_W, UICanvas.BOARD_SIZE_H, Image.SCALE_SMOOTH);
        gifImage = new ImageIcon(scaledImg);
    }

    public Preview getPreview(int previewNum) {
        if (preview == null) preview = new Preview(previewNum);
        return preview;
    }

    public void setPreview1FlagTrue(){
        preview1Flag = true;
    }
    public void setPreview1FlagFalse(){
        preview1Flag = false;
    }
}
