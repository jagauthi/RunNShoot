package Model;

public class Weapon extends Item {

	int xDir, yDir;
	int damage, ammo, maxAmmo, cost;
	Player wielder;
	long lastShootTime = 0;
	long delayTime;
	boolean shooting = false;
	String name;

	public Weapon(int x, int y, int d, Player w) {
		super(x, y);
		damage = d;
		wielder = w;
		xDir = 0;
		yDir = 0;
		cost = 0;
	}

	public void update() {

	}
	
	public void use(Player p) {
		wielder.weapon = this;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setXYDir(int xDir, int yDir) {
		this.xDir = xDir;
		this.yDir = yDir;
	}

	public int getDamage() {
		return damage;
	}

	public int getAmmo() {
		return ammo;
	}

	public int getCost() {
		return cost;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public String getName() {
		return name;
	}

	public void setDamage(int newDamage) {
		damage = newDamage;
	}

	public void setWielder(Player p) {
		wielder = p;
	}

	public void reload() {
		ammo = maxAmmo;
	}

	public boolean ableToShoot() {
		if (ammo > 0)
			return true;
		else
			return false;
	}

	public void stopUse() {
		shooting = false;
	}
}
