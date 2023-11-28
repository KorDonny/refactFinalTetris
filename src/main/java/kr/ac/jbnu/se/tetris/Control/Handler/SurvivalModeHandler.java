package kr.ac.jbnu.se.tetris.Control.Handler;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.KeyControl;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class SurvivalModeHandler extends NormalModeHandler implements GameModeHandler {
    private final Timer lineAdditionTimer;
    private final Random random;
    public SurvivalModeHandler() throws IOException {
        super();
        this.random = new Random();
        this.lineAdditionTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogic();
            }
        }); // 2초마다 한 줄 추가
    }
    @Override
    public void startGame() throws IOException {
//        tetris.remove(board); // 이전 게임 보드 제거
//        tetris.add(board, BorderLayout.SOUTH); // 새 게임 보드 추가
//        getBoundary();
//        board.start();
//        lineAdditionTimer.start();
//        tetris.revalidate();
//        tetris.repaint();
//        board.requestFocusInWindow();
        super.startGame();
        lineAdditionTimer.start();
    }
    @Override
    public TetrisCanvas getCanvas() {
        return super.getCanvas();
    }
    @Override
    public void connectCanvas() {
        KeyControl.updatePlayer(getCanvas());
    }
    public void doLogic() {
        if (!getCanvas().isStarted()) return;
        int holePosition = random.nextInt(TetrisCanvas.BoardWidth); // 뚫린 위치
        for (int x = 0; x < TetrisCanvas.BoardWidth; x++) {
            for (int y = TetrisCanvas.BoardHeight - 1; y > 0; y--) {
                Tetrominoes shape = getCanvas().shapeAt(x, y - 1);
                getCanvas().board[y * TetrisCanvas.BoardWidth + x] = shape;
            }
        }
        // 랜덤한 위치에 한 칸 뚫린 라인 생성
        for (int x = 0; x < TetrisCanvas.BoardWidth; x++) {
            if (x != holePosition) {
                getCanvas().board[x] = Tetrominoes.GrayLineShape;
            } else {
                getCanvas().board[x] = Tetrominoes.NoShape;
            }
        }
    }
}