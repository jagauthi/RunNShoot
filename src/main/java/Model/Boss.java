package Model;

import Controller.GameState;

public class Boss extends Enemy {

	public Boss(int xPos, int yPos, Player p, GameState game) {
		super(xPos, yPos, p, game);

		health = 250;
		exp = 100;
		damage = 30;
		maxHealth = health;
		speed = 7;
		gold = 50;
		WIDTH = 100;
		HEIGHT = 150;
	}

}
