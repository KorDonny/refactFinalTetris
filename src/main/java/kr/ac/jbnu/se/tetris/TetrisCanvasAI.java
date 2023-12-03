package kr.ac.jbnu.se.tetris;

import java.io.IOException;

public class TetrisCanvasAI extends TetrisCanvas {

	private AIControl aiControl;
	private final WorkFlow aiWorks = new WorkFlow(this);
	public TetrisCanvasAI() throws IOException {
		super();
		aiControl = new AIControl(this);
	}
	@Override
	public void start(){
		clearBoard();
		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		newPiece();
		actionTrigger();
	}
	@Override
	public void pause(){
		if (!isStarted)
			return;
		isPaused = !isPaused;
		repaint();
	}
	@Override
	protected void newPiece(){
		curPiece.setRandomShape();
		// 블록이 움직이지 못할 때(게임 종료)
		if (!tryMove(curPiece, curPiece.getCurX(), curPiece.getCurY())) {//블록 과다로 게임오버시.
			curPiece = new Block(Tetrominoes.NO_SHAPE); // 떨어지는 블록 없앰
			TimerManager.removeTask(aiWorks);
			isStarted = false;
		}
		if (isStarted){
			doControlLogic();
		}
	}

	public void doControlLogic() {
		Block tmpBlock = new Block(Tetrominoes.NO_SHAPE);
		tmpBlock.copyEntity(getCurPiece());
		int[] goodPosition = new AIControl(this).findGoodPosition(tmpBlock);

		for (int i = goodPosition[2]; i > 0; i--) {
			curPiece.rotateRight(getBoard());
		}
		int num = curPiece.getCurX() - goodPosition[0];
		while (num != 0){
			if (num > 0) {
				tryMove(curPiece, curPiece.getCurX() - 1, curPiece.getCurY());
				num--;
			} else if (num < 0) {
				tryMove(curPiece, curPiece.getCurX() + 1, curPiece.getCurY());
				num++;
			}
		}
	}
	@Override
	public boolean tryMove(Block newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NO_SHAPE)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		newPiece.setPosition(newX,newY);
		return true;
	}
	@Override
	protected void pieceDropped(){
		// 현재 위치에 블록 배치
		for (int i = 0; i < 4; ++i) {
			int x = curPiece.getCurX() + curPiece.x(i);
			int y = curPiece.getCurY() - curPiece.y(i);
			getBoard()[(y * TETRIS_CANVAS_W) + x] = curPiece.getShape();
		}
		// 완성된 라인 확인
		removeFullLines();
		// 완성된 줄이 있다면 작동 안함
		if (!isFallingFinished)
			newPiece();
	}
	@Override
	protected void removeFullLines() {
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
				for (int k = i; k < TETRIS_CANVAS_H - 1; ++k) {
					for (int j = 0; j < TETRIS_CANVAS_W; ++j)
						getBoard()[(k * TETRIS_CANVAS_W) + j] = shapeAt(j, k + 1);
				}
			}
		}
		// 완성된 라인이 있다면 UI업데이트
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			isFallingFinished = true;
			curPiece = new Block(Tetrominoes.NO_SHAPE);
			repaint();
		}
	}
}