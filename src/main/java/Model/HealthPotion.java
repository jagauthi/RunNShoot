package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class HealthPotion extends Consumable{

	public HealthPotion(int x, int y, int p) {
		super(x, y, p);
			
		try {
			icon = ImageIO.read(new File("resources/sprites/HealthPotion.png"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void use(Player p) {
		p.gainHealth(power);
	}
}
