package kr.ac.jbnu.se.tetris.Boundary;

import kr.ac.jbnu.se.tetris.Control.AIControl;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import java.io.IOException;
import java.util.TimerTask;

public class TetrisCanvasAI extends TetrisCanvas {

	private AIControl aiControl;
	public TetrisCanvasAI() throws IOException {
		super();
		BackPanel.addTask("Canvas AI Logic", new TimerTask() {
			@Override
			public void run() {
				try {
					actionTrigger();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}, 100);// 이벤트간 딜레이 400
		aiControl = new AIControl(this);
	}

	@Override
	protected void newPiece() throws InterruptedException {
		super.newPiece();
		if (isStarted){
			doControlLogic();
		}
	}

	public void doControlLogic() {
		Entity tmp_Entity = new Entity(Tetrominoes.NoShape);
		tmp_Entity.copyEntity(getCurPiece());
		int[] goodPosition = aiControl.findGoodPosition(tmp_Entity);

//		for (int i = 0; i < 3; i++) {
//			System.out.println("goodPosition[" + i + "] : " + goodPosition[i]);
//		}
//		System.out.println("----------------------------");

		for (int i = goodPosition[2]; i > 0; i--) {
			curPiece.rotateRightA();
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

//        canvas.dropDown();
	}
	public boolean tryMoveAI(Entity newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= TETRIS_CANVAS_W || y < 0 || y >= TETRIS_CANVAS_H)//테트리스 컨트롤 도형의 x,y에 의해 통제
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)//테트리스 핸들링 도형이 블랭크가 아닐시 게임은 진행중. 불리언에 의해 제어
				return false;
		}
		newPiece.setPosition(newX,newY);
		/*TestMonitor.setCurDxP1(curPiece.getCurX());
		TestMonitor.setCurDyP1(curPiece.getCurY());*/
		return true;
	}
}