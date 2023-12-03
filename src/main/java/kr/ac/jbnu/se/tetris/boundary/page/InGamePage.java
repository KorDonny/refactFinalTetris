package kr.ac.jbnu.se.tetris.boundary.page;

import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.control.KeyControl;
import kr.ac.jbnu.se.tetris.control.TimerManager;
import kr.ac.jbnu.se.tetris.boundary.UICanvas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_H;
import static kr.ac.jbnu.se.tetris.boundary.UICanvas.BOARD_SIZE_W;

public class InGamePage extends JPanel {
    private static InGamePage instance = null;
    InGamePage() throws IOException {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(BOARD_SIZE_W*3,BOARD_SIZE_H));
        setBorder(new EmptyBorder(0,0,0,0));
        BackPanel.getInstance().push(this);
        addKeyListener(KeyControl.getInstance());
        setFocusable(true);
        requestFocusInWindow();
        TimerManager.getInstance();
    }
    @Override
    public Component add(Component comp) {
        comp.setVisible(true);
        if(comp instanceof TetrisCanvas){
            if(getComponentCount() == 2) add(comp, BorderLayout.EAST);
            else add(comp, BorderLayout.WEST);
        }
        else if(comp instanceof UICanvas) {
            add(comp, BorderLayout.CENTER);
        }
        revalidate();
        return comp;
    }
    public static InGamePage getInstance() throws IOException {
        if(instance==null){
            synchronized (InGamePage.class){
                instance = new InGamePage();
            }
        }
        return instance;
    }
}

