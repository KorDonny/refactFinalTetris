package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.Handler.*;
import kr.ac.jbnu.se.tetris.Entity.GameMode;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class GameMenuPage extends JPanel {
    GameModeHandler modeHandler;
    GameMenuPage(){
        setOpaque(false);
        setLayout(new GridLayout(FrameMain.DEFAULT_VERT_GRID_ROW,FrameMain.DEFAULT_VERT_GRID_COLUMN,
                FrameMain.GRID_WGAP,FrameMain.GRID_VGAP));
        for(GameMode mode : GameMode.values()){
            JButton toInsert = new JButton(mode.label());
            toInsert.addActionListener(e-> {
                try {
                    startGame(mode);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(toInsert);
        }
        this.setBorder(new EmptyBorder((FrameMain.WINDOW_HEIGHT-this.getHeight())/2,
                0,0,0));
    }
    private void startGame(GameMode mode) throws IOException, InterruptedException {
        this.setVisible(false);
        switch (mode) {
            case NORMAL:
                modeHandler = new NormalModeHandler();
                modeHandler.startGame();
                break;
            case ITEM:
                modeHandler = new ItemModeHandler();
                modeHandler.startGame();
                break;
            case SURVIVAL:
                modeHandler = new SurvivalModeHandler();
                modeHandler.startGame();
                break;
            case SPRINT:
                modeHandler = new SprintModeHandler();
                modeHandler.startGame();
                break;
            case AI:
                modeHandler = new AIModeHandler();
                modeHandler.startGame();
                break;
            case MULTI:
                // Local 대전
                modeHandler = new LocalModeHandler();
                modeHandler.startGame();
                break;
        }
    }
}
