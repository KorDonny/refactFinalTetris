package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.FrameMain;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    BufferedImage backImg;
    static final String BACK_IMG_PATH = FrameMain.IMAGE_DIR_PATH+"background.png";
    boolean isGameFirst;
    public BackPanel() throws IOException {
        viewStack = new ArrayDeque<>();
        backImg = ImageIO.read(new File(BACK_IMG_PATH));
        isGameFirst = false;
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(backImg.getScaledInstance(WINDOW_WIDTH,WINDOW_HEIGHT,Image.SCALE_SMOOTH),
                0,0,null);
    }
    /** 디큐의 최상단에 삽입, 이후 상단 표시 ~= 화면 진입 @ 디큐 기능 문제시 error 반환 (peek은 null리턴) */
    public void push(JPanel target){
        if(!viewStack.isEmpty())viewStack.getFirst().setVisible(false);
        target.setOpaque(false);
        target.setVisible(true);
        viewStack.addFirst(target);
        add(viewStack.getFirst());
        revalidate();
    }
    /** 뷰 스택의 최상단을 가리고, 이후 제거 및 다음 최상단 표시 ~= 뒤로가기 @ 표시할 요소가 디큐에 없으면 error반환 (poll은 null리턴) */
    public void pop(){
        if(viewStack.isEmpty())return;
        viewStack.removeFirst().setVisible(false);
        viewStack.getFirst().setVisible(true);
        this.revalidate();
    }
    public static void addTask(Object obj, TimerTask task,long period){
        if(timer==null)timer = new Timer("Game Timer");
        if(timerMap==null)timerMap = new HashMap<>();
        if(timerMap.get(obj)==null){
            timerMap.put(obj,task);
            timer.scheduleAtFixedRate(task,0,period);
        }
    }
    /*
    * Timer 중앙화, Task에 period만 전달하여 수행. 단, delay는 빠짐.
    * 문제점 : 동기화 문제(기술 이해도 부족), obj의 주소값을 전달하여 인덱싱 하려 했으나
    * 같은 종류의 클래스가 들어가면 같이 취소되버림.
    * -------------최종 코드 제출시 삭제 예정(다이어그램 도시 과정때 삭제후 작업 요망)------------------
    * */
    /** 특정 타이머 태스크 존재시 반환 */
    public static TimerTask getTask(Object obj){ return timerMap.get(obj); }
    /** 특정 타이머 태스크만 삭제 */
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
    /** 특정 스레드만 wait 상태에서 재시작 */
    public static void resumeTask(Object obj){ timerMap.get(obj).notify(); }
    /** 모든 스레드 재시작  */
    public static void resumeAllTask(){ timer.notifyAll(); }
    public static Timer getTimer(){ return timer; }
}
