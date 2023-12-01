package kr.ac.jbnu.se.tetris.boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_HEIGHT;
import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_WIDTH;

public class BackPanel extends JPanel {
    private Deque<JPanel> viewStack;
    static Timer timer;
    static HashMap<Object,TimerTask> timerMap;
    BufferedImage backgroundImg;
    static final String BACKGROUND_PATH = "./src/main/java/kr/ac/jbnu/se/tetris/Resource/Image/background.png";
    boolean isGameFirst;
    public BackPanel() throws IOException {
        viewStack = new ArrayDeque<>();
        backgroundImg = ImageIO.read(new File(BACKGROUND_PATH));
        isGameFirst = false;
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(backgroundImg.getScaledInstance(WINDOW_WIDTH,WINDOW_HEIGHT,Image.SCALE_SMOOTH),
                0,0,null);
    }
    public void push(JPanel target){
        if(!viewStack.isEmpty())viewStack.peek().setVisible(false);
        target.setOpaque(false);
        target.setVisible(true);
        viewStack.add(target);
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
    /** timer는 하나로 관리하고 Task를 넣고자 함. SonarLint 리팩토링은 V와 computeIfAbsent에 대한 숙지가 되지 않아 리팩토링 금지 */
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
        TimerTask task = timerMap.get(obj);
        if(task == null)return;
        synchronized (task){
            task.cancel();
            timerMap.remove(obj);
            obj.notifyAll();  // 대기 중인 스레드에게 안전하게 진행할 수 있음을 알림
        }

    }
    public static void resumeTask(Object obj){
        synchronized (timerMap.get(obj)){
            timerMap.get(obj).notifyAll();
        }
    }
    public static void resumeAllTask(){
        synchronized (timer){
            timer.notifyAll();
        }
    }
    public void setGameUIFrame(){
        setBorder(25,100,75,100);
    }
    public static Timer getTimer(){ return timer; }
}
