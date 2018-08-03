package Controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Model.Player;
import View.CharacterSelection;
import View.GamePanel;

public class TitleState extends IState {
	boolean started = false;
	Font normalfont = new Font("Arial", Font.BOLD, 20);
	Font bigfont = new Font("Arial", Font.BOLD, 40);
	CharacterSelection charSelect;

	public TitleState(Player p, StateMachine s) {
		super(p, s);
		audioManager = new AudioManager();
		System.out.println("Connecting to database...");
		String[] chars = {"Anone", "Hemofeliac", "Teh", "Delete"};
		charSelect = new CharacterSelection(GamePanel.WIDTH, GamePanel.HEIGHT, s, chars);
		//charSelect.setOpen(true);
		sm.addComponent(charSelect);
	}

	public void update() {

	}

	public void oncePerSecondUpdate() {

	}

	public void render(Graphics g) {
		g.setFont(bigfont);
		g.setColor(Color.BLACK);
		g.drawString("Controls: WASD to move", 400, 200);
		g.drawString("Left Click in a direction to shoot", 400, 280);
		g.drawString("Right Click to reload", 400, 360);
		g.drawString("Press c to open inventory", 400, 440);
		g.drawString("Press space to start", 400, 620);
	}

	public void onEnter() {
		audioManager.playSong("resources/sounds/LauncherMusic.wav");
	}

	public void onExit() {
		audioManager.stopSong();
	}

	public void loadInfo(String[] info) {
		
	}

	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_W) {

		}
		if (keyCode == KeyEvent.VK_A) {

		}
		if (keyCode == KeyEvent.VK_S) {

		}
		if (keyCode == KeyEvent.VK_D) {

		}
		if (keyCode == KeyEvent.VK_SPACE) {
			String[] args = { "GameState" };
			sm.changeState(args);
		}
	}

	public void keyReleased(int keyCode) {
		if (keyCode == KeyEvent.VK_W) {

		}
		if (keyCode == KeyEvent.VK_A) {

		}
		if (keyCode == KeyEvent.VK_S) {

		}
		if (keyCode == KeyEvent.VK_D) {

		}
	}
}
