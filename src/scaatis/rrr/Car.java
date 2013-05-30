package scaatis.rrr;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.json.JSONObject;

import scaatis.rrr.RotateSpriteSheet.DirectionSpriteResource;

public class Car extends MovingObject {
    
    private int hp;
    private double facing;
    private Player driver;
    
    public Car(int id, Point2D location, Vector2D speed, int hp, double facing, Player driver) {
        super(id, driver.getSpriteSheet().new DirectionSpriteResource(facing), location, speed);
        this.hp = hp;
        this.facing = facing;
        this.driver = driver;
        driver.setCar(this);
    }
    
    public Car(int id, Point2D location, Vector2D speed, int hp, double facing, Player driver, AffineTransform transform) {
        super(id, driver.getSpriteSheet().new DirectionSpriteResource(facing), location, speed, transform);
        this.hp = hp;
        this.facing = facing;
        this.driver = driver;
        driver.setCar(this);
    }

    @Override
    public void override(JSONObject carObject) {
        if(getID() != carObject.getInt("id")) {
            return;
        }
        super.override(carObject);
        hp = carObject.getInt("hp");
        facing = carObject.getDouble("facing");
        driver.override(carObject);
        if(getResource() instanceof RotateSpriteSheet.DirectionSpriteResource) {
            ((DirectionSpriteResource) getResource()).setDirection(facing);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public double getFacing() {
        return facing;
    }

    public void setFacing(double facing) {
        this.facing = facing;
    }

    public Player getDriver() {
        return driver;
    }
}
