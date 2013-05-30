package scaatis.rrr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import org.json.JSONObject;

public class WaitingScene extends Scene {

    public WaitingScene() {
        super(0, Color.gray);
        FontSpriteSheet font;
        font = new FontSpriteSheet(ImageLoader.get("res/font.png"), new Point(), 16, 16);
        Drawable text = new Drawable(new Point(), font.new TextResource("Waiting for players..."));
        Dimension dim = text.getResource().getDimensions();
        Point center = SpriteViz.getScreenCenter();
        text.setLocation(new Point2D.Double(center.x - dim.width / 2, center.y - dim.height / 2));
        spawn(text);
    }

    @Override
    public void receiveJSON(JSONObject object) {

    }
//
//    @Override
//    public void draw(Graphics g, Point p) {
//        super.draw(g, p);
//        Graphics2D graphics2d = (Graphics2D) g.create();
//        graphics2d.translate(Math.cos(Math.PI / 4) * 300, 0);
//        graphics2d.scale(1, .25);
//        graphics2d.rotate(Math.PI / 4);
//        graphics2d.setColor(Color.white);
//        graphics2d.draw(new Rectangle(0, 0, 400, 300));
//    }
//    
//    

    @Override
    public void receiveInput(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

}
