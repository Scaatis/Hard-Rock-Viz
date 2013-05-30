package scaatis.rrr;

import org.json.JSONObject;

public class Player {
    private String        name;
    private CarType       carType;
    private RaceCharacter character;
    private int           missiles;
    private int           mines;
    private int           boosts;
    private int           laps;
    private Car           car;
    private RotateSpriteSheet spriteSheet;

    public Player(JSONObject playerObject, RotateSpriteSheet spriteSheet) {
        name = playerObject.getString("name");
        carType = CarType.getFromString(playerObject.getString("cartype"));
        character = RaceCharacter.getFromName(playerObject.getString("character"));
        this.spriteSheet = spriteSheet;
        missiles = 0;
        mines = 0;
        boosts = 0;
        laps = 0;
    }
    
    protected Player(String name) {
        this.name = name;
        carType = null;
        character = null;
    }

    public void override(JSONObject carObject) {
        if (!name.equals(carObject.getString("driver"))) {
            return;
        }
        mines = carObject.getInt("mines");
        missiles = carObject.getInt("missiles");
        boosts = carObject.getInt("boosts");
        laps = carObject.getInt("lapscomplete");
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getLaps() {
        return laps;
    }

    public String getName() {
        return name;
    }

    public CarType getCarType() {
        return carType;
    }

    public RaceCharacter getCharacter() {
        return character;
    }

    public int getMissiles() {
        return missiles;
    }

    public int getMines() {
        return mines;
    }

    public int getBoosts() {
        return boosts;
    }
    
    public RotateSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
    
    public boolean equals(Object other) {
        if(!(other instanceof Player)) {
            return false;
        }
        Player other2 = (Player) other;
        return name.equals(other2.name);
    }
}
