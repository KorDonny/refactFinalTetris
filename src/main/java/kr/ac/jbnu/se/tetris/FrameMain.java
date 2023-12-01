package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.LogInPage;
import kr.ac.jbnu.se.tetris.boundary.RegisterPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FrameMain extends JFrame {
    public final static int WINDOW_WIDTH = 1200;
    public final static int WINDOW_HEIGHT = 800;
    public final static int FONT_TITLE  = 20;
    public final static int FONT_DEFAULT = 10;
    public final static int DEFAULT_VERT_GRID_ROW = 15;
    public final static int DEFAULT_VERT_GRID_COLUMN = 1;
    public final static int GRID_VGAP = 10;
    public final static int GRID_WGAP = 0;
    JLayeredPane contentPane;
    static FrameMain frameMain;
    static BackPanel backPanel;
    static {
        try {
            backPanel = new BackPanel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public FrameMain() throws IOException {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            welcome.setHorizontalAlignment(JLabel.CENTER);
            welcome.setOpaque(false);

            btnLogIn.addActionListener(e -> backPanel.push(new LogInPage()));
            btnRegister.addActionListener(e -> backPanel.push(new RegisterPage()));

            add(welcome);
            add(btnLogIn);
            add(btnRegister);
        }
    }
    public static FrameMain getInstance() throws IOException {
        if(frameMain==null){
            synchronized (FrameMain.class){
                frameMain = new FrameMain();
            }
        }
        return frameMain;
    }
    public static BackPanel getBackPanel(){ return backPanel; }
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FrameMain frame = new FrameMain();
        frame.setVisible(true);
        frame.initiateUI();
        frame.requestFocusInWindow();
    }
}