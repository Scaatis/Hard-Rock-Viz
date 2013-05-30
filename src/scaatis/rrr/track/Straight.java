package scaatis.rrr.track;

import java.awt.geom.Point2D;

import scaatis.rrr.ImageLoader;
import scaatis.rrr.Sprite;
import scaatis.rrr.Util;
import scaatis.rrr.Vector2D;

public class Straight extends TrackTile {

    public static Vector2D diffHor = new Vector2D.Cartesian(-96, -24);
    public static Vector2D diffVer = new Vector2D.Cartesian(96, -24);

    public Straight(TrackState state, Direction orientation) {
        super(state, orientation);
    }

    @Override
    public Sprite getSprite() {
        if (getOrientation() == Direction.UP || getOrientation() == Direction.DOWN) {
            return ImageLoader.straightVer;
        } else {
            return ImageLoader.straightHor;
        }
    }

    @Override
    protected Point2D getSpriteLocation(Direction driveDirection) {
        if (getOrientation() == Direction.LEFT || getOrientation() == Direction.RIGHT) {
            if (driveDirection == Direction.LEFT) {
                return diffHor.applyTo(getLocation());
            } else {
                return getLocation();
            }
        } else {
            if (driveDirection == Direction.DOWN) {
                return getLocation();
            } else {
                return diffVer.applyTo(getLocation());
            }
        }
    }

    @Override
    public TrackState getExit(TrackState state) {
        if (getOrientation() == Direction.LEFT || getOrientation() == Direction.RIGHT) {
            if (state.getDirection() == Direction.LEFT) {
                return new TrackState(Util.toPoint(getLocation()), state.getDirection());
            } else {
                return new TrackState(Util.toPoint(diffHor.scale(-1).applyTo(getLocation())), state.getDirection());
            }
        } else {
            if (state.getDirection() == Direction.DOWN) {
                return new TrackState(Util.toPoint(diffVer.scale(-1).applyTo(getLocation())), state.getDirection());
            } else {
                return new TrackState(Util.toPoint(getLocation()), state.getDirection());
            }
        }
    }
}
