package kr.ac.jbnu.se.tetris.entity;

import kr.ac.jbnu.se.tetris.*;
import kr.ac.jbnu.se.tetris.boundary.page.GameMenuPage;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvasAI;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.entity.numeric.GameMode;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_H;
import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_W;

public interface CanvasScoreInterface {
    JLabel player1Score = new JLabel("0");
    JLabel player2Score = new JLabel("0");
    static void setScoreAttribute(){
        player1Score.setOpaque(false);
        player1Score.setFont(new Font("SansSerif",Font.BOLD, FrameMain.FONT_TITLE));
        player1Score.setForeground(Color.RED);
        if(GameMenuPage.getMode() == GameMode.MULTI){
            player2Score.setOpaque(false);
            player2Score.setForeground(Color.BLUE);
            player2Score.setFont(new Font("SansSerif",Font.BOLD, FrameMain.FONT_TITLE));
            player2Score.setLocation(BOARD_SIZE_W/2,BOARD_SIZE_H-50);
            player1Score.setLocation(BOARD_SIZE_W/2, 50);
        }
        else player1Score.setLocation(BOARD_SIZE_W/2, BOARD_SIZE_H/2);
    }
    static void updateDBScore(TetrisCanvas canvas) throws ExecutionException, InterruptedException {
        Account account = checkCanvas(canvas);
        if(account == null)return;
        int result;
        JLabel target;
        GameMode mode = GameMenuPage.getMode();
        target = account == Account.getLocalMultiAccount() ? player2Score : player1Score;
        result = Integer.parseInt(target.getText().split(" : ")[1]);
        if(result > FirebaseTool.getUserBestScore(account,mode)){
            target.setText("Congrats! New Records! : "+result);
            FirebaseTool.updateUserBestScore(account,result,mode);
        }
        else target.setText("Game Over : "+result);
    }
    static Account checkCanvas(TetrisCanvas canvas){
        if(canvas instanceof TetrisCanvasAI)return null;
        if(canvas == KeyControl.getPlayer(true))return Account.getLocalMultiAccount();
        return Account.getClientAccount();
    }
    static void updateScore(TetrisCanvas canvas){
        Account account = checkCanvas(canvas);
        if(account == null)return;
        JLabel target = account == Account.getLocalMultiAccount() ? player2Score : player1Score;
        target.setText(account.getNickName()+" : "+canvas.getNumLinesRemoved());
    }
}
