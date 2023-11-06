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
	//#
	public enum GameMode {
		NORMAL_MODE, ITEM_MODE, SURVIVAL_MODE, SPRINT_MODE, AI_MODE, PVP_MODE
	}
	private GameModeHandler modeHandler;
	//#

	//#GUI 클래스를 개별적으로 만들어야 할 것이라 사료됨. TetrisClass에서 우선 시연시도
	private final JPanel modeSelectionPanel;
	private final JPanel gameUIPanel;
	private final JButton startButton;
	private final JButton normalModeButton;
	private final JButton itemModeButton;
	private final JButton survivalModeButton;
	private final JButton sprintModeButton;
	private final JButton aiModeButton;
	private final JButton pvpModeButton;
	//#

	private final int UIBUTTONCOLM = 4;
	public static int windowWidth = 600; //1인 또는 개인 모드 -> 400에 setPreffered width/2 # 2인 또는 ai -> 600 width/3
	public static int windowHeight = 400;
	public static int gameUIBlockWidth = 200;
	public static int gameUIBlockHeight = 400;
//	public static TestMonitor display;
	protected TetrisCanvas boundary, boundary2;
	protected FirebaseTool firebaseTool;
	public Tetris() {
		firebaseTool=FirebaseTool.getInstance();
		if(!firebaseTool.logIn("hiyd125@jbnu.ac.kr","rltn12"))firebaseTool.signUp("hiyd125@jbnu.ac.kr","rltn12");
		//#
		modeSelectionPanel = new JPanel();
		gameUIPanel = new JPanel();
		gameUIPanel.setLayout(new BoxLayout(gameUIPanel,BoxLayout.X_AXIS));
//		display = new TestMonitor();
//		add(display,BorderLayout.CENTER);
//		display.setPreferredSize(new Dimension(windowWidth/2,windowHeight));
//		display.setVisible(false);

		startButton = new JButton("게임 시작");
		normalModeButton = new JButton("일반 모드");
		itemModeButton = new JButton("아이템 모드");
		survivalModeButton = new JButton("생존 모드");
		sprintModeButton = new JButton("스프린트 모드");
		aiModeButton = new JButton("AI 모드");
		pvpModeButton = new JButton("PVP 모드");

		add(startButton, BorderLayout.NORTH);
		add(modeSelectionPanel, BorderLayout.CENTER);
		modeSelectionPanel.add(normalModeButton);
		modeSelectionPanel.add(itemModeButton);
		modeSelectionPanel.add(survivalModeButton);
		modeSelectionPanel.add(sprintModeButton);
		modeSelectionPanel.add(aiModeButton);
		modeSelectionPanel.add(pvpModeButton);
		modeSelectionPanel.setVisible(false);

			startButton.addActionListener(e -> showModeSelection());
		normalModeButton.addActionListener(e -> startGame(GameMode.NORMAL_MODE));
		itemModeButton.addActionListener(e -> startGame(GameMode.ITEM_MODE));
		survivalModeButton.addActionListener(e -> startGame(GameMode.SURVIVAL_MODE));
		sprintModeButton.addActionListener(e -> startGame(GameMode.SPRINT_MODE));
		aiModeButton.addActionListener(e -> startGame(GameMode.AI_MODE));
		pvpModeButton.addActionListener(e -> startGame(GameMode.PVP_MODE));
		//#

		setSize(windowWidth, windowHeight);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//startGame(currentMode);
	}
	private void showModeSelection() {
		startButton.setVisible(false);
		modeSelectionPanel.setLayout(new GridLayout((int)Math.ceil(GameMode.values().length / 4),UIBUTTONCOLM));
		modeSelectionPanel.setVisible(true);
	}
	private void startGame(GameMode mode) {
		modeSelectionPanel.setVisible(false);
		add(gameUIPanel);
//		display.setVisible(true);
		switch (mode) {
			case NORMAL_MODE:
				modeHandler = new NormalModeHandler(this);
				modeHandler.startGame();
				break;
			case ITEM_MODE:
				modeHandler = new ItemModeHandler(this);
				modeHandler.startGame();
				break;
			case SURVIVAL_MODE:
				modeHandler = new SurvivalModeHandler(this);
				modeHandler.startGame();
				break;
			case SPRINT_MODE:
				modeHandler = new SprintModeHandler(this);
				modeHandler.startGame();
				break;
			case AI_MODE:
				modeHandler = new AIModeHandler(this);
				modeHandler.startGame();
				break;
			case PVP_MODE:
				// Local 대전
				modeHandler = new LocalModeHandler(this);
				modeHandler.startGame();
				break;
		}
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
		gameUIPanel.add(target);
		this.revalidate();
		this.repaint();
		requestFocusInWindow();
	}
	public static boolean containsComponent(Container container, Component component) {
		Component[] components = container.getComponents();
		for (Component c : components) {
			if (c == component) {
				return true;
			}
		}
		return false;
	}
}