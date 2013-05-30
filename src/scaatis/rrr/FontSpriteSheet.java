package scaatis.rrr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FontSpriteSheet extends SpriteSheet {

    public FontSpriteSheet(BufferedImage image, Point origin, int colums, int rows) {
        super(image, origin, colums, rows);
    }

    public void drawChar(Graphics g, char ch, Point2D location) {
        drawFrame(g, (int) ch, location);
    }

    public class TextResource implements GraphicsResource {
        private String text;

        public TextResource(String text) {
            this.text = text;
        }

        @Override
        public void draw(Graphics g, Point2D p) {
            int w = getSingleDimensions().width;
            double x = p.getX();
            for (int i = 0; i < text.length(); i++) {
                try {
                    drawChar(g, text.charAt(i), new Point2D.Double(x, p.getY()));
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                x += w;
            }
        }

        @Override
        public Dimension getDimensions() {
            Dimension singleChar = getSingleDimensions();
            return new Dimension(text.length() * singleChar.width, singleChar.height);
        }

        @Override
        public Rectangle getBounds() {
            Point center = new Point(-getOrigin().x, -getOrigin().y);
            return new Rectangle(center, getDimensions());
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
