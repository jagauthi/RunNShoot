package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class MachineGun extends Weapon {

	public MachineGun(int x, int y, int d, Player w) {
		super(x, y, d, w);
		name = "Machine Gun";
		ammo = 100;
		maxAmmo = 100;
		delayTime = 100;
		cost = 50;

		try {
			icon = ImageIO.read(new File("resources/sprites/MachineGun.png"));
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
}
