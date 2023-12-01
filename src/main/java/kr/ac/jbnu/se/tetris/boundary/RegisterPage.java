package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

public class RegisterPage extends LogInPage {
    public RegisterPage() {
        super();
    }
    @Override
    public void setBtnConfirm() {
        btnConfirm.addActionListener(e -> {
            if(FirebaseTool.getInstance().signUp(
                    new Account(idBox.getText(), pwBox.getPassword()))
            )FrameMain.getBackPanel().pop();
        });
    }
}
