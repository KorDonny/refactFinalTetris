package kr.ac.jbnu.se.tetris.boundary.page;

import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.boundary.BackPanel;
import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class LogInPage extends JPanel {
    JButton btnConfirm;
    JButton btnReject;
    HintTextField idBox;
    HintPasswordField pwBox;
    JLabel idLab;
    JLabel pwLab;
    GridLayout layout;
    FlowLayout layout1;
    FlowLayout layout2;
    JComponent[] list;
    FirebaseTool firebaseTool;
    public LogInPage(){
        //DB 아이디 체킹을 위한 연결
        firebaseTool= FirebaseTool.getInstance();

        //메인 레이아웃 구성 및 컴포넌트 마진 설정.
        layout = new GridLayout(3,1);
        this.setLayout(layout);
        layout.setVgap(20);
        layout.setHgap(0);
        int uiTopMargin = (FrameMain.WINDOW_HEIGHT)/3;
        this.setBorder(new EmptyBorder(uiTopMargin,0,0,0));

        setFirstUI();
    }
    public boolean checkID(Account account) throws IOException {
        if(Account.getClientAccount()==null){
            Account.updateClientAccount(firebaseTool.logIn(account));
            if(Account.getClientAccount()!=null){
                JOptionPane.showMessageDialog(null, "반갑습니다!");
                BackPanel.getInstance().push(new MenuPage());
                return true;
            }
            return false;
        }
        else{
            Account.updateLocalMultiAccount(firebaseTool.logIn(account));
            if(Account.getLocalMultiAccount()!=null){
                JOptionPane.showMessageDialog(null, "반갑습니다! 플레이어 2님!");
                return true;
            }
            JOptionPane.showMessageDialog(null, "회원정보를 확인하세요.");
            return false;
        }
    }
    public void setBtnConfirm(){
        btnConfirm.addActionListener(e -> {
            try {
                checkID(new Account(idBox.getText(),pwBox.getPassword()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void setFirstUI(){
        //각 컴포넌트들을 표시하기 위한 레이어 구성
        layout1 = new FlowLayout(FlowLayout.CENTER,20,20);
        layout2 = new FlowLayout(FlowLayout.CENTER,100,20);
        JPanel idPart = new JPanel(); idPart.setLayout(layout1); setOpaque(false);
        JPanel pwPart = new JPanel(); pwPart.setLayout(layout1); setOpaque(false);
        JPanel btnPart = new JPanel(); btnPart.setLayout(layout2); setOpaque(false);

        //컴포넌트 할당
        btnConfirm = new JButton("확인"); btnReject = new JButton("취소");
        idLab = new JLabel("아이디"); idLab.setOpaque(false); idLab.setForeground(Color.WHITE);
        pwLab = new JLabel("비밀번호"); pwLab.setOpaque(false); pwLab.setForeground(Color.WHITE);
        idBox = new HintTextField("이메일@도메인"); idBox.setPreferredSize(new Dimension(170, 30));
        pwBox = new HintPasswordField("6자 이상"); pwBox.setPreferredSize(new Dimension(170, 30));

        //컴포넌트들 표시를 위한 오퍼레이션 구문
        list = new JComponent[]{idLab, idBox, pwLab, pwBox, btnConfirm, btnReject};
        JPanel[] partList = new JPanel[]{idPart,pwPart,btnPart};
        int i = 0;
        for (JPanel panel : partList){
            panel.add(list[i++]); panel.add(list[i++]);
            panel.setVisible(true);
            panel.setOpaque(false);
            add(panel);
        }

        //확인버튼 액션 = 조건 참일시 MenuPage로 진입
        setBtnConfirm();
        //취소버튼 액션 = 메인 화면으로 회귀
        btnReject.addActionListener(e -> {
            try {
                BackPanel.getInstance().pop();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
