package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.FrameMain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class SprintModeHandler extends NormalModeHandler implements GameModeHandler {
    private final JLabel sprintModeStatusbar;
    private JLabel gameClearStatusLabel;
    private int targetLineCount; // 목표 라인 개수
    private boolean gameClearAchieved; // Game Clear 상태 여부
    private final Random random;
    private NormalModeHandler normal;
    public SprintModeHandler() throws IOException {
        super();
        this.sprintModeStatusbar = new JLabel();
        this.gameClearAchieved = false;
        this.random = new Random();
        initGameClearStatusLabel();
        updateTargetLineCount();

        // 게임 클리어 확인용 타이머 초기화 (1초마다 체크)
        Timer gameClearCheckTimer; // 게임 클리어 확인용 타이머
        gameClearCheckTimer = new Timer(1000, e -> {
            try {
                checkGameClear();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gameClearCheckTimer.setInitialDelay(1000); // 최초 딜레이 설정
        gameClearCheckTimer.start();
    }
    @Override
    public void startGame() throws IOException {
        super.startGame();
        updateTargetLineCount();
        updateStatusbarText();
        gameClearAchieved = false;
        getCanvas().setLayout(new OverlayLayout(getCanvas()));
        getCanvas().add(gameClearStatusLabel);
        gameClearStatusLabel.setVisible(false);
        checkGameClear();
        getCanvas().requestFocusInWindow();
        sprintModeStatusbar.setVisible(true);
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(getCanvas());
    }
    @Override
    public TetrisCanvas getCanvas() {return super.getCanvas();}
    public void checkGameClear() throws IOException {
        if (getCanvas().getNumLinesRemoved() >= targetLineCount && !gameClearAchieved) {
            gameClearAchieved = true;
            gameClearStatusLabel.setText("Game Clear!");
            gameClearStatusLabel.setVisible(true);
            getCanvas().getTimer().stop();
            getCanvas().setEnabled(false);
            FrameMain.getInstance().getBackPanel().repaint();
        }
    }
    private void updateTargetLineCount() {
        // 랜덤으로 20 또는 40 선택
        targetLineCount = random.nextBoolean() ? 20 : 40;
    }
    private void updateStatusbarText() {
        sprintModeStatusbar.setText("Remove " + targetLineCount + " lines!");
    }
    private void initGameClearStatusLabel() {
        this.gameClearStatusLabel = new JLabel("Game Clear!");
        this.gameClearStatusLabel.setForeground(Color.YELLOW);
        this.gameClearStatusLabel.setBackground(Color.BLACK);
        this.gameClearStatusLabel.setOpaque(true);
        this.gameClearStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        this.gameClearStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 50)); // 폰트 및 크기 설정
    }
}
