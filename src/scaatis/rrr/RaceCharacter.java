package scaatis.rrr;

public enum RaceCharacter {

	CYBERHAWK("Cyberhawk", "Grey"), IVANZYPHER("Ivanzypher", "Yellow"), JAKE_BADLANDS(
			"Jake Badlands", "Black"), KATARINA_LYONS("Katarina Lyons", "Green"), RIP("Rip", "Red"), SNAKE_SANDERS(
			"Snake Sanders", "Blue"), TARQUINN("Tarquinn", "Purple"), VIPER_MACKAY("Viper Mackay", "Orange");

	private String name;
	private String color;

	private RaceCharacter(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}
	
	public String getColor() {
	    return color;
	}

	public static RaceCharacter getFromName(String name) {
		RaceCharacter[] val = RaceCharacter.values();
		for (RaceCharacter ch : val) {
			if (ch.getName().equals(name)) {
				return ch;
			}
		}
		return null;
	}
}
