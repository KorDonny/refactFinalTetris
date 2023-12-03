package kr.ac.jbnu.se.tetris;

import java.awt.*;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
/**
 *  게임 화면을 구성하는 칸 <br/>
 *    해당 칸에 어떤 블록이 들어있는지 저장하는 변수 <br/>
 *    세로축을 y, 가로축을 x로 생각했을 때, <br/>
 *    board의 인덱스값은 십의 자릿수가 y, 일의 자릿수가 x를 나타냄 <br/>
 *    ex) (3,1)의 위치를 인덱싱하려면 -> board[13]
 */
public class TetrisCanvas extends UICanvas{//인터페이스 = 액션리스너 //상속클래스 = Jpanel
	/** 화면의 가로칸 수 */
	public static final int TETRIS_CANVAS_W = 10;
	/** 화면의 세로칸 수 */
	public static final int TETRIS_CANVAS_H = 22;
	private Tetrominoes[] board;
	private UICanvas uiCanvas;
	private PreviewBlock preview = null;
	private final int previewNum;
	private Block[] previewList;
	private Sound sound;
	/** 스페이스바 처리 시간 간격 조절 위한 인수 */
	private long droppedTime;
	/** 기본 프레임 딜레이 400 */ //딜레이 구성 변경 로직 구현하여 난이도 조절 가능할 것이라고 추측됨.
	/** ture : 블록이 바닥에 닿은 상태 <br/>
	 * false : 블록이 낙하중인 상태 */
	boolean isFallingFinished = false;
	/** 게임 시작 여부 */ //KeyControl 클래스가 static이어서, 한 보드가 false되버리면 키 감지 로직 멈추는 현상 있음, 확인 요망
	boolean isStarted = false;
	/** 게임 일시정지 여부 */
	boolean isPaused = false;
	/** 지워진 라인 갯수 */
	int numLinesRemoved = 0;
	/** 현재 떨어지는 블록 */
	Block curPiece;
	Block shadowPiece;
    TetrisCanvasBuff tBuff;
	private final WorkFlow tetrisWork = new WorkFlow(this);
	public TetrisCanvas() throws IOException {
		curPiece = new Block(Tetrominoes.NO_SHAPE); // 현재 블록
		shadowPiece = new Block(Tetrominoes.NO_SHAPE);
		board = new Tetrominoes[TETRIS_CANVAS_W * TETRIS_CANVAS_H]; // 1차원 배열의 칸 생성
		sound = new Sound();
        tBuff = new TetrisCanvasBuff();
		previewNum = 5;
		previewList = new Block[previewNum];
		for (int i = 0; i < previewNum; i++) previewList[i] = new Block(Tetrominoes.NO_SHAPE);
	}

	/** 칸의 가로 길이 */
	int squareWidth() { return (int) getSize().getWidth() / TETRIS_CANVAS_W; }

	/** 칸의 세로 길이 */
	int squareHeight() { return (int) getSize().getHeight() / TETRIS_CANVAS_H; }

	/** (x,y)에 블록 종류 */
	public Tetrominoes shapeAt(int x, int y) { return board[(y * TETRIS_CANVAS_W) + x]; }
	public void start() throws InterruptedException, ExecutionException {
		clearBoard();
		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		for (int i = 0; i < previewNum; i++) previewList[i].setRandomShape();
		newPiece();
		sound.startBgm();
		droppedTime = 0;
		actionTrigger();
	}
	protected void actionTrigger(){
		TimerManager.addTask(tetrisWork,new TimerTask() {
			@Override
			public void run() {
				try {
					if (isFallingFinished) {
						isFallingFinished = false;
						newPiece();
					} else {
						if(isPaused()){
							TimerManager.stopTask(tetrisWork);
							return;
						}
						oneLineDown();
					}
				} catch (InterruptedException | ExecutionException e) {
					/* Clean up whatever needs to be handled before interrupting  */
					Thread.currentThread().interrupt();
					throw new RuntimeException(e);
				}
			}
		},400)
		;
	}

	/**
	 * 일시정지 메소드
	 * 추후 pause이펙트를 공통으로 걸려면, timer에 대한 처리가 필요할 것으로 보임.
	 * */
	public void pause() {
		if (!isStarted)
			return;
		isPaused = !isPaused;
		if (isPaused) {
			sound.stopBgm();
		} else {
			sound.startBgm();
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - TETRIS_CANVAS_H * squareHeight();

		// 쌓여있는 블록 색칠
		for (int i = 0; i < TETRIS_CANVAS_H; ++i) {
			for (int j = 0; j < TETRIS_CANVAS_W; ++j) {
				Tetrominoes shape = shapeAt(j, TETRIS_CANVAS_H - i - 1);
				if (shape != Tetrominoes.NO_SHAPE)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		// 떨어지는 블록 관련 색칠
		if (curPiece.getShape() != Tetrominoes.NO_SHAPE) {
			drawMovingPiece(g, boardTop);
		}
	}

	private void drawMovingPiece(Graphics g, int boardTop) {
		// 그림자 생성
		shadowPiece.copyEntity(curPiece);
		int newY = shadowPiece.getCurY();
		while (newY > 0) {
			if (!tryMoveA(shadowPiece, shadowPiece.getCurX(), newY - 1))
				break;
			--newY;
		}

		// 블록 그림자 색칠
		for (int i = 0; i < 4; ++i) {
			int x = shadowPiece.getCurX() + shadowPiece.x(i);
			int y = shadowPiece.getCurY() - shadowPiece.y(i);
			drawSquare(g, x * squareWidth(), boardTop + (TETRIS_CANVAS_H - y - 1) * squareHeight(),
					Tetrominoes.SHADOW);
		}

		// 떨어지는 블록 색칠
		for (int i = 0; i < 4; ++i) {
			int x = curPiece.getCurX() + curPiece.x(i);
			int y = curPiece.getCurY() - curPiece.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (TETRIS_CANVAS_H - y - 1) * squareHeight(),
					curPiece.getShape());
		}
	}

	public boolean dropDown() throws InterruptedException, ExecutionException {
		if(System.currentTimeMillis() - droppedTime < 200){ return false; }
		int newY = curPiece.getCurY();
		while (newY > 0) {
			if (!tryMove(curPiece, curPiece.getCurX(), newY - 1))
				break;
			--newY;
		}
		droppedTime = System.currentTimeMillis();
		pieceDropped();
		return true;
	}

	/** 블록이 한줄 아래로 내려가는 메소드*/
	protected void oneLineDown() throws InterruptedException, ExecutionException {
		if (!tryMove(curPiece, curPiece.getCurX(), curPiece.getCurY() - 1))
			pieceDropped(); //떨어지면 수행되는 메소드, 드롭다운과 동일
	}

	/** 모든 칸을 빈 공간(NoShape블록)으로 초기화 */
	public void clearBoard() {
		for (int i = 0; i < TETRIS_CANVAS_H * TETRIS_CANVAS_W; ++i)
			board[i] = Tetrominoes.NO_SHAPE;
	}

	/** 현재 위치에 블록을 남기는 메소드 */
	protected void pieceDropped() throws InterruptedException, ExecutionException {
		if(isStarted())sound.playDropSound();
		// 현재 위치에 블록 배치
		for (int i = 0; i < 4; ++i) {
			int x = curPiece.getCurX() + curPiece.x(i);
			int y = curPiece.getCurY() - curPiece.y(i);
			board[(y * TETRIS_CANVAS_W) + x] = curPiece.getShape();
		}
		// 완성된 라인 확인
		checkFullLines();
		// 완성된 줄이 있다면 작동 안함
		if (!isFallingFinished && isStarted)
			newPiece();
	}

	/** 새 블록 생성 */
	protected void newPiece() throws InterruptedException, ExecutionException {

		curPiece.copyEntity(previewList[0]);
		for(int i = 0; i < previewNum - 1; i++)previewList[i].copyEntity(previewList[i + 1]);
		previewList[previewNum - 1].setRandomShape();

		preview.updatePreviewList(previewList);
		preview.setReadyFlagTrue();
		uiCanvas.repaint();

		// 블록이 움직이지 못할 때(게임 종료)
		if (!tryMove(curPiece, curPiece.getCurX(), curPiece.getCurY())) {//블록 과다로 게임오버시.
			curPiece = new Block(Tetrominoes.NO_SHAPE); // 떨어지는 블록 없앰
			TimerManager.removeTask(tetrisWork);
			sound.stopBgm();
			isStarted = false;
			preview.setReadyFlagFalse();
			CanvasScoreInterface.updateDBScore(this);
		}
	}
	@Override
	public void paintComponent(Graphics g){ g.drawImage(tBuff.getSprite(),0,0,null); }
	/** 블록 움직일 수 있는지 여부 반환<br/>
	 *  만약 움직일 수 있다면 움직이는 메서드 */
	public boolean tryMove(Block newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NO_SHAPE)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		curPiece = newPiece;
		curPiece.setPosition(newX,newY);
		repaint();
		return true;
	}
	public boolean tryMoveA(Block newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NO_SHAPE)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		newPiece.setPosition(newX,newY);
		repaint();
		return true;
	}
	/** 완성된 줄 제거 */
	protected void checkFullLines() {
		int numFullLines = 0; // 완성된 줄의 수

		// 위에서부터 내려오면서 찾기
		for (int i = TETRIS_CANVAS_H - 1; i >= 0; --i) {
			boolean lineIsFull = true;
			// i번째 행에 비어있는 칸이 있으면 break 작동
			for (int j = 0; j < TETRIS_CANVAS_W; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NO_SHAPE) {
					lineIsFull = false;
					break;
				}
			}
			// i번째 행에 빈칸이 없다면 윗줄들을 아래로 내림(채워진 줄 삭제)
			if (lineIsFull) {
				++numFullLines;
				removeFulllines(i);
			}
		}

		// 완성된 라인이 있다면 UI업데이트
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			isFallingFinished = true;
			curPiece = new Block(Tetrominoes.NO_SHAPE);
			sound.playRemoveSound();
			repaint();
			CanvasScoreInterface.updateScore(this);
		}
	}

	protected void removeFulllines(int num) {
		for (int i = num; i < TETRIS_CANVAS_H - 1; ++i) {
			for (int j = 0; j < TETRIS_CANVAS_W; ++j)
				board[(i * TETRIS_CANVAS_W) + j] = shapeAt(j, i + 1);
		}
	}

	/** 칸을 블록의 종류에 맞게 색칠하는 메소드 */
	protected void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color tmpcolor = shape.getColor();
		g.setColor(tmpcolor);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		g.setColor(tmpcolor.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);
		g.setColor(tmpcolor.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}
	public Block getCurPiece(){ return curPiece; }
	public boolean isPaused(){ return isPaused; }
	public boolean isStarted(){ return isStarted; }
	public int getNumLinesRemoved() { return numLinesRemoved; }
	//재시작 화면 기능 미구현할 시 삭제 요망.
	public void restart() throws InterruptedException, ExecutionException {
		clearBoard();
		numLinesRemoved=0;
		newPiece();
	}
	public Tetrominoes[] getBoard(){ return board; }
	public void setUICanvas(UICanvas uiCanvas) {
		this.uiCanvas = uiCanvas;
		preview = uiCanvas.getPreview(previewNum);
	}
}