package kr.ac.jbnu.se.tetris;

import kr.ac.jbnu.se.tetris.BackPanel;
import kr.ac.jbnu.se.tetris.LogInPage;
import kr.ac.jbnu.se.tetris.RegisterPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

import static kr.ac.jbnu.se.tetris.FrameMain.*;

public class EnterPage extends JPanel{
    public EnterPage(){
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
                BackPanel.getInstance().push(new LogInPage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnRegister.addActionListener(e -> {
            try {
                BackPanel.getInstance().push(new RegisterPage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(welcome);
        add(btnLogIn);
        add(btnRegister);
    }
}
