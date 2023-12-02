package kr.ac.jbnu.se.tetris.entity;

import kr.ac.jbnu.se.tetris.FrameMain;
import kr.ac.jbnu.se.tetris.boundary.UICanvas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/** each behavior need to have static int idx(first = 0) with static sprites,
 * and that type should be Array of BufferedImage[] */
public interface CanvasBuffInterface {
    /** should initiate fileName same as "sprite.png" */
    static BufferedImage[] initImageArray(String fileName, int size) throws IOException {
        BufferedImage[] sprites = new BufferedImage[size];
        BufferedImage img = ImageIO.read(new File(FrameMain.IMAGE_DIR_PATH+fileName));
        for(int i = 0; i < sprites.length; i++){
            sprites[i] = img.getSubimage(i*UICanvas.BOARD_SIZE_W,0,UICanvas.BOARD_SIZE_W,UICanvas.BOARD_SIZE_H);
        }
        return sprites;
    }
    /** return sprites to index static idx++ to get an increment after calls */
    BufferedImage getSprite();
}
