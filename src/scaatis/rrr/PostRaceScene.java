package scaatis.rrr;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import scaatis.rrr.FontSpriteSheet.TextResource;

public class PostRaceScene extends Scene {

    public static final int    pause = 3;

    private ArrayList<Player>  players;
    private PlacementIndicator placement;
    private int                currentPlace;
    private double             pausetimer;
//    private Announcer          announcer;

    public PostRaceScene(JSONObject finishObject) {
        super(-1, new Color(0x600000));
        JSONArray array = finishObject.getJSONArray("placement");
        players = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            players.add(new Player(array.getJSONObject(i), null));
        }
        currentPlace = 1;
//        announcer = new Announcer(-1);
//        spawn(announcer);
        spawnNextPlayer();
        pausetimer = pause;
        TextResource res  = ImageLoader.defaultFont.new TextResource("Race finished");
        spawn(new Drawable(new Point(SpriteViz.getScreenWidth() / 2 - res.getDimensions().width / 2, 12), res));
    }

    public void spawnNextPlayer() {
        Player player = players.remove(0);
        placement = new PlacementIndicator(currentPlace, player, currentPlace);
        spawn(placement);
//        if (currentPlace == 1) {
//            announcer.firstPlace(player.getCharacter());
//        } else if (currentPlace == 2) {
//            announcer.secondPlace(player.getCharacter());
//        } else {
//            announcer.thirdPlace(player.getCharacter());
//        }
        currentPlace++;
    }
    
    @Override
    public void cleanup() {
//        announcer.stop();
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (pausetimer > 0) {
            pausetimer -= delta;
            if ( pausetimer <= 0 && !players.isEmpty() && currentPlace < 4) {
                spawnNextPlayer();
                pausetimer = pause;
            }
        }
    }

}
