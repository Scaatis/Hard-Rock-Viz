package scaatis.rrr;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import scaatis.rrr.track.Track;

public class RaceScene extends Scene {
    private static final int         focusChangeInterval = 5;
    private static final int         locationBufferSize  = 5;
    private static final SpriteSheet explosion           = new SpriteSheet(ImageLoader.get("res/explosion.png"), new Point(24, 40), 5, 1);
    private static final SpriteSheet smoke               = new SpriteSheet(ImageLoader.get("res/smoke.png"), new Point(7, 7), 3, 1);

    private Track                    track;
    private TreeSet<Player>          players;
    private Collection<Integer>      removedIDs;
    private boolean                  raceStarted;
    private int                      idcounter;
    private MusicPlayer              music;
    private Player                   focusedPlayer;
    private List<Point2D>            savedLocations;
    private double                   focusChangeCooldown;
    private Random                   random;

    // private Announcer announcer;

    public RaceScene(JSONObject gamestart) {
        super(-1, new Color(0x735108));

        track = new Track(gamestart.getJSONObject("track"));
        spawn(track);

        JSONArray array = gamestart.getJSONArray("players");
        players = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < array.length(); i++) {
            Player player = new Player(array.getJSONObject(i), loadSpriteSheet(array.getJSONObject(i)));
            players.add(player);
        }

        removedIDs = new TreeSet<>();
        raceStarted = false;
        idcounter = getID();
        spawn(new ScoreBoard(--idcounter, new ArrayList<>(players), this));

        music = new MusicPlayer(--idcounter);
        music.play();
        spawn(music);

        // announcer = new Announcer(--idcounter);
        // spawn(announcer);
        random = new Random();
        focusChangeCooldown = 0;
        focusedPlayer = null;
        savedLocations = new ArrayList<>();
    }

    private void initializeFocus() {
        changeFocus();
        Point2D transformed = track.getIsoTransform().transform(focusedPlayer.getCar().getLocation(), null);
        for (int i = 0; i < locationBufferSize; i++) {
            savedLocations.add(transformed);
        }
    }

    public void changeFocus() {
        ArrayList<Player> pool = new ArrayList<>(players);
        while (focusedPlayer == null || focusedPlayer.getCar() == null) {
            focusedPlayer = pool.get(random.nextInt(pool.size()));
        }
        focusChangeCooldown = focusChangeInterval;
    }

    @Override
    public void update(double delta) {
        focusChangeCooldown -= delta;
        if (focusChangeCooldown < 0 && raceStarted) {
            changeFocus();
        }
        if (raceStarted && focusedPlayer.getCar() != null) {
            savedLocations.remove(0);
            Point2D transformed = track.getIsoTransform().transform(focusedPlayer.getCar().getLocation(), null);
            savedLocations.add(transformed);
            Point average = new Point();
            for (Point2D location : savedLocations) {
                average.x += (int) location.getX();
                average.y += (int) location.getY();
            }
            average.x /= locationBufferSize;
            average.y /= locationBufferSize;
            centerViewOn(average);
        }
        super.update(delta);
    }

    @Override
    public void receiveJSON(JSONObject object) {
        if (object.getString("message").equals("gamestate")) {

            TreeSet<UpdatableObject> unaffected = new TreeSet<>(getUpdatableObjects());
            JSONArray array = new JSONArray();
            addJSONArrays(array, object.getJSONArray("cars"));
            addJSONArrays(array, object.getJSONArray("missiles"));
            addJSONArrays(array, object.getJSONArray("mines"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                UpdatableObject updatableObject = getObjectByID(item.getInt("id"));
                if (updatableObject == null) {
                    if (removedIDs.contains(item.getInt("id"))) {
                        continue;
                    }
                    if (item.getString("message").equals("car")) {
                        spawn(getNewCar(item));
                    } else if (item.getString("message").equals("missile")) {
                        spawn(getNewMissile(item));
                    } else if (item.getString("message").equals("mine")) {
                        spawn(new Mine(item, track.getIsoTransform()));
                    }
                } else {
                    updatableObject.override(item);
                    unaffected.remove(updatableObject);
                }
            }
            // remove all objects which are no longer in the gamestate
            while (!unaffected.isEmpty()) {
                UpdatableObject object2 = unaffected.pollFirst();
                if (object2 instanceof MovingObject) {
                    despawn(object2);
                }
            }
            if (!raceStarted) {
                for (Player player : players) {
                    spawn(new PlayerNameIndicator(--idcounter, player));
                }
                // announcer.raceStart();
                initializeFocus();
                raceStarted = true;
            }
        } else if (object.getString("message").equals("missilehit")) {
            Player player = getPlayerByName(object.getString("target"));
            if(player.getCar() != null) {
                spawnExplosion(player.getCar().getLocation());
            }
        } else if(object.getString("message").equals("boost")) {
            Player player = getPlayerByName(object.getString("player"));
            if(player.getCar() != null) {
                spawnExplosion(player.getCar().getLocation());
            }
        }
    }

    public static void addJSONArrays(JSONArray a1, JSONArray a2) {
        for (int i = 0; i < a2.length(); i++) {
            a1.put(a2.get(i));
        }
    }

    public void spawnExplosion(Point2D location) {
        Point2D transformed = track.getIsoTransform().transform(location, null);
        spawn(new AnimatedBoom(--idcounter, transformed, explosion.new SpriteSheetResource(0), 20));
    }

    @Override
    public void despawn(Drawable drawable) {
        super.despawn(drawable);
        if (drawable instanceof UpdatableObject) {
            removedIDs.add(((UpdatableObject) drawable).getID());
        }
    }

    @Override
    public void cleanup() {
        music.stop();
        // announcer.stop();
    }

    private Car getNewCar(JSONObject carObject) {
        int id = carObject.getInt("id");
        Point2D location = new Point2D.Double(carObject.getDouble("locationX"), carObject.getDouble("locationY"));
        Vector2D speed = new Vector2D.Cartesian(carObject.getDouble("speedX"), carObject.getDouble("speedY"));
        int hp = carObject.getInt("hp");
        double facing = carObject.getDouble("facing");
        Player driver = getPlayerByName(carObject.getString("driver"));
        return new Car(id, location, speed, hp, facing, driver, track.getIsoTransform());
    }

    private Missile getNewMissile(JSONObject missileObject) {
        int id = missileObject.getInt("id");
        Point2D location = new Point2D.Double(missileObject.getDouble("locationX"), missileObject.getDouble("locationY"));
        Vector2D speed = new Vector2D.Cartesian(missileObject.getDouble("speedX"), missileObject.getDouble("speedY"));
        return new Missile(id, location, speed, track.getIsoTransform());
    }

    private Player getPlayerByName(String name) {
        Player seeker = new Player(name);
        Player ceil = players.ceiling(seeker);
        if (ceil != null && ceil.equals(seeker)) {
            return ceil;
        } else {
            return null;
        }
    }

    private UpdatableObject getObjectByID(int id) {
        UpdatableObject seeker = new UpdatableObject(id, null, null) {
            @Override
            public void update(double delta) {
            }
        };
        UpdatableObject ceil = getUpdatableObjects().ceiling(seeker);
        if (ceil != null && ceil.equals(seeker)) {
            return ceil;
        } else {
            return null;
        }
    }

    private RotateSpriteSheet loadSpriteSheet(JSONObject playerObject) {
        String name = playerObject.getString("cartype");
        name = name.replace(" ", "");
        name += RaceCharacter.getFromName(playerObject.getString("character")).getColor();
        BufferedImage resource = ImageLoader.get("res/cars/" + name + ".png");
        return new RotateSpriteSheet(resource, new Point(23, 32), 9, 5, 6);
    }
}
