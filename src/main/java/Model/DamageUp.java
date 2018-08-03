package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class DamageUp extends PowerUp {

	public DamageUp(int x, int y, int power, long duration) {
		super(x, y, power, duration);
		name = "DamageUp";

		try {
			icon = ImageIO.read(new File("resources/sprites/DamageUp.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void use(Player p) {
		p.buffDamage(power, duration);
	}
}
