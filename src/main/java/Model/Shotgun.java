package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class Shotgun extends Weapon {

	int angleFix = 20;
	int spread = 20;

	public Shotgun(int x, int y, int d, Player w) {
		super(x, y, d, w);
		name = "Shotgun";
		ammo = 8;
		maxAmmo = 8;
		delayTime = 500;
		cost = 100;

		try {
			icon = ImageIO.read(new File("resources/sprites/Shotgun.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		if (shooting && ableToShoot() && ((System.currentTimeMillis() - lastShootTime) > delayTime)) {
			if (xDir < angleFix && yDir < -angleFix) // draw up
			{
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH - (2 * spread)),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH - spread),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(
						new Bullet((wielder.x + wielder.WIDTH), (wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH + spread),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				ammo--;
				lastShootTime = System.currentTimeMillis();
			} else if (xDir > angleFix && yDir < angleFix) {
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 - (2 * spread)), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 - spread), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(
						new Bullet((wielder.x + wielder.WIDTH), (wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 + spread), xDir, yDir, damage));
				ammo--;
				lastShootTime = System.currentTimeMillis();
			} else if (xDir < angleFix && yDir > angleFix) {
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH - (2 * spread)),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH - spread),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(
						new Bullet((wielder.x + wielder.WIDTH), (wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH + spread),
						(wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				ammo--;
				lastShootTime = System.currentTimeMillis();
			} else if (xDir < -angleFix && yDir < angleFix) {
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 - (2 * spread)), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 - spread), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(
						new Bullet((wielder.x + wielder.WIDTH), (wielder.y + wielder.HEIGHT / 2), xDir, yDir, damage));
				wielder.sm.getCurrentState().addBullet(new Bullet((wielder.x + wielder.WIDTH),
						(wielder.y + wielder.HEIGHT / 2 + spread), xDir, yDir, damage));
				ammo--;
				lastShootTime = System.currentTimeMillis();
			}
		}
	}
}
