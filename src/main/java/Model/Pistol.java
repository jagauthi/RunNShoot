package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class Pistol extends Weapon {

	public Pistol(int x, int y, int d, Player w) {
		super(x, y, d, w);
		name = "Pistol";
		ammo = 10;
		maxAmmo = 10;
		delayTime = 400;
		cost = 20;
		
		try {
			icon = ImageIO.read(new File("resources/sprites/Pistol.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		if (shooting && ableToShoot() && ((System.currentTimeMillis() - lastShootTime) > delayTime)) {
			wielder.sm.getCurrentState().addBullet(
					new Bullet((wielder.x + wielder.WIDTH), (wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
			ammo--;
			lastShootTime = System.currentTimeMillis();
		}
	}

	public void stopUse() {
		shooting = false;
		lastShootTime -= (delayTime - 100);
	}
}
