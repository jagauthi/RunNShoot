package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class SpeedUp extends PowerUp {

	public SpeedUp(int x, int y, int power, long duration) {
		super(x, y, power, duration);
		name = "SpeedUp";

		try {
			icon = ImageIO.read(new File("resources/sprites/SpeedUp.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void use(Player p) {
		p.buffSpeed(power, duration);
	}
}
