package Controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Model.Bullet;
import Model.Player;
import View.GamePanel;

public class IState {
	Player player;
	StateMachine sm;
	ArrayList<Bullet> bullets;
	Font normalfont = new Font("Arial", Font.BOLD, 20);
	Font bigfont = new Font("Arial", Font.BOLD, 40);
	protected int xScale, yScale, xOffset, yOffset;
	AudioManager audioManager;

	public IState(Player p, StateMachine s) {
		player = p;
		sm = s;
		bullets = new ArrayList<Bullet>();
	}

	public void loadMap() {

	}

	public void update() {

	}

	public void drawBullets(Graphics g) {
		g.setColor(Color.BLACK);
		for (int x = 0; x < bullets.size(); x++) {
			g.fillRect(bullets.get(x).getX() - xOffset, bullets.get(x).getY() - yOffset, Bullet.getWidth(), Bullet.getHeight());
		}
	}

	public void updateBullets() {
		for (int x = 0; x < bullets.size(); x++) {
			bullets.get(x).update();
		}
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void addBullet(Bullet bul) {
		bullets.add(bul);
	}

	public void oncePerSecondUpdate() {

	}

	public void calculateRandomEncounterChance() {

	}

	public void render(Graphics g) {

	}

	public void drawPlayerInfo(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 5 * GamePanel.HEIGHT / 6, GamePanel.WIDTH, GamePanel.HEIGHT / 6);
		g.setFont(normalfont);
		g.setColor(Color.BLACK);
		g.drawString("Gold: " + player.getGold() + " gp", GamePanel.WIDTH / 4, 5 * GamePanel.HEIGHT / 6 + 30);
		g.drawString("Weapon Damage: " + player.getWeapon().getDamage(), GamePanel.WIDTH / 4,
				5 * GamePanel.HEIGHT / 6 + 60);
		g.drawString("Powerup Duration: " + player.getPowerupDurationLeft(), GamePanel.WIDTH / 4,
				5 * GamePanel.HEIGHT / 6 + 90);
		g.drawString("Ammo: " + player.getWeapon().getAmmo() + "/" + player.getWeapon().getMaxAmmo(),
				GamePanel.WIDTH / 4, 5 * GamePanel.HEIGHT / 6 + 120);

		g.drawString("Stage: " + sm.getGameState().stage, GamePanel.WIDTH / 2, 5 * GamePanel.HEIGHT / 6 + 30);
		g.drawString("Current Weapon: " + sm.getPlayer().getWeapon().getName(), GamePanel.WIDTH / 2, 5 * GamePanel.HEIGHT / 6 + 60);

		float currentHealth = (float) player.getHealth() / (float) player.getMaxHealth();
		float currentExp = (float) player.getExp() / (float) player.getXPToNextLevel();

		// White bar backgrounds
		g.setColor(Color.white);
		g.fillRect(GamePanel.WIDTH / 64 - 2, 5 * GamePanel.HEIGHT / 6 + 15, 204, 34); // Back Health bar
		g.fillRect(GamePanel.WIDTH / 64 - 2, 5 * GamePanel.HEIGHT / 6 + 54, 204, 14); // Back Experience bar

		// Red Health bar
		g.setColor(Color.red);
		g.fillRect(GamePanel.WIDTH / 64, 5 * GamePanel.HEIGHT / 6 + 17, (int) (currentHealth * 200), 30);

		// Green experience bar
		g.setColor(Color.green);
		g.fillRect(GamePanel.WIDTH / 64, 5 * GamePanel.HEIGHT / 6 + 56, (int) (currentExp * 200), 10);
	}

	public int getXScale() {
		return xScale;
	}

	public int getYScale() {
		return yScale;
	}

	public void onEnter() {

	}

	public void onExit() {

	}


	public void keyPressed(int keyCode) {

		if (keyCode == KeyEvent.VK_W) {
			player.setYDirection(-player.getSpeed());
			player.moveUp();
		}

		if (keyCode == KeyEvent.VK_A) {
			player.setXDirection(-player.getSpeed());
			player.moveLeft();
		}

		if (keyCode == KeyEvent.VK_S) {
			player.setYDirection(player.getSpeed());
			player.moveDown();
		}

		if (keyCode == KeyEvent.VK_D) {
			player.setXDirection(player.getSpeed());
			player.moveRight();
		}

		if (keyCode == KeyEvent.VK_C) {
			player.getInventory().setOpen(!player.getInventory().isOpen());
		}

		if (keyCode == KeyEvent.VK_G) {
			player.gainGold(100);
		}

		if (keyCode == KeyEvent.VK_LEFT) {
			if (player.getWeapon() != null) {
				player.punch(1);
			}
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			if (player.getWeapon() != null) {
				player.punch(2);
			}
		}

		if (keyCode == KeyEvent.VK_UP) {
			if (player.getWeapon() != null) {
				player.punch(3);
			}
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			if (player.getWeapon() != null) {
				player.punch(4);
			}
		}
	}

	public void keyReleased(int keyCode) {

		if (keyCode == KeyEvent.VK_W) {
			player.setYDirection(0);
		}

		if (keyCode == KeyEvent.VK_A) {
			player.setXDirection(0);
		}

		if (keyCode == KeyEvent.VK_S) {
			player.setYDirection(0);
		}

		if (keyCode == KeyEvent.VK_D) {
			player.setXDirection(0);
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setFist(new Rectangle(0, 0, 0, 0));
		}

		if (keyCode == KeyEvent.VK_LEFT) {
			player.setFist(new Rectangle(0, 0, 0, 0));
		}

		if (keyCode == KeyEvent.VK_UP) {
			player.setFist(new Rectangle(0, 0, 0, 0));
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			player.setFist(new Rectangle(0, 0, 0, 0));
		}
		/*
    	if(keyCode == KeyEvent.VK_C){
    		if(!charSheetOpen)
    			openCharacterSheet();
    		else
    			closeCharacterSheet();
    	}
    	if(keyCode == KeyEvent.VK_ESCAPE){
    		if(!optionsMenuOpen)
    			openOptionsMenu();
    		else
    			closeOptionsMenu();
		}
		*/
	}

	public void loadInfo(String[] args) {

	}
}
