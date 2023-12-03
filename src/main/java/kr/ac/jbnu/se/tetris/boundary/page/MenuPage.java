package kr.ac.jbnu.se.tetris.boundary.page;

import kr.ac.jbnu.se.tetris.FrameMain;
import kr.ac.jbnu.se.tetris.boundary.BackPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_HEIGHT;
import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_WIDTH;

public class MenuPage extends JPanel {
    JButton game;
    JButton score;
    enum Menu{
        GAME_MENU("게임 시작"),SCORE_BOARD("랭킹");
        private final String label;
        Menu(String label) {
            this.label = label;
        }
        public String label() {
            return label;
        }
    }
    MenuPage(){
        setOpaque(false);
        setLayout(new GridLayout(FrameMain.DEFAULT_VERT_GRID_ROW,FrameMain.DEFAULT_VERT_GRID_COLUMN,
                FrameMain.GRID_WGAP,FrameMain.GRID_VGAP));
        setSize(new Dimension(WINDOW_WIDTH/4,WINDOW_HEIGHT/4));
        game = new JButton(Menu.GAME_MENU.label());
        game.addActionListener(e -> {
            try {
                BackPanel.getInstance().push(new GameMenuPage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        score = new JButton(Menu.SCORE_BOARD.label());
        score.addActionListener(e -> {
            try {
                BackPanel.getInstance().push(new ScoreboardPage());
            } catch (ExecutionException | InterruptedException | IOException ex) {
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        });
        add(game);
        add(score);
        this.setBorder(new EmptyBorder((FrameMain.WINDOW_HEIGHT-this.getHeight())/2,
                0,0,0));
    }
}
