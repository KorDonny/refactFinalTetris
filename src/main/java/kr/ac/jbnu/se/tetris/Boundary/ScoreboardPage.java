package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Boundary.GameMenuPage;
import kr.ac.jbnu.se.tetris.Entity.GameMode;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import java.awt.*;

public class ScoreboardPage extends JPanel {
    FlowLayout mainLayout;
    final int BUTTON_GAP = 50;
    final int INDEX_TOP10 = 10;
    JPanel[] rankList;
    ScoreboardPage(){
        mainLayout = new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, FrameMain.WINDOW_HEIGHT/2);
        setLayout(mainLayout);

        /** 추후 DB연결을 해야 함. */
        rankList = new JPanel[GameMode.values().length];
        int idx = 0;
        for(GameMode menu : GameMode.values()){
            String title = menu.label();
            rankList[idx] = new JPanel(new GridLayout(INDEX_TOP10+1,1));
            JLabel label = new JLabel(title);
            label.setFont(new Font("SansSerif",Font.BOLD,20));
            label.setForeground(Color.MAGENTA);
            label.setBackground(Color.DARK_GRAY);
            rankList[idx].add(label);
            rankList[idx].setFont(new Font("SansSerif",Font.BOLD,20-idx));
            rankList[idx].setForeground(Color.WHITE);
            rankList[idx].setBackground(Color.BLACK);
            add(rankList[idx++]).setVisible(true);
        }
    }
}