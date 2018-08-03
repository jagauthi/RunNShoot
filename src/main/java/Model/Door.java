package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door {

	Rectangle doorRect;
	int x, y;
	public static int WIDTH = 50;
	public static int HEIGHT = 100;
	boolean locked;

	public Door(int x, int y) {
		this.x = x;
		this.y = y;
		doorRect = new Rectangle(x, y, WIDTH, HEIGHT);
		locked = true;
	}

	public void draw(Graphics g, int xOffset, int yOffset) {
		g.setColor(new Color(0.5f, 0.2f, 0.1f));
		g.fillRect(x- xOffset, y- yOffset, WIDTH, HEIGHT);
	}

	public Boolean isLocked() {
		return locked;
	}

	public void lock() {
		locked = true;
	}

	public void unlock() {
		locked = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rectangle getRect() {
		return doorRect;
	}
}
