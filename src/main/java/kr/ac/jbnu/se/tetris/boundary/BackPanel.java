package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.FrameMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_HEIGHT;
import static kr.ac.jbnu.se.tetris.FrameMain.WINDOW_WIDTH;

public class BackPanel extends JPanel {
    private static BackPanel instance = null;
    private static final Deque<JPanel> viewStack;
    static{
        viewStack = new ArrayDeque<>();
    }
    BufferedImage backImg;
    static final String BACK_IMG_PATH = FrameMain.IMAGE_DIR_PATH+"background.png";
    public BackPanel() throws IOException {
        backImg = ImageIO.read(new File(BACK_IMG_PATH));
        setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
    }
    @Override
    public void paintComponent(Graphics g){
        int x = (getWidth() - WINDOW_WIDTH) / 2;
        int y = (getHeight() - WINDOW_HEIGHT) / 2;
        g.drawImage(backImg.getScaledInstance(WINDOW_WIDTH,WINDOW_HEIGHT,Image.SCALE_SMOOTH),
                x,y,null);
    }
    /** 디큐의 최상단에 삽입, 이후 상단 표시 ~= 화면 진입 @ 디큐 기능 문제시 error 반환 (peek은 null리턴) */
    public void push(JPanel target){
        if(!viewStack.isEmpty())viewStack.getFirst().setVisible(false);
        setBorder(new EmptyBorder((WINDOW_WIDTH-target.getWidth())/2,(WINDOW_HEIGHT-target.getHeight())/2,(WINDOW_WIDTH-target.getWidth())/2,(WINDOW_HEIGHT-target.getHeight())/2));
        setBorder(BorderFactory.createEmptyBorder());
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
        revalidate();
    }
    /** 컴포넌트 알림창 사용시 부모 창을 붙여주기 위해 메소드 구현 */
    public Component top(){
        return viewStack.peekFirst();
    }
    public static BackPanel getInstance() throws IOException {
        if(instance==null){
            synchronized (BackPanel.class){
                instance = new BackPanel();
            }
        }
        return instance;
    }
}