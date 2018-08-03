package Model;

import java.awt.Rectangle;

import View.GamePanel;

public class Bullet {

	int x, y, xDirection, yDirection, finalDamage;
	static int WIDTH = 25;
	static int HEIGHT = 25;
	Rectangle bulletRect;

	public Bullet(int xPos, int yPos, int xDir, int yDir, int weaponDamage) {
		x = xPos;
		y = yPos;
		xDirection = xDir;
		yDirection = yDir;
		finalDamage = weaponDamage;
		bulletRect = new Rectangle();
	}

	public void update() {
		move();
		bulletRect.setBounds(x, y, WIDTH, HEIGHT);
	}

	public Rectangle getRect() {
		return bulletRect;
	}

	public void move() {
		x += xDirection;
		y += yDirection;
	}

	public Boolean isOutsideOfBounds() {
		if (x < 0 || x > GamePanel.WIDTH || y < 0 || y > GamePanel.HEIGHT)
			return true;
		else
			return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return WIDTH;
	}

}
