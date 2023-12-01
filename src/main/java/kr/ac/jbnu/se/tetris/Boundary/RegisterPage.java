package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
