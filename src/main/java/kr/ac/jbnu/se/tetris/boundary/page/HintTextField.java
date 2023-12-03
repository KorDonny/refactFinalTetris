package kr.ac.jbnu.se.tetris.boundary.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class HintTextField extends JTextField {
    public HintTextField(String hint) {
        setForeground(Color.GRAY);

        // 텍스트 필드가 포커스를 얻거나 잃을 때 이벤트 처리
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hint);
                    setForeground(Color.GRAY);
                }
            }
        });
    }
}
