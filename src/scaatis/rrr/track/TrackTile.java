package scaatis.rrr.track;

import java.awt.geom.Point2D;
import scaatis.rrr.Drawable;
import scaatis.rrr.Sprite;

public abstract class TrackTile extends Drawable {
	
	public static final int SEGMENT_LENGTH = 45;
	public static final int TRACK_WIDTH = SEGMENT_LENGTH * 5;
	
	private Direction orientation;

	protected TrackTile(TrackState state, Direction orientation) {
	    super(state.getLocation(), null);
		this.orientation = orientation;
		setResource(getSprite());
		setLocation(getSpriteLocation(state.getDirection()));
	}
	
	public abstract Sprite getSprite();
	protected abstract Point2D getSpriteLocation(Direction driveDirection);
	public abstract TrackState getExit(TrackState state);
	
	public Direction getOrientation() {
		return orientation;
	}
}
