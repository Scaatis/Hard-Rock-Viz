package scaatis.rrr;

import java.awt.Point;

public class PlacementIndicator extends UpdatableObject {

    private static Sprite background  = new Sprite(ImageLoader.get("res/scorebg.png"), new Point());
    private static Sprite firstPrize  = new Sprite(ImageLoader.get("res/winner.png"), new Point());
    private static Sprite secondPrize = new Sprite(ImageLoader.get("res/second.png"), new Point());
    private static Sprite thirdPrize  = new Sprite(ImageLoader.get("res/third.png"), new Point());

    private double        age;
    private boolean       left;
    private int           startx;
    private int           targetx;

    public PlacementIndicator(int id, Player player, int place) {
        super(id, new Point(), new Scene(0));
        age = 0;
        left = place != 2;

        int maxTextLength = (background.getBounds().width - 6) / ImageLoader.defaultFont.getSingleDimensions().width;
        String actualname = player.getName();
        if (actualname.length() > maxTextLength) {
            actualname = actualname.substring(0, maxTextLength);
        }
        Scene scene = (Scene) getResource();
        if (left) {
            scene.spawn(new Drawable(new Point(32, 32), PlayerData.getPortrait(player.getCharacter())));
            scene.spawn(new Drawable(new Point(68, 4), background));
            scene.spawn(new Drawable(new Point(71, 15), ImageLoader.defaultFont.new TextResource(actualname)));
            scene.spawn(new Drawable(new Point(71, 27), ImageLoader.defaultFont.new TextResource(player.getCharacter().getName())));
            scene.spawn(new Drawable(new Point(71, 39), ImageLoader.defaultFont.new TextResource(player.getCarType().getName())));
            scene.spawn(new Drawable(new Point(200, 0), getTrophy(place)));
            startx = -scene.getDimensions().width;
        } else {
            scene.spawn(new Drawable(new Point(), getTrophy(place)));
            scene.spawn(new Drawable(new Point(30, 4), background));
            scene.spawn(new Drawable(new Point(33, 15), ImageLoader.defaultFont.new TextResource(actualname)));
            scene.spawn(new Drawable(new Point(33, 27), ImageLoader.defaultFont.new TextResource(player.getCharacter().getName())));
            scene.spawn(new Drawable(new Point(33, 39), ImageLoader.defaultFont.new TextResource(player.getCarType().getName())));
            scene.spawn(new Drawable(new Point(192, 32), PlayerData.getPortrait(player.getCharacter())));
            startx = SpriteViz.getScreenWidth() + scene.getDimensions().width;
        }
        targetx = SpriteViz.getScreenWidth() / 2 - scene.getDimensions().width / 2;
        setLocation(new Point(startx, SpriteViz.getScreenHeight() / 2 - 100 + (place - 1) * 68));
        update(0);
    }

    private Sprite getTrophy(int place) {
        switch (place) {
        case 1:
            return firstPrize;
        case 2:
            return secondPrize;
        case 3:
            return thirdPrize;
        default:
            return null;
        }
    }

    @Override
    public void update(double delta) {
        if (age < PostRaceScene.pause) {
            age += delta;
            if (age > PostRaceScene.pause) {
                setLocation(new Point(targetx, (int) getLocation().getY()));
            } else {
                setLocation(new Point(startx + (int) ((targetx - startx) / PostRaceScene.pause * age),
                        (int) getLocation().getY()));
            }
        }

    }
}
