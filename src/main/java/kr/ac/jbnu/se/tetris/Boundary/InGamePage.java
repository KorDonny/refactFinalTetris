package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.Handler.AIModeHandler;
import kr.ac.jbnu.se.tetris.Control.Handler.GameModeHandler;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InGamePage extends JPanel {
    private static InGamePage instance;
    private final int UP_BORDER=50,SIDE_BORDER=100;

    InGamePage() throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(UICanvas.BOARD_SIZE_W*3+SIDE_BORDER*2, UICanvas.BOARD_SIZE_H));
        setLocation((FrameMain.WINDOW_WIDTH-this.getWidth())/2,(FrameMain.WINDOW_HEIGHT-this.getHeight())/2);
        setBorder(new EmptyBorder(UP_BORDER/2,SIDE_BORDER/2,UP_BORDER/2,SIDE_BORDER/2));
        FrameMain.getBackPanel().push(this);
        setFocusable(true);
        requestFocusInWindow(true);
        addKeyListener(KeyControl.getInstance());
        GameMenuPage.getModeHandler().startGame();
    }
    @Override
    public Component add(Component comp) {
        comp.setVisible(true);
        if(comp instanceof TetrisCanvas){
            if(this.getComponentCount() == 2) add(comp,BorderLayout.EAST);
            else add(comp,BorderLayout.WEST);
        }
        else if(comp instanceof UICanvas){
            add(comp,BorderLayout.CENTER);
        }
        revalidate();
        return null;
    }
    public static InGamePage getInstance() throws IOException, InterruptedException {
        if(instance==null){
            synchronized (InGamePage.class){
                instance = new InGamePage();
            }
        }
        return instance;
    }
    enum TabBtnEnum{
        RESTART(new JButton("다시시작")),QUIT(new JButton("나가기"));
        private JButton btn;
        TabBtnEnum(JButton btn){
            this.btn=btn;
        }
        public JButton getBtn(){ return btn; }
    }
    public void initSettingsTab(){
        setLayout(new GridLayout(1,TabBtnEnum.values().length));
        TabBtnEnum.RESTART.getBtn().addActionListener(e -> {
            try {
                if(KeyControl.getPlayer(true)!=null)KeyControl.getPlayer(true).restart();
                else if(GameMenuPage.getModeHandler() instanceof AIModeHandler)GameMenuPage.getModeHandler().getCanvas().restart();
                KeyControl.getPlayer(false).restart();
            } catch (InterruptedException ex) { throw new RuntimeException(ex); }
        });
        TabBtnEnum.QUIT.getBtn().addActionListener(e -> FrameMain.getBackPanel().pop());
        for(TabBtnEnum btn : TabBtnEnum.values()){
            btn.getBtn().setVisible(true);
            add(btn.getBtn());
        }
    }
    public static void getSettingsTab(){
//        if(settings==null)return;
//        try{
//            if(KeyControl.getPlayer(true)!=null)KeyControl.getPlayer(true).pause();
//            else if(InGamePage.getModeType() instanceof AIModeHandler)InGamePage.getModeType().getCanvas().pause();
//            KeyControl.getPlayer(false).pause();
//        }catch (InterruptedException ex) {throw new RuntimeException(ex);}
    }
}

