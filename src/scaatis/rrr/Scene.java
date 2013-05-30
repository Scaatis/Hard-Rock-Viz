package scaatis.rrr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONObject;

public class Scene extends UpdatableObject implements GraphicsResource {
    private List<Drawable>           drawables;
    private TreeSet<UpdatableObject> updatables;
    private Color                    background;
    private Point2D                  viewport;

    public Scene(int id) {
        this(id, null, new Point());
    }

    public Scene(int id, Point2D location) {
        this(id, null, location);
    }

    public Scene(int id, Color background) {
        this(id, background, new Point());
    }

    public Scene(int id, Color background, Point2D location) {
        super(id, location, null);
        drawables = new ArrayList<>();
        updatables = new TreeSet<>(new Comparator<UpdatableObject>() {

            @Override
            public int compare(UpdatableObject o1, UpdatableObject o2) {
                return Integer.compare(o1.getID(), o2.getID());
            }
        });
        this.background = background;
        viewport = new Point2D.Double();
        setResource(this);
    }

    public void update(double delta) {
        for (UpdatableObject object : updatables) {
            object.update(delta);
        }
        Iterator<Drawable> iterator = drawables.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isDestroyed()) {
                iterator.remove();
            }
        }
    }

    public void spawn(Drawable drawable) {
        drawables.add(drawable);
        if (drawable instanceof UpdatableObject) {
            updatables.add((UpdatableObject) drawable);
        }
    }

    public void despawn(Drawable drawable) {
        drawable.destroy();
    }

    @Override
    public final void draw(Graphics g, Point2D p) {
        Graphics2D graphics2d = (Graphics2D) g.create();
        graphics2d.translate(p.getX() - viewport.getX(), p.getY() - viewport.getY());

        if (background != null) {
            Graphics g2 = graphics2d.create();
            g2.setColor(background);
            Rectangle r = g2.getClipBounds();
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.dispose();
        }
        Collections.sort(drawables, new ZSort());
        for (Drawable drawable : drawables) {
            drawable.draw(graphics2d);
        }
        graphics2d.dispose();
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    @Override
    public Dimension getDimensions() {
        return getBounds().getSize();
    }

    public Point2D getViewport() {
        return viewport;
    }

    public void setViewport(Point2D viewport) {
        this.viewport = viewport;
    }

    protected List<Drawable> getDrawables() {
        return drawables;
    }

    protected TreeSet<UpdatableObject> getUpdatableObjects() {
        return updatables;
    }

    @Override
    public Rectangle getBounds() {
        Rectangle rectangle = new Rectangle();
        for (Drawable drawable : drawables) {
            Rectangle rect = new Rectangle(drawable.getBounds());
            rectangle.add(rect);
        }
        return rectangle;
    }

    public void centerViewOn(Point2D point2d) {
        setViewport(new Point2D.Double(point2d.getX() - SpriteViz.getScreenWidth() / 2,
                point2d.getY() - SpriteViz.getScreenHeight() / 2));
    }

    public void cleanup() {

    }

    public void receiveJSON(JSONObject object) {

    }

    public void receiveInput(KeyEvent event) {

    }
}
