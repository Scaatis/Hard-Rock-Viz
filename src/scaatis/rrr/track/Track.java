package scaatis.rrr.track;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.json.JSONArray;
import org.json.JSONObject;

import scaatis.rrr.ComboSprite;
import scaatis.rrr.Drawable;

public class Track extends Drawable {

    private AffineTransform transform;

    public Track(JSONObject trackObject) {
        super(new Point(), new ComboSprite());
        parseFromJSON(trackObject);
    }

    private void parseFromJSON(JSONObject trackObject) {
        Direction startDir = Direction.valueOf(trackObject.getString("startdir"));
        // create track tiles, calculate size
        TrackState state = new TrackState(0, 0, startDir);

        JSONArray descriptions = trackObject.getJSONArray("tiles");
        // ArrayList<TrackTile> tiles = new ArrayList<>();
        ComboSprite comboSprite = (ComboSprite) getResource();
        int finishX = 0;
        int finishY = 0;
        
        for (int i = 0; i < descriptions.length(); i++) {
            JSONArray array = descriptions.getJSONArray(i);
            String description = array.getString(0);
            TrackTile tile = null;
            if (description.equals("straight")) {
                tile = new Straight(state, state.getDirection());
            } else if (description.equals("turnright")) {
                tile = new Curve(state, state.getDirection());
            } else if (description.equals("turnleft")) {
                tile = new Curve(state, state.getDirection().clockwise());
            } else if (description.equals("finish")) {
                tile = new FinishLine(state, state.getDirection());
                finishX = array.getInt(1);
                finishY = array.getInt(2);
            }
            if (tile == null) {
                throw new RuntimeException("Unknown track tile " + description);
            }
            comboSprite.add(tile);
            state = tile.getExit(state);
        }
        int height = trackObject.getInt("height");
        
        // calculate transform from regular to isometric coordinates
        transform = AffineTransform.getScaleInstance(1, .25);
        transform.concatenate(AffineTransform.getTranslateInstance(height / Math.sqrt(2), 0));
        transform.concatenate(AffineTransform.getRotateInstance(Math.PI / 4));
        
        comboSprite.bake();
        
        Point finish = new Point(finishX, finishY);
        if(startDir == Direction.LEFT) {
            finish.translate(TrackTile.SEGMENT_LENGTH * 3, 0);
        } else if(startDir == Direction.UP) {
            finish.translate(0, TrackTile.SEGMENT_LENGTH * 3);
        }
        Point2D transformedFinish = transform.transform(finish, null);
        Point origin = comboSprite.getOrigin();
        
        comboSprite.setOrigin(new Point(origin.x - (int) transformedFinish.getX(), origin.y - (int) transformedFinish.getY()));
    }

    public AffineTransform getIsoTransform() {
        return transform;
    }

    @Override
    public int getZ() {
        return Integer.MIN_VALUE;
    }
}
