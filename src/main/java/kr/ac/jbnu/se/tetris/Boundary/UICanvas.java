package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.KeyControl;

import javax.swing.*;
import java.awt.*;

public class UICanvas extends JPanel {
    final int BOARD_SIZE_W = 400;
    final int BOARD_SIZE_H = 800;
    Timer timer;//타이머 클래스는 존재. -> 타임레코딩 가능할것이라 예상됨. 필요 리소스 = DB
    public UICanvas(){
        setPreferredSize(new Dimension(BOARD_SIZE_W,BOARD_SIZE_H));
    }

    public Timer getTimer(){ return timer; }
}
