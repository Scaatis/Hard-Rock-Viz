package scaatis.rrr;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.json.JSONObject;

public abstract class UpdatableObject extends Drawable {
    
    private int id;
    
    public UpdatableObject(int id, Point2D location, GraphicsResource resource) {
        super(location, resource);
        this.id = id;
    }
    
    public UpdatableObject(int id, Point2D location, GraphicsResource resource, AffineTransform transform) {
        super(location, resource, transform);
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
    
    public abstract void update(double delta);
    
    public void override(JSONObject obj) {
        Point2D loc = new Point2D.Double(obj.getDouble("locationX"), obj.getDouble("locationY"));
        setLocation(loc);
    }
    
    public boolean equals(Object other) {
        if(!(other instanceof UpdatableObject)) {
            return false;
        }
        UpdatableObject other2 = (UpdatableObject) other;
        return id == other2.id;
    }
}
