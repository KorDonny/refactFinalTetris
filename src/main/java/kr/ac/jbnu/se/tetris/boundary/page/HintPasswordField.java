package kr.ac.jbnu.se.tetris.boundary.page;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class HintPasswordField extends JPasswordField {
    public HintPasswordField(String hint) {
        setEchoChar((char) 0); // 초기에는 힌트가 보이도록
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(getPassword()).equals(hint)) {
                    setText("");
                    setEchoChar('•'); // 입력이 시작되면 가려지도록
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(getPassword()).isEmpty()) {
                    setText(hint);
                    setEchoChar((char) 0);
                }
            }
        });
        setText(hint);
    }
}
