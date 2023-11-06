package kr.ac.jbnu.se.tetris.Control;

//import kr.ac.jbnu.se.tetris.Boundary.TestMonitor;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;
import kr.ac.jbnu.se.tetris.Tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class KeyControl extends KeyAdapter{

    boolean isLeft,isRight,isUp,isDown,isOne,isDrop;
    boolean isLeftP2,isRightP2,isUpP2,isDownP2,isOneP2,isDropP2;
    static TetrisCanvas player1, player2;
    Timer timer;
    Tetris game;
    public KeyControl(Tetris game){
        this.game = game;

        player1 = game.getP1();
        isLeft=false;
        isRight=false;
        isUp=false;
        isDown=false;
        isOne=false;
        isDrop=false;

        player2 = game.getP2();
        isLeftP2=false;
        isRightP2=false;
        isUpP2=false;
        isDownP2=false;
        isOneP2=false;
        isDropP2=false;
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doKeyLogic();
            }
        });
        timer.start();
    }
    public boolean isSingle(){ return player2 != null; }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(!isSingle()){
            if(key == KeyEvent.VK_LEFT){isLeftP2 = false;}
            if(key == KeyEvent.VK_RIGHT){isRightP2 = false;}
            if(key == KeyEvent.VK_UP){isUpP2 = false;}
            if(key == KeyEvent.VK_DOWN){isDownP2 = false;}
            if(key == ']'){isDropP2 = false;}
            if(key == '['){isOneP2 = false;}
        }
        if(key == KeyEvent.VK_A){isLeft = false;}
        if(key == KeyEvent.VK_D){isRight = false;}
        if(key == KeyEvent.VK_W){isUp = false;}
        if(key == KeyEvent.VK_S){isDown = false;}
        if(key == KeyEvent.VK_SPACE){isDrop = false;}
        if(key == KeyEvent.VK_B){isOne = false;}
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (!player1.isStarted() || getCurPiece(player1).getShape() == Tetrominoes.NoShape) {
            return;
        }
        else if(player2!=null){
            if((!player2.isStarted() || getCurPiece(player2).getShape() == Tetrominoes.NoShape))
                return;
        }
        int key = e.getKeyCode();
        if(key=='p'||key=='P'){ player1.pause(); if(player2!=null)player2.pause(); return; }
        if(!isSingle()){
            if (player2.isStarted() || getCurPiece(player2).getShape() != Tetrominoes.NoShape || !player2.isPaused()) {
                if(key == KeyEvent.VK_LEFT&&!isRightP2){isLeftP2 = true;}
                if(key == KeyEvent.VK_RIGHT&&!isLeftP2){isRightP2 = true;}
                if(isRightP2&&isLeftP2){isLeftP2 = false; isRightP2 = false;}
                if(key == KeyEvent.VK_UP){isUpP2 = true;}
                if(key == KeyEvent.VK_DOWN){isDownP2 = true;}
                if(isDownP2&&isUpP2){isUpP2 = false; isDownP2 = false;}
                if(key == ']'&&!isOneP2){isDropP2 = true;}
                if(key == '['&&!isDropP2){isOneP2 = true;}
            }
        }
        if (player1.isStarted() || getCurPiece(player1).getShape() != Tetrominoes.NoShape || !player1.isPaused()) {
            if(key == KeyEvent.VK_A&&!isRight){isLeft = true;}
            if(key == KeyEvent.VK_D&&!isLeft){isRight = true;}
            if(isRight&&isLeft){isLeft = false; isRight = false;}
            if(key == KeyEvent.VK_W&&!isDown){isUp = true;}
            if(key == KeyEvent.VK_S&&!isUp){isDown = true;}
            if(isDown&&isUp){isUp = false; isDown = false;}
            if(key == KeyEvent.VK_SPACE&&!isOne){isDrop = true;}
            if(key == KeyEvent.VK_B&&!isDrop){isOne = true;}
        }
    }
    public void doKeyLogic(){
        /**
         * 일시정지, 드롭다운은 즉각 처리. switch문 이전에 KeyPressed에서 처리함
         * 아래는 키값의 상수처리, 이를 합연산으로 처리 구분*/
        if(isSingle()){
            if(isDropP2){player2.dropDown();return;}
            if(isLeftP2) player2.tryMove(getCurPiece(player2),getX(player2)-1,getY(player2));
            if(isRightP2) player2.tryMove(getCurPiece(player2),getX(player2)+1,getY(player2));
            if(isUpP2) getCurPiece(player2).rotateLeft();
            if(isDownP2) getCurPiece(player2).rotateRight();
            if(isOneP2) player2.tryMove(getCurPiece(player2),getX(player2),getY(player2)-1);
        }
        if(isDrop) {player1.dropDown(); return;}
        if(isLeft) player1.tryMove(getCurPiece(player1),getX(player1)-1,getY(player1));
        if(isRight) player1.tryMove(getCurPiece(player1),getX(player1)+1,getY(player1));
        if(isUp) getCurPiece(player1).rotateLeft();
        if(isDown) getCurPiece(player1).rotateRight();
        if(isOne) player1.tryMove(getCurPiece(player1),getX(player1),getY(player1)-1);
    }
    Entity getCurPiece(TetrisCanvas player){ return player.getCurPiece(); }
    int getX(TetrisCanvas player){ return getCurPiece(player).getCurX(); }
    int getY(TetrisCanvas player){ return getCurPiece(player).getCurY(); }
}