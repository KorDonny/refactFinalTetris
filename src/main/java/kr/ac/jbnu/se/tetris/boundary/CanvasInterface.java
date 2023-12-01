package kr.ac.jbnu.se.tetris.boundary;

import java.io.IOException;

public interface CanvasInterface {
    /** 각 UI에 필요한 배경 경로 설정 요망 */
    String gifImagePath = null;
    String[] animatedImagePath = null;
    /** 이미지 경로 지정 요망. 함수 내부에서 받아서 사용함.*/
    void setImage() throws IOException;
}
