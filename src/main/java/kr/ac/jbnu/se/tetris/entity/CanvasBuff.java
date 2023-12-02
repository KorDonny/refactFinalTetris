package kr.ac.jbnu.se.tetris.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class CanvasBuff implements CanvasBuffInterface{
    /** 각 캔버스에 맞게 경로 수정 필수 */
    private static final String UI_IMG_FILE = "uiCanvasSprite.png";
    private static final int buffSize = 13;
    /** 이미지 인덱스 */
    private static int idx = 0;
    private static BufferedImage[] sprites;
    static {
        try {
            sprites = CanvasBuffInterface.initImageArray(UI_IMG_FILE, buffSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CanvasBuff(){
    }
    /** 객체화 이후 접근 요망, 자동으로 다음 장을 가리킴, 인덱스 끝단 진입시 0으로 회귀 */
    @Override
    public BufferedImage getSprite() {
        if(idx == buffSize) idx = 0;
        return sprites[idx++];
    }
}
