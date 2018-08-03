package Model;

public class Consumable extends Item {

	int power, cost;
	String name;

	public Consumable(int x, int y, int p) {
		super(x, y);
		power = p;
		cost = 10;
	}

	public void update() {

	}
	
	public void use(Player p) {
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPower() {
		return power;
	}

	public int getCost() {
		return cost;
	}

	public String getName() {
		return name;
	}
}
