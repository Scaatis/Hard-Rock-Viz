package scaatis.rrr;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.json.JSONObject;

public abstract class MovingObject extends UpdatableObject {
    private Vector2D speed;

    public MovingObject(int id, GraphicsResource resource, Point2D location, Vector2D speed) {
        super(id, location, resource);
        this.speed = speed;
    }
    
    public MovingObject(int id, GraphicsResource resource, Point2D location, Vector2D speed, AffineTransform transform) {
        super(id, location, resource, transform);
        this.speed = speed;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public void update(double delta) {
        setLocation(speed.scale(delta).applyTo(getLocation()));
    }

    public void override(JSONObject obj) {
        super.override(obj);
        Vector2D speed = new Vector2D.Cartesian(obj.getDouble("speedX"), obj.getDouble("speedY"));
        setSpeed(speed);
    }

    // public static MovingObject fromJSON(JSONObject object) {
    // String message = object.getString("message");
    // if(message.equals("car")) {
    // return new Car(object);
    // } else if(message.equals("missile")) {
    // return new Missile(object);
    // } else if(message.equals("mine")) {
    // return new Mine(object);
    // }
    // return null;
    // }
}
