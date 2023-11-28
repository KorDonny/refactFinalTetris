package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.KeyControl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_HEIGHT;
import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_WIDTH;

public class BackPanel extends JPanel {
    Stack<JPanel> viewStack;
    static Timer timer;
    static HashMap<String,TimerTask> timerMap;
    BufferedImage background;
    final String backgroundPath = "./src/main/java/kr/ac/jbnu/se/tetris/background.png";
    boolean isGameFirst;
    public BackPanel() throws IOException {
        viewStack = new Stack<>();
        background = ImageIO.read(new File(backgroundPath));
        isGameFirst = false;
    }

    public void paintComponent(Graphics g){
        g.drawImage(background.getScaledInstance(WINDOW_WIDTH,WINDOW_HEIGHT,Image.SCALE_SMOOTH),
                0,0,null);
    }
    public void push(JPanel target){
        if(!viewStack.isEmpty())viewStack.peek().setVisible(false);
        target.setVisible(true);
        viewStack.add(target);
        if(target instanceof UICanvas&&!(target instanceof TetrisCanvas)) {
            setGameUIFrame();
            target.setFocusable(true); // 키입력 강제로 받도록 설정.
            target.addKeyListener(KeyControl.getInstance());
        }
        add(viewStack.peek());
        revalidate();
    }
    /** 뷰 스택의 최상단을 가리고, 이후 제거 및 다음 최상단 표시 ~= 뒤로가기. */
    public void pop(){
        if(viewStack.isEmpty())return;
        viewStack.pop().setVisible(false);
        viewStack. peek().setVisible(true);
        this.revalidate();
    }
    /** 바깥여백. */
    public void setBorder(int top, int left, int bottom, int right) {
        super.setBorder(new EmptyBorder(top, left, bottom, right));
    }
    public static void addTask(String taskID, TimerTask task,long period){
        if(timer==null)timer = new Timer("Game Timer");
        if(timerMap==null)timerMap = new HashMap<>();
        if(timerMap.get(taskID)==null){
            timerMap.put(taskID,task);
            timer.scheduleAtFixedRate(task,0,period);
        }
    }
    public static TimerTask getTask(String taskID){ return timerMap.get(taskID); }
    public static void removeTask(String taskID){ if(timerMap.get(taskID)!=null)timerMap.get(taskID).cancel(); }
    public static void stopTask(String taskID) throws InterruptedException { timerMap.get(taskID).wait(); }
    public static void resumeTask(String taskID){ timerMap.get(taskID).notify(); }
    public static void resumeAllTask(){ timer.notifyAll(); }
    public static void startTask(String taskID){ timerMap.get(taskID).run(); }
    public void setGameUIFrame(){
        setBorder(100,100,100,100);

    }
}
