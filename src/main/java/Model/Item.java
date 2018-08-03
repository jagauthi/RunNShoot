package Model;

import java.awt.Image;
import java.awt.Rectangle;

public class Item {

	int x, y, cost;
	Rectangle itemRect;
	static int WIDTH = 40;
	static int HEIGHT = 30;
	Image icon;

	public Item(int x, int y) {
		this.x = x;
		this.y = y;
		itemRect = new Rectangle(x, y, 20, 30);
	}

	public Rectangle getRect() {
		return itemRect;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCost() {
		return cost;
	}

	public Image getIcon() {
		return icon;
	}
	
	public void use(Player p) {
		
	}
}
