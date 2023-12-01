package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends LogInPage {
    public RegisterPage() {
        super();
    }
    @Override
    public void setBtnConfirm() {
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(FirebaseTool.getInstance().signUp(
                        new Account(idBox.getText(), pwBox.getPassword()))
                )FrameMain.getBackPanel().pop();
            }
        });
    }
}
