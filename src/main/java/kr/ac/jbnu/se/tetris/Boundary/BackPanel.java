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
    static HashMap<Object,TimerTask> timerMap;
    BufferedImage background;
    final String backgroundPath = "./src/main/java/kr/ac/jbnu/se/tetris/Resource/background.png";
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
    public static void addTask(Object obj, TimerTask task,long period){
        if(timer==null)timer = new Timer("Game Timer");
        if(timerMap==null)timerMap = new HashMap<>();
        if(timerMap.get(obj)==null){
            timerMap.put(obj,task);
            timer.scheduleAtFixedRate(task,0,period);
        }
    }
    public static TimerTask getTask(Object obj){ return timerMap.get(obj); }
    public static void removeTask(Object obj){ if(timerMap.get(obj)!=null)timerMap.get(obj).cancel(); }
    public static void stopTask(Object obj) {
        synchronized (obj) {
            try {
                TimerTask task = timerMap.get(obj);
                if (task != null) {
                    task.cancel();
                    timerMap.remove(obj);
                }
                obj.notify();  // 대기 중인 스레드에게 안전하게 진행할 수 있음을 알림
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
        }
    }
    public static void resumeTask(Object obj){ timerMap.get(obj).notify(); }
    public static void resumeAllTask(){ timer.notifyAll(); }
    public void setGameUIFrame(){
        setBorder(25,100,75,100);
    }
}