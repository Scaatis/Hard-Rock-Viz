package scaatis.rrr;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static HashMap<String, BufferedImage> loadedImages = new HashMap<>();
    public static Sprite curveRightUp = new Sprite(get("res/curve.png"), new Point(99, 11));
    public static Sprite curveLeftDown = new Sprite(get("res/curve2.png"), new Point(151, 11));
    public static Sprite curveDownRight = new Sprite(get("res/curve3.png"), new Point(0, 37));
    public static Sprite curveLeftUp = new Sprite(get("res/curve4.png"), new Point(155, 11));
    public static Sprite straightHor = new Sprite(get("res/straight.png"), new Point(155, 11));
    public static Sprite straightVer = new Sprite(get("res/straight2.png"), new Point(96, 11));
    public static Sprite finishHor = new Sprite(get("res/finish.png"), new Point(155, 11));
    public static Sprite finishVer = new Sprite(get("res/finish2.png"), new Point(32, 11));
    public static FontSpriteSheet defaultFont = new FontSpriteSheet(get("res/font.png"), new Point(0, 8), 16, 16);
    
    private static BufferedImage load(String path) throws IOException {
        BufferedImage img = loadedImages.get(path);
        if(img != null) {
            return img;
        }
        img = ImageIO.read(new File(path));
        loadedImages.put(path, img);
        return img;
    }
    
    public static BufferedImage get(String path) {
        try {
            return load(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
