package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class MenuPage extends JPanel {
    JButton game, score;
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

        game = new JButton(Menu.GAME_MENU.label());
        game.addActionListener(e -> FrameMain.getBackPanel().push(new GameMenuPage()));
        score = new JButton(Menu.SCORE_BOARD.label());
        score.addActionListener(e -> {
            try {
                FrameMain.getBackPanel().push(new ScoreboardPage());
            } catch (ExecutionException | InterruptedException ex) {
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
