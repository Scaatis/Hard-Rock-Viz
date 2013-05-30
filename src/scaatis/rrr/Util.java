package scaatis.rrr;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Util {
    public static Point toPoint(Point2D p) {
        return new Point((int) p.getX(), (int) p.getY());
    }
}
