package kr.ac.jbnu.se.tetris;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class TetrisCanvasBuff extends CanvasBuff implements CanvasBuffInterface{
    /** 각 캔버스에 맞게 경로 수정 필수 */
    private static final String TETRIS_IMG_FILE = "tetrisCanvasSprite.png";
    private static final int BUFF_SIZE = 8;
    /** 이미지 인덱스 */
    private int idx = 0;
    private final BufferedImage[] sprites;
    public TetrisCanvasBuff() throws IOException {
        sprites = CanvasBuffInterface.initImageArray(TETRIS_IMG_FILE, BUFF_SIZE);
    }
    @Override
    public BufferedImage getSprite(){
        if(idx == BUFF_SIZE) idx = 0;
        return this.sprites[idx++];
    }
}
