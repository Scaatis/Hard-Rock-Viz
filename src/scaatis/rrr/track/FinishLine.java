package scaatis.rrr.track;

import scaatis.rrr.ImageLoader;
import scaatis.rrr.Sprite;

public class FinishLine extends Straight {

    public FinishLine(TrackState state, Direction orientation) {
        super(state, orientation);
    }

    @Override
    public Sprite getSprite() {
        if (getOrientation() == Direction.UP || getOrientation() == Direction.DOWN) {
            return ImageLoader.finishVer;
        } else {
            return ImageLoader.finishHor;
        }
    }

}
