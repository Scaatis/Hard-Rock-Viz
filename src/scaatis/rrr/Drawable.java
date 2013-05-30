package scaatis.rrr;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Drawable {
    private Point2D          location;
    private GraphicsResource resource;
    private AffineTransform  transform;
    private boolean          destroyed;

    public Drawable(Point2D location, GraphicsResource resource) {
        this(location, resource, null);
    }

    public Drawable(Point2D location, GraphicsResource resource, AffineTransform transform) {
        this.location = location;
        this.resource = resource;
        this.transform = transform;
        destroyed = false;
    }

    public GraphicsResource getResource() {
        return resource;
    }

    public void setResource(GraphicsResource resource) {
        this.resource = resource;
    }

    public void draw(Graphics g) {
        Point2D drawLoc = location;
        if (transform != null) {
            drawLoc = transform.transform(drawLoc, null);
        }
        resource.draw(g, drawLoc);
    }

    public Point2D getLocation() {
        return location;
    }

    public Rectangle getBounds() {
        Rectangle res = new Rectangle(resource.getBounds());
        res.translate((int) location.getX(), (int) location.getY());
        return res;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public int getZ() {
        return (int) location.getY();
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
