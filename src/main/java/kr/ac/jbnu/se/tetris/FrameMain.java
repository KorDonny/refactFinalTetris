package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.LogInPage;
import kr.ac.jbnu.se.tetris.boundary.RegisterPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class FrameMain extends JFrame {
    static FrameMain frameMain;
    JLayeredPane contentPane;
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int FONT_TITLE  = 20;
    public static final int FONT_DEFAULT = 10;
    public static final int DEFAULT_VERT_GRID_ROW = 15;
    public static final int DEFAULT_VERT_GRID_COLUMN = 1;
    public static final int GRID_VGAP = 10;
    public static final int GRID_WGAP = 0;
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
        backPanel.push(new UIPanel());
    }
    /** 기초 진입화면 */
    static class UIPanel extends JPanel{
        UIPanel(){
            setOpaque(false);
            setLayout(new GridLayout(DEFAULT_VERT_GRID_ROW,DEFAULT_VERT_GRID_COLUMN,GRID_WGAP,GRID_VGAP));

            int uiTopMargin = (WINDOW_HEIGHT)/3;
            setBorder(new EmptyBorder(uiTopMargin,0,0,0));

            JLabel welcome = new JLabel("환영합니다!");
            JButton btnLogIn = new JButton("로그인");
            JButton btnRegister = new JButton("회원가입");

            welcome.setForeground(Color.WHITE);
            welcome.setFont(new Font("SansSerif",Font.BOLD,FONT_TITLE));
            welcome.setHorizontalAlignment(SwingConstants.CENTER);
            welcome.setOpaque(false);

            btnLogIn.addActionListener(e -> {
                try {
                    getBackPanel().push(new LogInPage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            btnRegister.addActionListener(e -> {
                try {
                    getBackPanel().push(new RegisterPage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

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
    public static void main(String[] args) throws IOException{
        FrameMain frame = new FrameMain();
        frame.setVisible(true);
        frame.initiateUI();
        frame.requestFocusInWindow();
    }
}