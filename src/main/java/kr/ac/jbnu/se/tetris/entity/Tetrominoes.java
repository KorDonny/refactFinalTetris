package kr.ac.jbnu.se.tetris.entity;

import java.awt.*;

public enum Tetrominoes {

	/** 블럭 형상 정보 인스턴스. 인덱싱 변수 idx를 포함하고 있음 */
	NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape, GrayLineShape, Shadow;
	/** 인스턴스 컴파일시 자동으로 enum들에게 배정됨 */
	public Color getColor(){
		Color color;
		switch(this.ordinal()) {
			case 0:
				color = new Color(0, 0, 0);
				break;
			case 1:
				color = new Color(204, 102, 102);
				break;
			case 2:
				color = new Color(102, 204, 102);
				break;
			case 3:
				color = new Color(102, 102, 204);
				break;
			case 4:
				color = new Color(204, 204, 102);
				break;
			case 5:
				color = new Color(204, 102, 204);
				break;
			case 6:
				color = new Color(102, 204, 204);
				break;
			case 7:
				color = new Color(218, 170, 0);
				break;
			case 8:
				color = new Color(90, 90, 90);
				break;
			case 9:
				color = new Color(57, 57, 57);
				break;
			default:
				color = null;
		}
		return color;
	}
	public int[][] getShapeArr() {
		int[][] shape;
		switch(this.ordinal()){
			case 0:
				shape = new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } };
				break;
			case 1:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }.clone();
				break;
			case 2:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }.clone();
				break;
			case 3:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }.clone();
				break;
			case 4:
				shape = new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }.clone();
				break;
			case 5:
				shape = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }.clone();
				break;
			case 6:
				shape = new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }.clone();
				break;
			case 7:
				shape = new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }.clone();
				break;
			case 8:
				shape = new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }.clone();
				break;
			default:
				return new int[0][];
		}
		return shape;
	}
}
