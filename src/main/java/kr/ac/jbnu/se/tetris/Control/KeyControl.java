package kr.ac.jbnu.se.tetris.Control;

import kr.ac.jbnu.se.tetris.Boundary.BackPanel;
import kr.ac.jbnu.se.tetris.Boundary.InGamePage;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

public class KeyControl implements KeyListener {
    static KeyControl keyControl = null;
    boolean isLeft,isRight,isUp,isDown,isOne,isDrop;
    boolean isLeftP2,isRightP2,isUpP2,isDownP2,isOneP2,isDropP2;
    static TetrisCanvas player1;
    static TetrisCanvas player2;
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
        BackPanel.addTask("KeyControl", new TimerTask() {
            @Override
            public void run() {
                try {
                    if(getPlayer(false)==null)return;
                    if(player2!=null)handlePlayerInput(player2, isDropP2, isLeftP2, isRightP2, isUpP2, isDownP2, isOneP2);
                    handlePlayerInput(player1, isDrop, isLeft, isRight, isUp, isDown, isOne);
                } catch (InterruptedException e) {
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(getPlayer(false)==null)return;
        int key = e.getKeyCode();
        if(key=='p'||key=='P'){
            try {
                if(player2!=null)player2.pause();
                player1.pause();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        if(key==KeyEvent.VK_ESCAPE){
            if(player1!=null) {
                try {
                    if(player2!=null)player2.pause();
                    player1.pause();
                    return;
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if(!isSingle()){
            if (player2.isStarted() && getCurPiece(player2).getShape() != Tetrominoes.NoShape && !player2.isPaused()){
                isLeftP2 = key == KeyEvent.VK_LEFT;
                isRightP2 = key == KeyEvent.VK_RIGHT;
                isUpP2 = key == KeyEvent.VK_UP;
                isDownP2 = key == KeyEvent.VK_DOWN;
                isDropP2 = key == ']' ;
                isOneP2 = key == '[';
            }
        }
        if (player1.isStarted() && getCurPiece(player1).getShape() != Tetrominoes.NoShape && !player1.isPaused()){
            isLeft = key == KeyEvent.VK_A;
            isRight = key == KeyEvent.VK_D;
            isUp = key == KeyEvent.VK_W;
            isDown = key == KeyEvent.VK_S;
            isDrop = key == KeyEvent.VK_SPACE;
            isOne = key == KeyEvent.VK_R;
        }
    }
    private void handlePlayerInput(TetrisCanvas player, boolean isDrop, boolean isLeft, boolean isRight, boolean isUp, boolean isDown, boolean isOne) throws InterruptedException {
        if (isDrop) {
            player.dropDown();
            return;
        }
        int newX = getX(player);
        int newY = getY(player);
        if(isOne) --newY;
        if(isUp && !isDown) getCurPiece(player).rotateLeft();
        else if(isDown && !isUp) getCurPiece(player).rotateRight();
        if(isLeft && !isRight) --newX;
        else if(isRight && isLeft) ++newX;
        player.tryMove(getCurPiece(player), newX, newY);
    }
    Entity getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }
    public static void updatePlayer(TetrisCanvas player){
        if(player1==null)player1=player;
        else player2=player;
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