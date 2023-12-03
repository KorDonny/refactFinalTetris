package kr.ac.jbnu.se.tetris.boundary.page;

import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;

import java.io.IOException;

public class RegisterPage extends LogInPage {
    public RegisterPage() {
        super();
    }
    @Override
    public void setBtnConfirm() {
        btnConfirm.addActionListener(e -> {
            if(FirebaseTool.getInstance().signUp(
                    new Account(idBox.getText(), pwBox.getPassword()))
            ) {
                try {
                    BackPanel.getInstance().pop();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
