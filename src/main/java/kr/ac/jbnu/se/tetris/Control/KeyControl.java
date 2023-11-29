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
                    if(player2!=null)handlePlayerInput(player2, isDropP2, isLeftP2, isRightP2, isUpP2, isDownP2, isOneP2);
                    handlePlayerInput(player1, isDrop, isLeft, isRight, isUp, isDown, isOne);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },150);
    }
    public boolean isSingle(){ return player2 == null; }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

//        if(!isSingle()){
//            isLeftP2 = key == KeyEvent.VK_LEFT;
//            isRightP2 = key == KeyEvent.VK_RIGHT;
//            isUpP2 = key == KeyEvent.VK_UP;
//            isDownP2 = key == KeyEvent.VK_DOWN;
//            isDropP2 = key == ']';
//            isOneP2 = key == '[';
//        }
//        isLeft = key == KeyEvent.VK_A;
//        isRight = key == KeyEvent.VK_D;
//        isUp = key == KeyEvent.VK_W;
//        isOne = key == KeyEvent.VK_S;
//        isDrop = key == KeyEvent.VK_SPACE;
//        isDown = key == KeyEvent.VK_R;
        if (!isSingle()) {
            isLeftP2 = key == KeyEvent.VK_RIGHT ? false : true;
            isRightP2 = key == KeyEvent.VK_RIGHT ? false : true;
            isUpP2 = key == KeyEvent.VK_UP ? false : true;
            isDownP2 = key == KeyEvent.VK_DOWN ? false : true;
            isDropP2 = key == ']' ? false : true;
            isOneP2 = key == '[' ? false : true;
        }
        isLeft = key == KeyEvent.VK_A ? false : true;
        isRight = key == KeyEvent.VK_D ? false : true;
        isUp = key == KeyEvent.VK_W ? false : true;
        isOne = key == KeyEvent.VK_S ? false : true;
        isDrop = key == KeyEvent.VK_SPACE ? false : true;
        isDown = key == KeyEvent.VK_R ? false : true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        int key = e.getKeyCode();
//        if(key=='p'||key=='P'){
//            try {
//                if(player2!=null)player2.pause();
//                player1.pause();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//            return;
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
                isLeftP2 = key == KeyEvent.VK_RIGHT;
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
            isOne = key == KeyEvent.VK_S;
            isDrop = key == KeyEvent.VK_SPACE;
            isDown = key == KeyEvent.VK_R;
        }
    }
//    public void doKeyLogic() throws InterruptedException {
//        /**
//         * 일시정지, 드롭다운은 즉각 처리. switch문 이전에 KeyPressed에서 처리함
//         * 아래는 키값의 상수처리, 이를 합연산으로 처리 구분*/
//        if(!isSingle()){
//            if(isDropP2){player2.dropDown();return;}
//            if(isLeftP2) player2.tryMove(getCurPiece(player2),getX(player2)-1,getY(player2));
//            if(isRightP2) player2.tryMove(getCurPiece(player2),getX(player2)+1,getY(player2));
//            if(isUpP2) player2.tryMove(getCurPiece(player2).rotateLeft(),getX(player2),getY(player2));
//            if(isDownP2) player2.tryMove(getCurPiece(player2).rotateRight(),getX(player2),getY(player2));
//            if(isOneP2) player2.tryMove(getCurPiece(player2),getX(player2),getY(player2)-1);
//        }
//        if(isDrop) {player1.dropDown(); return;}
//        if(isLeft) player1.tryMove(getCurPiece(player1),getX(player1)-1,getY(player1));
//        if(isRight) player1.tryMove(getCurPiece(player1),getX(player1)+1,getY(player1));
//        if(isUp) player1.tryMove(getCurPiece(player1).rotateLeft(),getX(player1),getY(player1));
//        if(isDown) player1.tryMove(getCurPiece(player1).rotateRight(),getX(player1),getY(player1));
//        if(isOne) player1.tryMove(getCurPiece(player1),getX(player1),getY(player1)-1);
//    }
    private void handlePlayerInput(TetrisCanvas player, boolean isDrop, boolean isLeft, boolean isRight, boolean isUp, boolean isDown, boolean isOne) throws InterruptedException {
        if (isDrop) {
            player.dropDown();
            return;
        }
        int newX = getX(player);
        int newY = getY(player);
        if(isUp && !isDown) getCurPiece(player).rotateLeftA();
        else if(isDown && !isUp) getCurPiece(player).rotateRightA();
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
        if(isP2&&player2!=null)return player2;
        else return player1;
    }
}