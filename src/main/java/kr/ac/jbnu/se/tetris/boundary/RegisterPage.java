package kr.ac.jbnu.se.tetris.boundary;

import com.google.firebase.auth.FirebaseAuthException;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class RegisterPage extends LogInPage {
    public RegisterPage() throws IOException {
        super();
    }
    @Override
    public void setBtnConfirm(){
        btnConfirm.addActionListener(e -> {
            try {
                if(FirebaseTool.getInstance().signUp(
                        new Account(idBox.getText(), pwBox.getPassword()))
                )FrameMain.getBackPanel().pop();
            } catch (FirebaseAuthException | ExecutionException | InterruptedException | IOException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        });
    }
}
