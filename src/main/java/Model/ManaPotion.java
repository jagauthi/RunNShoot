package Model;

import java.io.File;

import javax.imageio.ImageIO;

public class ManaPotion extends Consumable{

	public ManaPotion(int x, int y, int p) {
		super(x, y, p);
			
		try {
			icon = ImageIO.read(new File("resources/sprites/ManaPotion.png"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void use(Player p) {
		p.gainMana(power);
	}
}
