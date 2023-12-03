package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.boundary.page.GameMenuPage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.entity.Block;
import kr.ac.jbnu.se.tetris.entity.WorkFlow;
import kr.ac.jbnu.se.tetris.entity.numeric.Tetrominoes;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class KeyControl implements KeyListener {
    static KeyControl keyControl;
    static TetrisCanvas player1;
    static TetrisCanvas player2;
    boolean isLeft;
    boolean isRight;
    boolean isUp;
    boolean isDown;
    boolean isOne;
    boolean isDrop;
    boolean isLeftP2;
    boolean isRightP2;
    boolean isUpP2;
    boolean isDownP2;
    boolean isOneP2;
    boolean isDropP2;
    private final TetrisTimerTask keyCtrlTask;
    private final WorkFlow keyCtrlWorks;
    public KeyControl(){
        isLeft=false;
        isRight=false;
        isUp=false;
        isDown=false;
        isOne=false;
        isDrop=false;

        isLeftP2=false;
        isRightP2=false;
        isUpP2=false;
        isDownP2=false;
        isOneP2=false;
        isDropP2=false;
        keyCtrlTask = new TetrisTimerTask() {
            @Override
            public void doLogic() throws ExecutionException, InterruptedException, IOException {
                if(getPlayer(false)==null)return;
                if(player2!=null)handlePlayerInput(player2, isDropP2, isLeftP2, isRightP2, isUpP2, isDownP2, isOneP2);
                handlePlayerInput(player1, isDrop, isLeft, isRight, isUp, isDown, isOne);
            }
        };
        keyCtrlWorks = new WorkFlow(keyCtrlTask);
        TimerManager.addTask(keyCtrlWorks,keyCtrlTask,100);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(player1==null)return;
        int key = e.getKeyCode();
        if (!isSingle()) {
            if(key == KeyEvent.VK_LEFT) isLeftP2 = false;
            if(key == KeyEvent.VK_RIGHT) isRightP2 = false;
            if(key == KeyEvent.VK_UP) isUpP2 = false;
            if(key == KeyEvent.VK_DOWN) isDownP2 = false;
            if(key == ']') isDropP2 = false;
            if(key == '[') isOneP2 = false;
        }
        if(key == KeyEvent.VK_A) isLeft = false;
        if(key == KeyEvent.VK_D) isRight = false;
        if(key == KeyEvent.VK_W) isUp = false;
        if(key == KeyEvent.VK_S) isDown = false;
        if(key == KeyEvent.VK_SPACE) isDrop = false;
        if(key == KeyEvent.VK_R) isOne = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //nothing to do
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(player1==null)return;
        int key = e.getKeyCode();
        if(key==KeyEvent.VK_ESCAPE){
            if(TimerManager.getTask(keyCtrlWorks).isPaused()) {TimerManager.resumeAllTasks(); return;}
            TimerManager.pauseAllTasks();
            while(true){
                switch (JOptionPane.showOptionDialog(null,null,"설정",JOptionPane
                                .YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                        new String[]{"재시작","종료","취소"},2)){
                    case 0:
                        try {
                            player1.restart();
                            if(player2!=null)player2.restart();
                            TimerManager.resumeAllTasks();
                        } catch (InterruptedException | ExecutionException | IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        return;
                    case 1:
                        try {
                            exitPrg();
                            return;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    case -1:
                    case 2:
                        TimerManager.resumeAllTasks();
                        return;
                    default:
                        break;
                }
            }
        }
        if(!isSingle() && player2.isStarted() && getCurPiece(player2).getShape() != Tetrominoes.NO_SHAPE && !player2.isPaused()){
            isLeftP2 = key == KeyEvent.VK_LEFT;
            isRightP2 = key == KeyEvent.VK_RIGHT;
            isUpP2 = key == KeyEvent.VK_UP;
            isDownP2 = key == KeyEvent.VK_DOWN;
            isDropP2 = key == ']' ;
            isOneP2 = key == '[';
        }
        if (player1.isStarted() && getCurPiece(player1).getShape() != Tetrominoes.NO_SHAPE && !player1.isPaused()){
            isLeft = key == KeyEvent.VK_A;
            isRight = key == KeyEvent.VK_D;
            isUp = key == KeyEvent.VK_W;
            isDown = key == KeyEvent.VK_S;
            isDrop = key == KeyEvent.VK_SPACE;
            isOne = key == KeyEvent.VK_R;
        }
    }
    private void handlePlayerInput(TetrisCanvas player, boolean isDrop, boolean isLeft, boolean isRight, boolean isUp, boolean isDown, boolean isOne) throws InterruptedException, ExecutionException, IOException {
        if (isDrop) {
            player.dropDown();
            return;
        }
        int newX = getX(player);
        int newY = getY(player);
        if(isOne) --newY;
        if(isUp && !isDown) getCurPiece(player).rotateLeft(player.getBoard());
        else if(isDown && !isUp) getCurPiece(player).rotateRight(player.getBoard());
        if(isLeft && !isRight) player.tryMove(getCurPiece(player), --newX, newY);
        else if(isRight && !isLeft) player.tryMove(getCurPiece(player), ++newX, newY);
        else player.tryMove(getCurPiece(player),newX, newY);
    }
    Block getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }
    public static void updatePlayer(TetrisCanvas player){
        if(player1==null) player1 = player;
        else player2 = player;
    }
    public static KeyControl getInstance(){
        if(keyControl==null){
            synchronized (KeyControl.class){
                keyControl = new KeyControl();
            }
        }
        return keyControl;
    }
    public static TetrisCanvas getPlayer(boolean isP2) {
        if(isP2)return player2;
        else return player1;
    }
    static synchronized void exitPrg() throws IOException {
        TimerManager.removeTask(keyControl.keyCtrlWorks);
        if(player2 != null){
            player2.exitCanvProg();
            player2 = null;
        }
        player1.exitCanvProg();
        player1 = null;
        keyControl = null;
        GameMenuPage.resetMenu();
    }
    public boolean isSingle(){ return player2 == null || player2 instanceof TetrisCanvasAI; }
}