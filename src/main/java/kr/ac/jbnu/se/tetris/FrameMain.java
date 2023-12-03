package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.page.EnterPage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
public class FrameMain extends JFrame {
    public static final String RESOURCE_PATH = "./src/main/java/kr/ac/jbnu/se/tetris/resource/";
    public static final String IMAGE_DIR_PATH = RESOURCE_PATH+"image/";
    public static final String MUSIC_DIR_PATH = RESOURCE_PATH+"music/";
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int FONT_TITLE  = 20;
    public static final int FONT_DEFAULT = 10;
    public static final int DEFAULT_VERT_GRID_ROW = 15;
    public static final int DEFAULT_VERT_GRID_COLUMN = 1;
    public static final int GRID_VGAP = 10;
    public static final int GRID_WGAP = 0;
    JLayeredPane contentPane;
    public FrameMain() throws IOException {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JLayeredPane();
        contentPane.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(BackPanel.getInstance());
        add(contentPane);
        setResizable(false);

        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        BackPanel.getInstance().push(new EnterPage());
    }
    public static void main(String[] args) throws IOException {
        FrameMain frame = new FrameMain();
        frame.setVisible(true);
    }
}