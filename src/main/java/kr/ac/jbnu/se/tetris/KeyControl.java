package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
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
    private final WorkFlow keyCtrlWorks = new WorkFlow(this);
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
        TimerManager.addTask(keyCtrlWorks, new TimerTask() {
            @Override
            public void run() {
                try {
                    if(getPlayer(false)==null)return;
                    if(player2!=null)handlePlayerInput(player2, isDropP2, isLeftP2, isRightP2, isUpP2, isDownP2, isOneP2);
                    handlePlayerInput(player1, isDrop, isLeft, isRight, isUp, isDown, isOne);
                } catch (InterruptedException | ExecutionException e) {
                    /* Clean up whatever needs to be handled before interrupting  */
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        },100);
    }
    public boolean isSingle(){ return player2 == null; }
    @Override
    public void keyReleased(KeyEvent e) {
        if(getPlayer(false)==null)return;
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
        if(getPlayer(false)==null)return;
        int key = e.getKeyCode();
        if(key=='p'||key=='P'){
            if(player2!=null)player2.pause();
            player1.pause();
            return;
        }
        if(key==KeyEvent.VK_ESCAPE&&player1!=null){
            if(player2!=null)player2.pause();
            player1.pause();
            return;
        }
        if(!isSingle()&&player2.isStarted() && getCurPiece(player2).getShape() != Tetrominoes.NO_SHAPE && !player2.isPaused()){
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
    private void handlePlayerInput(TetrisCanvas player, boolean isDrop, boolean isLeft, boolean isRight, boolean isUp, boolean isDown, boolean isOne) throws InterruptedException, ExecutionException {
        synchronized (getCurPiece(player)){
            if (isDrop) {
                player.dropDown();
                return;
            }
            int newX = getX(player);
            int newY = getY(player);
            if(isOne) --newY;
            if(isUp && !isDown) getCurPiece(player).rotateLeft(player.getBoard()); // 원래 코드 getCurPiece(player).rotateLeft();
            else if(isDown && !isUp) getCurPiece(player).rotateRight(player.getBoard()); // 원래 코드 getCurPiece(player).rotateRight();
            if(isLeft && !isRight) player.tryMove(getCurPiece(player), --newX, newY);
            else if(isRight && !isLeft) player.tryMove(getCurPiece(player), ++newX, newY);
            else player.tryMove(getCurPiece(player),newX, newY);
        }
    }
    Block getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }
    public static void updatePlayer(TetrisCanvas player){
        if(player1==null)player1=player;
        else if(GameMenuPage.getMode() == GameMode.MULTI)player2=player;
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
}