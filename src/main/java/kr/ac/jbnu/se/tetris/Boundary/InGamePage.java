package kr.ac.jbnu.se.tetris.Boundary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InGamePage extends JPanel {
    private static InGamePage instance = null;
    InGamePage(){
        Icon imgIcon = new ImageIcon(this.getClass().getResource("ajax-loader.gif"));
        JLabel label = new JLabel(imgIcon);
        label.setBounds(668, 43, 46, 14); // for example, you can use your own values
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension());
    }
    @Override
    public Component add(Component comp) {
        if(comp instanceof TetrisCanvas){
            if(this.getComponentCount() == 2) add(comp,BorderLayout.EAST);
            else add(comp,BorderLayout.WEST);
        }
        else if(comp instanceof UICanvas)add(comp,BorderLayout.CENTER);
        return null;
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

