package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Entity.GameMode;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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
        Map<String,Integer>[] resultArr = new LinkedHashMap[GameMode.values().length];
        Set<String>[] nameTag = new Set[GameMode.values().length];
        for(GameMode mode : GameMode.values()){
            resultArr[mode.ordinal()] = FirebaseTool.getModeBestScoreChart(mode);
            nameTag[mode.ordinal()] = resultArr[mode.ordinal()].keySet();
            JLabel colmTag = new JLabel(mode.label());
            colmTag.setOpaque(true);
            colmTag.setBackground(Color.cyan);
            colmTag.setForeground(Color.MAGENTA.darker());
            colmTag.setFont(new Font("SansSerif",Font.BOLD,FONT_DEFAULT));
            add(colmTag);
        }
        int idx = 0;
        for (int i = 0; i < INDEX_TOP10*(GameMode.values().length+1); i++){
            JLabel colmTag;
            if(i%7==0){
                colmTag = new JLabel(String.valueOf(i/7+1));
            }
            else{
                colmTag = new JLabel(
                        nameTag[i%7-1].toArray()[idx%INDEX_TOP10]+" - "+resultArr[i%7-1].get(nameTag[i%7-1].toArray()[idx++%INDEX_TOP10]).toString()
                );
            }
            colmTag.setHorizontalAlignment(JLabel.CENTER);
            colmTag.setOpaque(true);
            colmTag.setBackground(Color.cyan);
            colmTag.setForeground(Color.MAGENTA);
            colmTag.setFont(new Font("SansSerif",Font.BOLD,FONT_DEFAULT));
            add(colmTag);
        }
    }
}