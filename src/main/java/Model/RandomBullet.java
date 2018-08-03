package Model;

import java.awt.Rectangle;
import java.util.Random;

import View.GamePanel;

public class RandomBullet extends Bullet {

	int x, y, xDirection, yDirection, finalDamage, randomness;
	static int WIDTH = 10;
	static int HEIGHT = 10;
	Rectangle bulletRect;

	Random pathFinder;

	public RandomBullet(int xPos, int yPos, int xDir, int yDir, int weaponDamage) {
		super(xPos, yPos, xDir, yDir, weaponDamage);
		x = xPos;
		y = yPos;
	
		xDirection = xDir; 
		yDirection = yDir; 
		finalDamage = weaponDamage;
		
		randomness = 10;
		
		bulletRect = new Rectangle();
		pathFinder = new Random();
	}

	public void update() {
		move();
		bulletRect.setBounds(x, y, WIDTH, HEIGHT);
	}

	public Rectangle getRect() {
		return bulletRect;
	}

	public void move() {
		x += xDirection += pathFinder.nextInt(randomness) - randomness/2;
		y += yDirection += pathFinder.nextInt(randomness) - randomness/2;
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
