package Controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import Model.Boss;
import Model.Bullet;
import Model.DamageUp;
import Model.Door;
import Model.Enemy;
import Model.Location;
import Model.Player;
import Model.PowerUp;
import Model.SpeedUp;
import View.GamePanel;

import javax.imageio.ImageIO;

public class GameState extends IState {
	ArrayList<Enemy> enemies;
	ArrayList<PowerUp> powerups;
	ArrayList<Location> locations;
	BufferedImage mapImage;
	boolean started = false;
	Door door;
	int stage = 0;

	public GameState(Player p, StateMachine s) {
		super(p, s);
		xScale = 7;
		yScale = 7;
		locations = new ArrayList<Location>();
		getLocations();
		audioManager = new AudioManager();

		String mapLocation = "resources/maps/mtStart2.png";
		mapImage = null;

		try {
			mapImage = ImageIO.read(new File(mapLocation));
		}
		catch (IOException ioe) {
			System.out.println("Unable to load image file.");
		}
		enemies = new ArrayList<Enemy>();
		powerups = new ArrayList<PowerUp>();
	}

	public void update() {
		player.update();
		checkForPowerup();
		updateBullets();
		updateEnemies();
		//clearExtraBullets();

		if (enemies.size() == 0 && door.isLocked()) {
			door.unlock();
		}

		if (player.getRect().intersects(door.getRect())) {
			if (!door.isLocked()) {
				String[] args = { "HubState" };
				sm.changeState(args);
			}
		}
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void oncePerSecondUpdate() {

	}

	public void getLocations() {
		door = new Door(1800, 1400);
	}

	public void render(Graphics g) {
		g.setColor(new Color(0.5f, 0.5f, 0.9f));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		xOffset = player.getX() - GamePanel.WIDTH/2;
		yOffset = player.getY() - GamePanel.HEIGHT/2;
		g.drawImage(mapImage, -xOffset, -yOffset, GamePanel.WIDTH*xScale, GamePanel.HEIGHT*yScale, null);
		door.draw(g, xOffset, yOffset);
		player.draw(g);
		drawEnemies(g);
		drawBullets(g);
		drawPowerups(g);
		drawPlayerInfo(g);
	}

	public void drawEnemies(Graphics g) {
		g.setColor(Color.RED);
		for (int x = 0; x < enemies.size(); x++) {
			g.fillRect(enemies.get(x).getX()-xOffset, enemies.get(x).getY()-yOffset, enemies.get(x).getWidth(),
					enemies.get(x).getHeight());
			g.drawImage(enemies.get(x).getImage(), enemies.get(x).getX()-xOffset, enemies.get(x).getY()-yOffset, null);
		}
	}

	public void drawBullets(Graphics g) {
		g.setColor(Color.BLACK);
		for (int x = 0; x < bullets.size(); x++) {
			g.fillRect(bullets.get(x).getX()-xOffset, bullets.get(x).getY()-yOffset, Bullet.getWidth(), Bullet.getHeight());
		}
	}

	public void drawPowerups(Graphics g) {
		for (int x = 0; x < powerups.size(); x++) {
			g.drawImage(powerups.get(x).getIcon(), powerups.get(x).getX()-xOffset, powerups.get(x).getY()-yOffset, null);
		}
	}

	public void onEnter() {
		audioManager.playSong("resources/sounds/SnowZone.wav");
		player.setXY(1400, 1240);
		stage++;
		door.lock();

		if (stage % 5 != 0) {
			for (int x = 0; x < stage; x++) {
				createRandomEnemy();
			}
		} else {
			spawnBoss();
		}

		if (Math.random() < 0.8)
			createRandomPowerup();
	}

	public void onExit() {
		audioManager.stopSong();
		bullets.clear();
	}

	public void loadInfo(String[] info) {

	}

	public void spawnBoss() {

		enemies.add(new Boss(3 * GamePanel.WIDTH / 4, GamePanel.HEIGHT / 2, player, this));
	}

	public void createRandomEnemy() {
		Random generator = new Random();

		int randNum = generator.nextInt(GamePanel.WIDTH - 50);
		int newX = player.getX() - (randNum - GamePanel.WIDTH/2);
		int xDistance = Math.abs(player.getX() - newX);
		while (xDistance < 150) {
			randNum = generator.nextInt(GamePanel.WIDTH - 50);
			newX = player.getX() - (randNum - GamePanel.WIDTH/2);
			xDistance = Math.abs(player.getX() - newX);
		}

		randNum = generator.nextInt(GamePanel.HEIGHT - GamePanel.HEIGHT/6 - 30);
		int newY = player.getY() - (randNum - GamePanel.HEIGHT/2);
		int yDistance = Math.abs(player.getY() - newY);

		while (yDistance < 150) {
			randNum = generator.nextInt(GamePanel.HEIGHT - GamePanel.HEIGHT / 6 - 30);
			newY = player.getY() - (randNum - GamePanel.HEIGHT/2);
			yDistance = Math.abs(player.getY() - newY);
		}
		enemies.add(new Enemy(newX, newY, player, this));
	}

	public void createRandomPowerup() {
		Random generator = new Random();
		int newX = generator.nextInt(GamePanel.WIDTH - 50);
		int xDistance = Math.abs(player.getX() - newX);
		while (xDistance < 150) {
			newX = generator.nextInt(GamePanel.WIDTH - 50);
			xDistance = Math.abs(player.getX() - newX);
		}

		int newY = generator.nextInt(GamePanel.HEIGHT - GamePanel.HEIGHT / 6 - 30);
		int yDistance = Math.abs(player.getX() - newY);
		while (yDistance < 150) {
			newY = generator.nextInt(GamePanel.HEIGHT - GamePanel.HEIGHT / 6 - 30);
			yDistance = Math.abs(player.getX() - newY);
		}
		if (Math.random() > 0.5)
			powerups.add(new SpeedUp(newX, newY, 8, 1000000));
		else
			powerups.add(new DamageUp(newX, newY, 8, 1000000));
	}

	public void updateEnemies() {
		for (int x = 0; x < enemies.size(); x++) {
			enemies.get(x).update();
		}
	}

	public void clearExtraBullets() {
		for (int x = 0; x < bullets.size(); x++) {
			Bullet curBullet = bullets.get(x);
			if (curBullet.isOutsideOfBounds()) {
				bullets.remove(x);
				x--;
			}
		}
	}

	public void checkForPowerup() {
		for (int i = 0; i < powerups.size(); i++) {
			if (player.getRect().intersects(powerups.get(i).getRect())) {
				powerups.get(i).use(player);
				powerups.remove(i);
			}
		}
	}

	public void newGame() {
		System.out.println("Oh no nothing happens when this happens :( :O :D");
	}

	public void clear() {
		while (enemies.size() > 0) {
			enemies.remove(0);
		}
	}

	public void drawLocations(Graphics g)
	{
		if(locations.size() > 0)
		{
			g.setColor(Color.magenta);
			for(int i = 0; i < locations.size(); i++)
			{
//    			g.fillRect(locations.get(i).getX(), locations.get(i).getY(),
//    					Location.WIDTH, Location.HEIGHT);
			}
		}
	}

}
