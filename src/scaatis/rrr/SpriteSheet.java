package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage image;
    private Point         origin;
    private int           colums;
    private int           rows;

    public SpriteSheet(BufferedImage image, Point origin, int colums, int rows) {
        this.image = image;
        this.origin = origin;
        this.colums = colums;
        this.rows = rows;
    }

    public int getLength() {
        return colums * rows;
    }

    private void checkFrame(int frame) {
        if (frame >= getLength() || frame < 0) {
            throw new ArrayIndexOutOfBoundsException(frame);
        }
    }

    public Dimension getSingleDimensions() {
        return new Dimension(image.getWidth() / colums, image.getHeight() / rows);
    }

    public Dimension getDimensions() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public Rectangle getFrameBounds(int frame) {
        checkFrame(frame);
        Dimension singleDimension = getSingleDimensions();
        Point center = new Point(frame % colums * singleDimension.width,
                frame / colums * singleDimension.height);
        return new Rectangle(center, singleDimension);
    }

    public Rectangle getSingleBounds() {
        Point center = new Point(-origin.x, -origin.y);
        return new Rectangle(center, getSingleDimensions());
    }

    public void drawFrame(Graphics g, int frame, Point2D location) {
        Rectangle bounds = getFrameBounds(frame);
        Graphics2D graphics2d = (Graphics2D) g.create();
        graphics2d.translate(location.getX(), location.getY());
        graphics2d.translate(-origin.x, -origin.y);
        graphics2d.drawImage(image, 0, 0, bounds.width, bounds.height,
                bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, null);
        graphics2d.dispose();
    }

    public void drawFrameXMirror(Graphics g, int frame, Point2D location) {
        Rectangle bounds = getFrameBounds(frame);
        Graphics2D graphics2d = (Graphics2D) g.create();
        graphics2d.translate(location.getX(), location.getY());
        graphics2d.translate(-origin.x, -origin.y);
        graphics2d.drawImage(image, 0, 0, bounds.width, bounds.height,
                bounds.x + bounds.width, bounds.y, bounds.x,  bounds.y + bounds.height, null);
        graphics2d.dispose();
    }

    public Point getOrigin() {
        return new Point(origin);
    }

    public class SpriteSheetResource implements GraphicsResource {
        private int frame;

        public SpriteSheetResource(int frame) {
            checkFrame(frame);
            this.frame = frame;
        }

        @Override
        public void draw(Graphics g, Point2D p) {
            drawFrame(g, frame, p);
        }

        @Override
        public Dimension getDimensions() {
            return getSingleDimensions();
        }

        @Override
        public Rectangle getBounds() {
            return getSingleBounds();
        }

        public int getFrame() {
            return frame;
        }

        public void setFrame(int frame) {
            checkFrame(frame);
            this.frame = frame;
        }

        public void advanceFrame() {
            frame = (frame + 1) % getLength();
        }
        
        public int getFrames() {
            return getLength();
        }
    }
}
