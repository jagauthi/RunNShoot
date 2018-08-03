package Model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import Controller.GameState;
import View.GamePanel;

import javax.imageio.ImageIO;

public class Enemy {

	int x, y, health, maxHealth, xDir, yDir, exp, damage, speed, gold;
	public int WIDTH = 30;
	public int HEIGHT = 50;

	private final int AGGRO_RANGE = 100;
	private final int BASE_SPEED = 6;
	private final int BASE_DAMAGE = 20;

	private Player target;
	private Boolean isAlive = false;
	private Boolean aggrod = false;
	Rectangle enemyRect;
	private GameState game;

	private BufferedImage image;

	public Enemy(int xPos, int yPos, Player p, GameState game) {
		x = xPos;
		y = yPos;
		health = 50;
		target = p;
		this.game = game;
		exp = 10;
		damage = BASE_DAMAGE;
		speed = BASE_SPEED;
		maxHealth = health;
		gold = 10;
		enemyRect = new Rectangle(x, y, WIDTH, HEIGHT);
		try {
			image = ImageIO.read(new File("resources/sprites/wolfSprite.png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		checkIfHit();
		if (health < 0)
			die();
		checkIfDamagingPlayer();
		checkIfAggrod();
		getDirectionToPlayer();
		avoidEnemies();
		//detectEdges();
		if (aggrod)
			move();
		enemyRect.setBounds(x, y, WIDTH, HEIGHT);
	}

	private void checkIfDamagingPlayer() {
		if(enemyRect.intersects(target.getRect())) {
			target.takeDamage(damage);
		}
	}

	public void detectEdges() {
		if (x < 10)
			setXDirection(1);
		else if (x + WIDTH > GamePanel.WIDTH)
			setXDirection(-1);

		if (y < 50)
			setYDirection(1);
		else if (y + HEIGHT > 5 * GamePanel.HEIGHT / 6)
			setYDirection(-1);
	}

	private void move() {
		x += xDir;
		y += yDir;
	}

	private void setXDirection(int newX) {
		xDir = newX;
	}

	private void setYDirection(int newY) {
		yDir = newY;
	}

	private void checkIfHit() {
		for (int x = 0; x < game.getBullets().size(); x++) {
			if (game.getBullets().get(x).bulletRect.intersects(enemyRect)) {
				takeDamage(game.getBullets().get(x).finalDamage);
				game.getBullets().remove(x);
			}
		}
	}

	private void checkIfAggrod() {
		if (x < target.x + target.WIDTH + AGGRO_RANGE && x + WIDTH > target.x - AGGRO_RANGE) {
			if (y < target.y + target.HEIGHT + AGGRO_RANGE & y + HEIGHT > target.y - AGGRO_RANGE) {
				aggrod = true;
			} else
				aggrod = false;
		} else
			aggrod = false;

		if (maxHealth > health) {
			aggrod = true;
		}
	}

	public void takeDamage(int d) {
		health -= d;
	}

	private void getDirectionToPlayer() {
		if (target.x + target.WIDTH <= x)
			setXDirection(-speed);
		else if (x + WIDTH <= target.x)
			setXDirection(speed);
		else
			setXDirection(0);

		if (target.y >= y + 5)
			setYDirection(speed);
		else if (y >= target.y + 5)
			setYDirection(-speed);
		else
			setYDirection(0);
	}

	private void avoidEnemies() {
		for (int x = 0; x < game.getEnemies().size(); x++) {
			if (game.getEnemies().get(x) != this) {
				if (enemyRect.intersects(game.getEnemies().get(x).enemyRect)) {
					// setXDirection(-xDir);
					// setYDirection(-yDir);
				}
			}
		}
	}

	private void die() {
		target.gainExp(exp);
		target.gainGold(gold);
		isAlive = false;
		game.getEnemies().remove(this);
		// this.delete();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

}
