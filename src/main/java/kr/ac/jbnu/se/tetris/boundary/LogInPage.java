package kr.ac.jbnu.se.tetris.boundary;

import kr.ac.jbnu.se.tetris.control.FirebaseTool;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LogInPage extends JPanel {
    JButton btnConfirm, btnReject;
    HintTextField idBox;
    HintPasswordField pwBox;
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
        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameMain.getBackPanel().pop();
            }
        });
    }
    public boolean checkID(Account account) throws IOException {
        if(Account.getClientAccount()==null){
            Account.updateClientAccount(firebaseTool.logIn(account));
            if(Account.getClientAccount()!=null){
                JOptionPane.showMessageDialog(null, "반갑습니다!");
                FrameMain.getInstance().getBackPanel().push(new MenuPage());
                return true;
            }
            JOptionPane.showMessageDialog(null, "회원정보를 확인하세요.");
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
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkID(new Account(idBox.getText(),pwBox.getPassword()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    class HintTextField extends JTextField {
        private String hint;

        public HintTextField(String hint) {
            this.hint = hint;
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

    class HintPasswordField extends JPasswordField {
        private String hint;

        public HintPasswordField(String hint) {
            this.hint = hint;
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
}
