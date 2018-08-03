package Controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import Model.Bullet;
import Model.Door;
import Model.HealthPotion;
import Model.Item;
import Model.MachineGun;
import Model.Player;
import Model.Shotgun;
import Model.Weapon;
import View.GamePanel;

public class HubState extends IState {
	boolean started = false;
	Door door;
	ArrayList<Item> items;

	public HubState(Player p, StateMachine s) {
		super(p, s);
		door = new Door(7 * GamePanel.WIDTH / 8, 3 * GamePanel.HEIGHT / 5);
		items = new ArrayList<Item>();

		items.add(new HealthPotion(GamePanel.WIDTH / 4, 2 * GamePanel.HEIGHT / 6, 50));
		items.add(new HealthPotion(2 * GamePanel.WIDTH / 4, 2 * GamePanel.HEIGHT / 6, 50));
		items.add(new HealthPotion(3 * GamePanel.WIDTH / 4, 2 * GamePanel.HEIGHT / 6, 50));
		
		items.add(new MachineGun(GamePanel.WIDTH / 4, GamePanel.HEIGHT / 6, 10, player));
		items.add(new Shotgun(2 * GamePanel.WIDTH / 4, GamePanel.HEIGHT / 6, 10, player));
		items.add(new HealthPotion(3 * GamePanel.WIDTH / 4, GamePanel.HEIGHT / 6, 50));

		audioManager = new AudioManager();
		door.unlock();
	}

	public void update() {
		player.update();
		updateBullets();
		clearExtraBullets();
		checkForWeapons();
		if (player.getRect().intersects(door.getRect())) {
			if (!door.isLocked()) {
				String[] args = { "GameState" };
				sm.changeState(args);
			}
		}
	}

	public void oncePerSecondUpdate() {

	}

	public void render(Graphics g) {
		g.setColor(new Color(0.5f, 0.8f, 1.0f));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		xOffset = player.getX() - GamePanel.WIDTH/2;
		yOffset = player.getY() - GamePanel.HEIGHT/2;
		door.draw(g, xOffset, yOffset);
		player.draw(g);
		drawBullets(g);
		drawItems(g);
		drawPlayerInfo(g);
	}

	public void drawItems(Graphics g) {
		for (int x = 0; x < items.size(); x++) {
			g.setFont(normalfont);
			g.drawImage(items.get(x).getIcon(), items.get(x).getX() - xOffset, items.get(x).getY() - yOffset, null);
			g.setColor(Color.white);
			g.drawString(items.get(x).getCost() + " gp", items.get(x).getX() - xOffset, items.get(x).getY() - yOffset - 10);
		}
	}

	public void checkForWeapons() {
		for (int i = 0; i < items.size(); i++) {
			if (player.getRect().intersects(items.get(i).getRect())) {
				if (player.getGold() >= items.get(i).getCost()) {
					// Player needs to equip the weapon
					player.pickupItem(items.get(i));
					player.loseGold(items.get(i).getCost());
					items.remove(i);
				}
			}
		}
	}

	public void onEnter() {
		player.setXY(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		audioManager.playSong("resources/sounds/Town.wav");
	}

	public void onExit() {
		audioManager.stopSong();
	}

	public void loadInfo(String[] info) {

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
}
