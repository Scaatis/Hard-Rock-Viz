package scaatis.rrr;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.json.JSONObject;

public class Mine extends MovingObject {

    public static Sprite mineSprite = new Sprite(ImageLoader.get("res/mine.png"), new Point(7, 6));

    public Mine(JSONObject mineObject, AffineTransform transform) {
        super(mineObject.getInt("id"), mineSprite,
                new Point2D.Double(mineObject.getDouble("locationX"), mineObject.getDouble("locationY")),
                new Vector2D.Cartesian(0, 0), transform);
    }

}
