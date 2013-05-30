package scaatis.rrr;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class PlayerNameIndicator extends UpdatableObject {

    private Player           following;
    private int              xoffset;
    private static final int yoffset = 32;

    public PlayerNameIndicator(int id, Player player) {
        super(id, player.getCar().getLocation(), ImageLoader.defaultFont.new TextResource(player.getName()), player.getCar().getTransform());
        following = player;
        xoffset = getResource().getBounds().width / 2;
    }

    @Override
    public void update(double delta) {
        setLocation(following.getCar().getLocation());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g.create();
        graphics2d.scale(.5, .5);
        Point2D drawLoc = getTransform().transform(getLocation(), null);
        drawLoc = new Point2D.Double(2 * (drawLoc.getX() - xoffset), 2 * (drawLoc.getY() - yoffset));
        getResource().draw(graphics2d, drawLoc);
        graphics2d.dispose();
    }

}
