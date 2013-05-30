package scaatis.rrr;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;

import org.json.JSONObject;

public class ScoreBoard extends Scene {

    private static final int margin = 8;

    private Scene            parent;

    public ScoreBoard(int id, List<Player> players, Scene parent) {
        super(id);
        this.parent = parent;
        updateLocation();
        for (int i = 0; i < players.size(); i++) {
            spawn(new PlayerData(i + 1, getCorner(i), players.get(i), i % 2 == 0,
                    SpriteViz.getScreenWidth() / 2 - 3 * margin));
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        updateLocation();
    }

    private void updateLocation() {
        setLocation(parent.getViewport());
    }

    private Point getCorner(int i) {
        int width = SpriteViz.getScreenWidth();
        int height = SpriteViz.getScreenHeight();
        int x = i % 2 == 0 ? margin : width - margin;
        int y = i / 2 == 0 ? margin : height - margin - 64;
        return new Point(x, y);
    }

    @Override
    public void receiveJSON(JSONObject object) {

    }

    @Override
    public void receiveInput(KeyEvent event) {

    }

    @Override
    public int getZ() {
        return Integer.MAX_VALUE;
    }
}
