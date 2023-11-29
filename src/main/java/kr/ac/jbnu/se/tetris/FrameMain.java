package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.LogInPage;
import kr.ac.jbnu.se.tetris.Boundary.RegisterPage;
import kr.ac.jbnu.se.tetris.Control.KeyControl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class FrameMain extends JFrame {
    static FrameMain frameMain;
    JLayeredPane contentPane;
    public final static int WINDOW_WIDTH = 1200, WINDOW_HEIGHT = 800, FONT_TITLE  = 20, FONT_DEFAULT = 10,
    DEFAULT_VERT_GRID_ROW = 15,DEFAULT_VERT_GRID_COLUMN = 1,GRID_VGAP = 10, GRID_WGAP = 0;
    static BackPanel backPanel;
    public FrameMain() throws IOException {
        frameMain = this;
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backPanel = new BackPanel();

        contentPane = new JLayeredPane();
        contentPane.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(backPanel);
        add(contentPane);
        setResizable(false);

        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
    }
    /** 우회 접근을 해야 컴포넌트가 안가려짐. 생성자에서 연결시 버튼이나 라벨들이 가리는 문제점 존재 */
    public void initiateUI(){
        backPanel.push(new UIPanel(this));
    }
    /** 기초 진입화면 */
    class UIPanel extends JPanel{
        UIPanel(FrameMain main){
            setOpaque(false);
            setLayout(new GridLayout(DEFAULT_VERT_GRID_ROW,DEFAULT_VERT_GRID_COLUMN,GRID_WGAP,GRID_VGAP));

            int uiTopMargin = (WINDOW_HEIGHT)/3;
            setBorder(new EmptyBorder(uiTopMargin,0,0,0));

            JLabel welcome = new JLabel("환영합니다!");
            JButton btnLogIn = new JButton("로그인");
            JButton btnRegister = new JButton("회원가입");

            welcome.setForeground(Color.WHITE);
            welcome.setFont(new Font("SansSerif",Font.BOLD,FONT_TITLE));
            welcome.setOpaque(false);

            btnLogIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { backPanel.push(new LogInPage()); }
            });
            btnRegister.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { backPanel.push(new RegisterPage()); }
            });

            add(welcome);
            add(btnLogIn);
            add(btnRegister);
        }
    }
    public static FrameMain getInstance() throws IOException {
        if(frameMain==null){
            synchronized (KeyControl.class){
                frameMain = new FrameMain();
            }
        }
        return frameMain;
    }
    public static BackPanel getBackPanel(){ return backPanel; }
    public static void main(String[] args) throws IOException {
        FrameMain frame = new FrameMain();
        frame.setVisible(true);
        frame.initiateUI();
        frame.requestFocusInWindow();
        frame.addKeyListener(new KeyControl());
    }
}