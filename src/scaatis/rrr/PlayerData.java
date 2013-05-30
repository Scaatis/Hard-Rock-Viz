package scaatis.rrr;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import scaatis.rrr.FontSpriteSheet.TextResource;

public class PlayerData extends Scene {
    public static Sprite   healthBlobFull  = new Sprite(ImageLoader.get("res/healthBlobFull.png"), new Point(3, 3));
    public static Sprite   healthBlobSmall = new Sprite(ImageLoader.get("res/healthBlobSmall.png"), new Point(3, 3));
    public static Sprite   missileIcon     = new Sprite(ImageLoader.get("res/missileIcon.png"), new Point(7, 7));
    public static Sprite   mineIcon        = new Sprite(ImageLoader.get("res/mineIcon.png"), new Point(7, 7));
    public static Sprite   boostIcon       = new Sprite(ImageLoader.get("res/boostIcon.png"), new Point(7, 7));

    private Player         player;
    private boolean        leftOriented;
    private TextResource   missileCount;
    private TextResource   boostCount;
    private TextResource   mineCount;
    private TextResource   lapCount;
    private int            lastHp;
    private List<Drawable> blobs;

    public PlayerData(int id, Point2D location, Player player, boolean leftOriented, int maxWidth) {
        super(id, location);
        this.player = player;
        this.leftOriented = leftOriented;
        blobs = new ArrayList<>();
        Point portraitLocation = new Point((leftOriented ? 32 : -32), 32);
        spawn(new Drawable(portraitLocation, getPortrait(player.getCharacter())));

        int maxTextLength = (maxWidth - 72) / ImageLoader.defaultFont.getSingleBounds().width;
        String actualName = player.getName().length() > maxTextLength ?
                player.getName().substring(0, maxTextLength)
                : player.getName();
        TextResource name = ImageLoader.defaultFont.new TextResource(actualName);
        Point nameLocation = new Point((leftOriented ? 68 : -68 - name.getBounds().width),
                12);
        spawn(new Drawable(nameLocation, name));

        Point missileIconPos = new Point((leftOriented ? 75 : -86),
                21);
        spawn(new Drawable(missileIconPos, missileIcon));
        Point missileCountPos = new Point(missileIconPos);
        missileCountPos.translate(10, 8);
        missileCount = ImageLoader.defaultFont.new TextResource(Integer.toString(player.getMissiles()));
        spawn(new Drawable(missileCountPos, missileCount));

        Point boostIconPos = new Point(missileIconPos);
        Point boostCountPos = new Point(missileCountPos);
        boostIconPos.translate((leftOriented ? 30 : -30), 0);
        boostCountPos.translate((leftOriented ? 30 : -30), 0);
        boostCount = ImageLoader.defaultFont.new TextResource(Integer.toString(player.getBoosts()));
        spawn(new Drawable(boostIconPos, boostIcon));
        spawn(new Drawable(boostCountPos, boostCount));

        Point mineIconPos = new Point(boostIconPos);
        Point mineCountPos = new Point(boostCountPos);
        mineIconPos.translate((leftOriented ? 30 : -30), 0);
        mineCountPos.translate((leftOriented ? 30 : -30), 0);
        mineCount = ImageLoader.defaultFont.new TextResource(Integer.toString(player.getMines()));
        spawn(new Drawable(mineIconPos, mineIcon));
        spawn(new Drawable(mineCountPos, mineCount));
        lastHp = -1;

        lapCount = ImageLoader.defaultFont.new TextResource("Laps: " + player.getLaps());
        Point lapsPos = new Point((leftOriented ? 68 : -68 - lapCount.getBounds().width),
                52);
        spawn(new Drawable(lapsPos, lapCount));
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (player.getCar() != null) {
            updateBlobs();
        }
        updateCounts();
        updateLaps();
    }

    private void updateLaps() {
        lapCount.setText("Laps: " + player.getLaps());
    }

    private void updateBlobs() {
        int hp = player.getCar().getHp();
        if (hp == lastHp) {
            return;
        }
        lastHp = hp;
        Point currentPoint = new Point((leftOriented ? 71 : -71), 36);
        int xdiff = (leftOriented ? 8 : -8);

        for (Drawable blob : blobs) {
            despawn(blob);
        }
        blobs.clear();

        while (hp > 0) {
            Drawable blob;
            if (hp > 1) {
                blob = new Drawable(new Point(currentPoint), healthBlobFull);
                currentPoint.translate(xdiff, 0);
                hp -= 2;
            } else {
                blob = new Drawable(new Point(currentPoint), healthBlobSmall);
                hp--;
            }
            spawn(blob);
            blobs.add(blob);
        }
    }

    private void updateCounts() {
        missileCount.setText(Integer.toString(player.getMissiles()));
        mineCount.setText(Integer.toString(player.getMines()));
        boostCount.setText(Integer.toString(player.getBoosts()));
    }

    public static Sprite getPortrait(RaceCharacter character) {
        return new Sprite(ImageLoader.get("res/portraits/" + character.getName() + ".png"), new Point(32, 32));
    }

    @Override
    public void receiveJSON(JSONObject object) {

    }

    @Override
    public void receiveInput(KeyEvent event) {

    }

}
