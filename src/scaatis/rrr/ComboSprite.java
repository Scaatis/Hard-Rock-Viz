package scaatis.rrr;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComboSprite extends Sprite {
    private List<Drawable> sprites;
    private boolean        modified;

    public ComboSprite() {
        this(new Point());
    }

    public ComboSprite(Point origin) {
        super(null, origin);
        sprites = new ArrayList<>();
        modified = true;
    }

    public void add(Drawable d) {
        sprites.add(d);
        modified = true;
    }

    public void bake() {
        Collections.sort(sprites, new ZSort());
        Rectangle dimensions = new Rectangle();
        for (Drawable sprite : sprites) {
            dimensions.add(sprite.getBounds());
        }
        BufferedImage result = new BufferedImage(dimensions.width,
                dimensions.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.translate(-dimensions.x, -dimensions.y);
        for (Drawable sprite : sprites) {
            sprite.draw(g);
        }
        g.dispose();
        Point oldOrigin = getOrigin();
        setOrigin(new Point(oldOrigin.x - dimensions.x, oldOrigin.y - dimensions.y));
        setImage(result);
        modified = false;
    }

    @Override
    public void draw(Graphics g, Point2D p) {
        if (modified) {
            bake();
        }
        super.draw(g, p);
    }

}
