package kr.ac.jbnu.se.tetris.boundary;

import java.io.IOException;
/**
 * 각 UI에 필요한 배경 경로 클래스 변수로 설정 요망
 * 1. 인덱스를 통한 GIF구성을 원할시 idx 변수로 순회 -> 이미지 경로 배열에 대한 이터레이터도 고려해보시길
 * 2. 현재 백그라운드 이미지 ImageIcon이 적합
 * 3. String[] 배열을 통한 경로 모음 구성
 * */
public interface CanvasInterface {
    /** 이미지 경로 함수 내부에서 받아서 사용함.*/
    void setImage() throws IOException;
}
