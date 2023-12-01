package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_H;
import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_W;

public class InGamePage extends JPanel {
    private static InGamePage instance = null;
    InGamePage(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(BOARD_SIZE_W*3,BOARD_SIZE_H));
        setBorder(new EmptyBorder(0,0,0,0));
        FrameMain.getBackPanel().push(this);
        addKeyListener(KeyControl.getInstance());
        setFocusable(true);
        requestFocusInWindow();
    }
    @Override
    public Component add(Component comp) {
        comp.setVisible(true);
        if(comp instanceof TetrisCanvas){
            if(this.getComponentCount() == 2) add(comp,BorderLayout.EAST);
            else add(comp,BorderLayout.WEST);
        }
        else if(comp instanceof UICanvas)add(comp,BorderLayout.CENTER);
        revalidate();
        return comp;
    }
    public static InGamePage getInstance(){
        if(instance==null){
            synchronized (InGamePage.class){
                instance = new InGamePage();
            }
        }
        return instance;
    }
}

