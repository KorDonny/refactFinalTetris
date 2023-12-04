package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.FrameMain;
import kr.ac.jbnu.se.tetris.boundary.page.GameMenuPage;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.control.TetrisTimerTask;
import kr.ac.jbnu.se.tetris.control.TimerManager;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.entity.CanvasBuff;
import kr.ac.jbnu.se.tetris.entity.PreviewBlock;
import kr.ac.jbnu.se.tetris.entity.WorkFlow;
import kr.ac.jbnu.se.tetris.entity.numeric.GameMode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class UICanvas extends JPanel{
    private static UICanvas instance = null;
    public static final int BOARD_SIZE_W = 350;
    public static final int BOARD_SIZE_H = 700;
    public static final int BOARD_HGAP = 50;
    private final CanvasBuff uiBuff  = new CanvasBuff();
    private PreviewBlock mainPreview = null;
    private PreviewBlock subPreview = null;
    private static int p1Score;
    private static String p1Text = Account.getClientAccount().getNickName()+" : "+0;
    private static int p2Score;
    private static String p2Text = "0";
    private final WorkFlow uiCanvasWorks = new WorkFlow(this);
    public UICanvas() throws IOException {
        setOpaque(false);
        setPreferredSize(new Dimension(BOARD_SIZE_W,BOARD_SIZE_H));
        if(!(this instanceof TetrisCanvas)){
            TimerManager.getInstance();
            TimerManager.addTask(uiCanvasWorks, new TetrisTimerTask() {
                @Override
                public void doLogic(){
                    repaint();
                }
            },100);
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (mainPreview != null && mainPreview.checkReady()){
            mainPreview.drawPreview(g);
        }
        if (subPreview != null && subPreview.checkReady()){
            subPreview.drawPreview(g);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(uiBuff.getSprite(), 0, 0, null);
        g.setFont(new Font("SansSerif",Font.BOLD, FrameMain.FONT_TITLE));
        if(GameMenuPage.getMode() == GameMode.MULTI){
            updateScoreText(g,p2Text);
        }
        updateScoreText(g,p1Text);
    }
    public PreviewBlock getPreview(int previewNum) {
        if (mainPreview == null) {
            mainPreview = new PreviewBlock(previewNum, false);
            return mainPreview;
        } else if (subPreview == null) {
            subPreview = new PreviewBlock(previewNum, true);
            return subPreview;
        }
        return null;
    }
    public static void updateDBScore(TetrisCanvas canvas) throws ExecutionException, InterruptedException{
        Account account;
        if(canvas == KeyControl.getPlayer(true) && GameMenuPage.getMode() == GameMode.MULTI) account = Account.getLocalMultiAccount();
        else if(canvas == KeyControl.getPlayer(false)) account = Account.getClientAccount();
        else return;
        int result = account == Account.getClientAccount() ? p1Score : p2Score;
        if(FirebaseTool.getUserBestScore(account,GameMenuPage.getMode())<result){
            // 그래픽 업데이트 코드
            instance.getGraphics().setColor(Color.green);
            p1Text = "Congrats! New Record! : " + result;
            instance.repaint(); // 화면 갱신
        }
        else{
            instance.getGraphics().setColor(Color.cyan);
            p1Text = "Game Over : "+result;
            instance.repaint();
        }
    }
    private void updateScoreText(Graphics g, String text){
        if(GameMenuPage.getMode() == GameMode.MULTI){
            g.setColor(Color.BLUE);
            g.drawString(text,BOARD_SIZE_W/2,BOARD_HGAP*2);
        }
        g.setColor(Color.RED);
        g.drawString(text,BOARD_SIZE_W/2,GameMenuPage.getMode() == GameMode.MULTI ? BOARD_SIZE_H-BOARD_HGAP*2 : BOARD_SIZE_H/2);
    }
    public static void updateScore(TetrisCanvas canvas,int score) throws IOException {
        if(canvas == KeyControl.getPlayer(true) && GameMenuPage.getMode() == GameMode.MULTI) {
            p2Score = score;
            p2Text = Account.getLocalMultiAccount().getNickName()+" : "+p2Score;
        }
        else {
            p1Score = score;
            p1Text = Account.getClientAccount().getNickName()+" : "+p1Score;
        }
        getInstance().repaint();
    }
    public static UICanvas getInstance() throws IOException {
        if(instance==null){
            synchronized (UICanvas.class){
                instance = new UICanvas();
            }
        }
        return instance;
    }
    public static synchronized void exitProg() throws IOException {
        p1Score = 0;
        p2Score = 0;
        TimerManager.removeTask(getInstance().uiCanvasWorks);
        instance = null;
    }
}
