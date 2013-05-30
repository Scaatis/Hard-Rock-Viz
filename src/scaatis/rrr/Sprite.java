package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Sprite implements GraphicsResource {
    private BufferedImage image;
    private Point         origin;

    public Sprite(BufferedImage image, Point origin) {
        this.image = image;
        this.origin = origin;
    }

    public Dimension getDimensions() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public Rectangle getBounds() {
        return new Rectangle(-origin.x, -origin.y, image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }

    protected void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getOrigin() {
        return new Point(origin);
    }

    @Override
    public void draw(Graphics g, Point2D location) {
        Graphics2D graphics2d = (Graphics2D) g.create();
        graphics2d.drawImage(image,
                (int) location.getX() - origin.x, (int) location.getY() - origin.y,
                null);
        graphics2d.dispose();
    }
}
