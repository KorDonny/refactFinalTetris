package kr.ac.jbnu.se.tetris;

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
