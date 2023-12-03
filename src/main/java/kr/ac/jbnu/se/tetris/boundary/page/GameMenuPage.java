package kr.ac.jbnu.se.tetris.boundary.page;

import kr.ac.jbnu.se.tetris.*;
import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.UICanvas;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.control.TimerManager;
import kr.ac.jbnu.se.tetris.control.handler.*;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.entity.numeric.GameMode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_HEIGHT;
import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_WIDTH;

public class GameMenuPage extends JPanel {
    static GameMode curMode;
    GameMenuPage(){
        setOpaque(false);
        setLayout(new GridLayout(FrameMain.DEFAULT_VERT_GRID_ROW,FrameMain.DEFAULT_VERT_GRID_COLUMN,
                FrameMain.GRID_WGAP,FrameMain.GRID_VGAP));
        setSize(new Dimension(WINDOW_WIDTH/2,WINDOW_HEIGHT/4));
        for(GameMode mode : GameMode.values()){
            JButton toInsert = new JButton(mode.label());
            toInsert.addActionListener(e-> {
                try {
                    startGame(mode);
                } catch (IOException | InterruptedException | ExecutionException ex) {
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            });
            add(toInsert);
        }
        this.setBorder(new EmptyBorder((FrameMain.WINDOW_HEIGHT-this.getHeight())/2,
                0,0,0));
    }
    private void startGame(GameMode mode) throws IOException, InterruptedException, ExecutionException {
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
                // Local 대전, 세컨 계정 연동
                while(true){
                    if(checkSecondAccount()) break;
                    return;
                }
                setModeHandler(new LocalModeHandler());
                break;
        }
        InGamePage.getInstance();
        setMode(mode);
        getModeHandler().startGame();
    }
    private static class GameModeHolder {
        private static final InnerContextResource CONTEXT_PROV = new InnerContextResource();
    }
    private static final class InnerContextResource {
        private GameModeHandler context;
        private void setContext(GameModeHandler context) {this.context = context; }
    }
    public static GameModeHandler getModeHandler() { return GameModeHolder.CONTEXT_PROV.context; }
    public static void setModeHandler(GameModeHandler ac) { GameModeHolder.CONTEXT_PROV.setContext(ac); }
    private static void setMode(GameMode mode){ curMode = mode; }
    public static GameMode getMode(){ return curMode; }
    private boolean checkSecondAccount(){
        JPanel optionCustom = new JPanel();
        optionCustom.setLayout(new GridLayout(1, 2));
        optionCustom.setOpaque(false);
        JTextField[] infoField = new JTextField[]{
                new HintTextField("ID"),
                new HintPasswordField("PW")
        };
        for (JTextField field : infoField){
            field.setVisible(true);
            field.setSize(150, 20);
            optionCustom.add(field);
            optionCustom.add(Box.createHorizontalStrut(10));
        }
        optionCustom.setVisible(true);
        int result = JOptionPane.showConfirmDialog(this, optionCustom,
                "Please Enter EMAIL and PW Values", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            return FirebaseTool.getInstance().logIn(new Account(infoField[0].getText(),
                    ((JPasswordField)infoField[1]).getPassword())) != null;
        }
        return false;
    }
    public static synchronized void resetMenu() throws IOException {
        setMode(null);
        setModeHandler(null);
        UICanvas.exitProg();
        BackPanel.getInstance().pop();
        TimerManager.exitProg();
    }
}
