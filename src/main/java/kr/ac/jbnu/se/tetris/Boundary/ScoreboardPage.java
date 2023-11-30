package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Entity.GameMode;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.FrameMain.FONT_DEFAULT;

public class ScoreboardPage extends JPanel {
    GridLayout scoreLayout;
    final int BUTTON_GAP = 50;
    final int INDEX_TOP10 = 10;
    ScoreboardPage() throws ExecutionException, InterruptedException {
        scoreLayout = new GridLayout(0,GameMode.values().length+1,BUTTON_GAP/2,BUTTON_GAP/2);
        setLayout(scoreLayout);

        JButton backBtn = new JButton("<");
        backBtn.setVisible(true);
        add(backBtn);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameMain.getBackPanel().pop();
            }
        });
        for(GameMode menu : GameMode.values()){
            JLabel colmTag = new JLabel(menu.label());
            colmTag.setOpaque(true);
            colmTag.setBackground(Color.cyan);
            colmTag.setForeground(Color.GREEN);
            colmTag.setFont(new Font("SansSerif",Font.BOLD,FONT_DEFAULT));
            add(colmTag);
        }
        for (int i = 0; i < INDEX_TOP10*(GameMode.values().length+1); i++){
            JLabel colmTag;
            if(i%7==0){
                colmTag = new JLabel(String.valueOf(i/7+1));
            }
            else{
                FirebaseTool.getModeBestScoreChart(GameMode.values()[i%7]).keySet();
                colmTag = new JLabel();
            }
            colmTag.setOpaque(true);
            colmTag.setBackground(Color.cyan);
            colmTag.setForeground(Color.MAGENTA);
            colmTag.setFont(new Font("SansSerif",Font.BOLD,FONT_DEFAULT));
            add(colmTag);
        }
    }
}