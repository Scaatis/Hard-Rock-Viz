package scaatis.rrr;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Missile extends MovingObject {
    public static RotateSpriteSheet missileSprite = new RotateSpriteSheet(ImageLoader.get("res/missile2.png"), new Point(7, 24), 5, 1, 2);
    
    public Missile(int id, Point2D location, Vector2D speed, AffineTransform transform) {
        super(id, missileSprite.new DirectionSpriteResource(speed.getDirection()), location, speed, transform);
    }

}
