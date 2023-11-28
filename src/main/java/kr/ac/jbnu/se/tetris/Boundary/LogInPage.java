package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LogInPage extends JPanel {
    JButton btnConfirm, btnReject;
    JTextField idBox;
    JPasswordField pwBox;
    JLabel idLab, pwLab;
    GridLayout layout;
    FlowLayout layout1, layout2;
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
        this.setOpaque(false);
        int uiTopMargin = (FrameMain.WINDOW_HEIGHT)/3;
        this.setBorder(new EmptyBorder(uiTopMargin,0,0,0));

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
        idBox = new JTextField(); idBox.setPreferredSize(new Dimension(170, 30));
        pwBox = new JPasswordField(); pwBox.setPreferredSize(new Dimension(170, 30));

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
        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameMain.getBackPanel().pop();
            }
        });
    }
    public boolean checkID(String id, String pw) throws IOException {
        if(firebaseTool.logIn(id,pw)){
            JOptionPane.showMessageDialog(null, "반갑습니다!");
            FrameMain.getInstance().getBackPanel().push(new MenuPage());
            return true;
        }
        JOptionPane.showMessageDialog(null, "회원정보를 확인하세요.");
        return false;
    }
    public void setBtnConfirm(){
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkID(idBox.getText(),null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
