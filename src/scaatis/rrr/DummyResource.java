package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class DummyResource implements GraphicsResource {

    @Override
    public void draw(Graphics g, Point2D p) {
        
    }

    @Override
    public Dimension getDimensions() {
        return new Dimension();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle();
    }

}
