package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class RotateSpriteSheet extends SpriteSheet {
    private int stepsPerQuarter;

    public RotateSpriteSheet(BufferedImage image, Point origin, int colums, int rows, int steps) {
        super(image, origin, colums, rows);
        stepsPerQuarter = steps;
    }

    public void drawDirection(Graphics g, double direction, Point2D location) {
        // assumptions: spritesheets starts with down sprites and contains only
        // images facing left.
        // 0 - right
        // 1/2 PI - down
        double normaldir = normalizeDirection(direction);
        int frame = getFrame(normaldir);
        boolean invert = (normaldir < 0.25 * Math.PI || normaldir > 1.25 * Math.PI);
        if (invert) {
            drawFrameXMirror(g, frame, location);
        } else {
            drawFrame(g, frame, location);
        }
    }

    private double normalizeDirection(double direction) {
        double normaldir = direction;
        while (normaldir > 2 * Math.PI) {
            normaldir -= 2 * Math.PI;
        }
        while (normaldir < 0) {
            normaldir += 2 * Math.PI;
        }
        return normaldir;
    }

    private int getFrame(double direction) {
        double res;
        if (direction < 0.25 * Math.PI) {
            res = -2 * stepsPerQuarter / Math.PI * direction + .5 * stepsPerQuarter;
        } else if (direction < 1.25 * Math.PI) {
            res = 2 * stepsPerQuarter / Math.PI * direction - .5 * stepsPerQuarter;
        } else {
            res = -2 * stepsPerQuarter / Math.PI * direction + 4.5 * stepsPerQuarter;
        }
        return (int) Math.round(res);
    }

    public class DirectionSpriteResource implements GraphicsResource {
        private double direction;

        public DirectionSpriteResource(double direction) {
            this.direction = direction;
        }

        @Override
        public void draw(Graphics g, Point2D p) {
            drawDirection(g, direction, p);
        }

        @Override
        public Dimension getDimensions() {
            return getSingleDimensions();
        }

        @Override
        public Rectangle getBounds() {
            return getSingleBounds();
        }

        public double getDirection() {
            return direction;
        }

        public void setDirection(double direction) {
            this.direction = direction;
        }
    }
}
