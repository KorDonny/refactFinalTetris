package kr.ac.jbnu.se.tetris;
import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Control.Calculator;
import kr.ac.jbnu.se.tetris.Control.FirebaseTool;
import kr.ac.jbnu.se.tetris.Control.Handler.*;

import java.awt.*;
import javax.swing.*;

/**
 * 블록은 7개의 모양을 가진 블록과 구현을 위해 만들어진 빈 공간(NoShape)이 있음
 * (Tetrominoes 클래스에 종류의 이름이 있음)
 * 블록은 4개의 칸으로 구성
 */

public class Tetris extends JFrame {//테트리스 클래스
	private final int UIBUTTONCOLM = 4;
	public static int windowWidth = 600; //1인 또는 개인 모드 -> 400에 setPreffered width/2 # 2인 또는 ai -> 600 width/3
	public static int windowHeight = 400;
	public static int gameUIBlockWidth = 200;
	public static int gameUIBlockHeight = 400;
	protected TetrisCanvas boundary, boundary2;
	protected FirebaseTool firebaseTool;
	public Tetris() {
		firebaseTool=FirebaseTool.getInstance();
		if(!firebaseTool.logIn("hiyd125@jbnu.ac.kr","rltn12"))firebaseTool.signUp("hiyd125@jbnu.ac.kr","rltn12");

		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//startGame(currentMode);
	}
	public TetrisCanvas getP1(){ return boundary; }
	public TetrisCanvas getP2(){ return boundary2 != null ? boundary2 : null; }
	public void updateP1(TetrisCanvas boundary){ this.boundary=boundary; }
	public void updateP2(TetrisCanvas boundary){ this.boundary2=boundary; }
	public static void main(String[] args) {//메인실행코드
		SwingUtilities.invokeLater(() -> {
			Tetris game = new Tetris();
			game.setLocationRelativeTo(null);
			game.setVisible(true);
		});
	}
	public void inputGameUI(JPanel target){
		target.setPreferredSize(new Dimension(gameUIBlockWidth,gameUIBlockHeight));
		//gameUIPanel.add(target);
		this.revalidate();
		this.repaint();
		requestFocusInWindow();
	}
}