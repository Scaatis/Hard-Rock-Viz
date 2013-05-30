package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public interface GraphicsResource {
    public void draw(Graphics g, Point2D p);
    public Dimension getDimensions();
    public Rectangle getBounds();
}
