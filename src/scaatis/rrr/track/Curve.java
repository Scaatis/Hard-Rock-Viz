package scaatis.rrr.track;

import java.awt.geom.Point2D;
import scaatis.rrr.ImageLoader;
import scaatis.rrr.Sprite;
import scaatis.rrr.Util;
import scaatis.rrr.Vector2D;

/**
 * Curve is always a right bend when moving in the direction orienation.
 * 
 * @author Felix Schneider
 * @version
 */
public class Curve extends TrackTile {

    public static Vector2D diffLeft = new Vector2D.Cartesian(-151, -56);
    public static Vector2D diffUp = new Vector2D.Cartesian(374, 0);
    public static Vector2D diffRight = new Vector2D.Cartesian(-151, 56);
    public static Vector2D diffDown = new Vector2D.Cartesian(-73,0);
    
    public Curve(TrackState state, Direction orientation) {
        super(state, orientation);
    }
	
    @Override
    public Sprite getSprite() {
        if(getOrientation() == Direction.LEFT) {
            return ImageLoader.curveRightUp;
        } else if(getOrientation() == Direction.RIGHT) {
            return ImageLoader.curveLeftDown;
        } else if(getOrientation() == Direction.UP) {
            return ImageLoader.curveDownRight;
        } else {
            return ImageLoader.curveLeftUp;
        }
    }

    @Override
    public Point2D getSpriteLocation(Direction driveDirection) {
        if(getOrientation() == Direction.LEFT) {
            if(driveDirection == getOrientation()) {
                return diffLeft.applyTo(getLocation());
            } else {
                return getLocation();
            }
        } else if(getOrientation() == Direction.RIGHT) {
            if(driveDirection == getOrientation()) {
                return getLocation();
            } else {
                return diffRight.scale(-1).applyTo(getLocation());
            }
        } else if(getOrientation() == Direction.UP) {
            if(driveDirection == getOrientation()) {
                return getLocation();
            } else {
                return diffUp.scale(-1).applyTo(getLocation());
            }
        } else {
            if(driveDirection == getOrientation()) {
                return diffDown.applyTo(getLocation());
            } else {
                return getLocation();
            }
        }
    }

    @Override
    public TrackState getExit(TrackState trackState) {
        Direction direction;
        if(trackState.getDirection() == getOrientation()) {
            direction = trackState.getDirection().clockwise();
        } else {
            direction = trackState.getDirection().cclockwise();
        }
        Point2D location;
        Direction driveDirection = trackState.getDirection();
        
        if(getOrientation() == Direction.LEFT) {
            if(driveDirection == getOrientation()) {
                location = getLocation();
            } else {
                location = diffLeft.scale(-1).applyTo(getLocation());
            }
        } else if(getOrientation() == Direction.RIGHT) {
            if(driveDirection == getOrientation()) {
                location = diffRight.applyTo(getLocation());
            } else {
                location = getLocation();
            }
        } else if(getOrientation() == Direction.UP) {
            if(driveDirection == getOrientation()) {
                location = diffUp.applyTo(getLocation());
            } else {
                location = getLocation();
            }
        } else {
            if(driveDirection == getOrientation()) {
                location = getLocation();
            } else {
                location = diffDown.scale(-1).applyTo(getLocation());
            }
        }
        return new TrackState(Util.toPoint(location), direction);
    }
}
