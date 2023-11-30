package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.Handler.*;
import kr.ac.jbnu.se.tetris.Entity.GameMode;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class GameMenuPage extends JPanel {
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
        InGamePage.getInstance();
        switch (mode) {
            case NORMAL:
                setModeHandler(new NormalModeHandler());
                break;
            case ITEM:
                setModeHandler(new ItemModeHandler());
                break;
            case SURVIVAL:
                setModeHandler(new SurvivalModeHandler());
                break;
            case SPRINT:
                setModeHandler(new SprintModeHandler());
                break;
            case AI:
                setModeHandler(new AIModeHandler());
                break;
            case MULTI:
                // Local 대전
                setModeHandler(new LocalModeHandler());
                break;
        }
        getModeHandler().startGame();
    }
    private static class GameModeHolder {
        private static final InnerContextResource CONTEXT_PROV = new InnerContextResource();
        private GameModeHolder() { super(); }
    }
    private static final class InnerContextResource {
        private GameModeHandler context;
        private InnerContextResource() { super(); }
        private void setContext(GameModeHandler context) {this.context = context; }
    }
    public static GameModeHandler getModeHandler() { return GameModeHolder.CONTEXT_PROV.context; }
    public void setModeHandler(GameModeHandler ac) { GameModeHolder.CONTEXT_PROV.setContext(ac); }
}
