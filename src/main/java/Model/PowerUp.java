package Model;

public class PowerUp extends Item {

	int power;
	long duration;
	int WIDTH = 50;
	int HEIGHT = 30;
	String name;

	public PowerUp(int x, int y, int power, long duration) {
		super(x, y);
		this.power = power;
		this.duration = duration;
	}

	public int getPower() {
		return power;
	}

	public long getDuration() {
		return duration;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getName() {
		return name;
	}
	
	public void use(Player p) {
		
	}
}
