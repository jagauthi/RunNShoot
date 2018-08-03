package Controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Model.Player;
import View.GamePanel;

public class StateMachine {
	// States
	Map<String, IState> states = new HashMap<String, IState>();
	IState currentState = null;
	GameState gameState;
	TitleState titleState;
	HubState hubState;
	
	public static BufferedImage mageSpriteSheet, rogueSpriteSheet, warriorSpriteSheet,
					healthBarNP, manaBarNP, expBarNP, HUDBase, HUDLvlUpImage, barBoundsNP;
	public static Image[] mageSprite, rogueSprite, warriorSprite;
	public static Image enemySprite;

	Player player;

	GamePanel panel;

	public StateMachine(GamePanel panel, String playerInfo) {
		this.panel = panel;
		newGame(playerInfo);
	}

	public void update() {
		currentState.update();
	}

	public void oncePerSecondUpdate() {
		currentState.oncePerSecondUpdate();
	}

	public void render(Graphics g) {
		currentState.render(g);
	}

	public void newGame(String playerInfoString) {
		this.loadImages();
		String[] playerInfo = playerInfoString.split("#");
    	if(playerInfo[1].equals("Mage"))
		{
			player = new Player(this, playerInfoString, mageSpriteSheet);
		} 
    	else if(playerInfo[1].equals("Rogue"))
		{
			player = new Player(this, playerInfoString, rogueSpriteSheet);
		} 
		else
		{
			player = new Player(this, playerInfoString, warriorSpriteSheet);
		} 
		panel.setMouseListenerPlayer(player);
		gameState = new GameState(player, this);
		titleState = new TitleState(player, this);
		hubState = new HubState(player, this);
		states.clear();
		this.add("TitleState", titleState);
		this.add("GameState", gameState);
		this.add("HubState", hubState);
		currentState = gameState;
		currentState.onEnter();
	}
	
	  public void loadImages()
	    {
	    	try {
				mageSpriteSheet = ImageIO.read(new File("resources/Sprites/mageSprite.png"));
				rogueSpriteSheet = ImageIO.read(new File("resources/Sprites/rogueSprite.png"));
				warriorSpriteSheet = ImageIO.read(new File("resources/Sprites/warriorSprite.png"));
				enemySprite = ImageIO.read(new File("resources/Sprites/wolfSprite.png"));
				
				healthBarNP = ImageIO.read(new File("resources/GUI/HUD/health_np_18x18_4x4.png"));
				manaBarNP = ImageIO.read(new File("resources/GUI/HUD/mana_np_18x18_4x4.png"));
				expBarNP = ImageIO.read(new File("resources/GUI/HUD/exp_np_5x5_2x2.png"));
				barBoundsNP = ImageIO.read(new File("resources/GUI/HUD/bar_np_26x26_6x6.png"));
				HUDBase = ImageIO.read(new File("resources/GUI/HUD/HUDbase.png"));
				HUDLvlUpImage = ImageIO.read(new File("resources/GUI/HUD/levelUpBar.png"));
			} 
	    	catch (IOException e) {
				e.printStackTrace();
			}
	    	mageSprite = new Image[4];
	    	rogueSprite = new Image[4];
	    	warriorSprite = new Image[4];
	    	for(int n = 0; n < 4; n++)
	    	{
	    		mageSprite[n] = mageSpriteSheet.getSubimage(40*n, 240, 40, 60);
	    		rogueSprite[n] = rogueSpriteSheet.getSubimage(40*n, 240, 40, 60);
	    		warriorSprite[n] = warriorSpriteSheet.getSubimage(40*n, 240, 40, 60);
	    	}
	    }

	public void change(String stateName) {
		currentState.onExit();
		currentState = states.get(stateName);
	}

	public void add(String name, IState state) {
		states.put(name, state);
	}

	public IState getCurrentState() {
		return currentState;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void changeState(String[] args) {
		if (currentState != null)
			currentState.onExit();

		currentState = states.get(args[0]);

		currentState.onEnter();
	}

	public void addComponent(JComponent comp) {
		panel.add(comp);
	}

	public void doRequestFocus() {
		panel.requestFocus();
	}

	public Player getPlayer() {
		return player;
	}
}
