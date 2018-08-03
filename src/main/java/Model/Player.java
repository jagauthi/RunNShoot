package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;

import Controller.StateMachine;
import Controller.Tile;
import Controller.Animation;
import View.GamePanel;

public class Player {

	int x, y;
	private int xDir, yDir;
	private int health, mana, exp, level, speed, gold, pointsToSpend;
	private int stamina, strength, dexterity, intelligence;
	private String name, playerClass, location;

	final int WIDTH = 20;
	final int HEIGHT = 30;
	final int SCALE = 2;
	private final int BASE_SPEED = 7;
	private Boolean moving;
	
	private Animation animation;
	private BufferedImage spriteSheet;
	private ArrayList<BufferedImage[]> sprites;
	private boolean spritesLoaded = false;
	private final int numFrames[] = {4, 4, 4, 4, 4};
	private Color eyeColor, eyeColor2;
	
	private int currentAnimation;
	private int direction;
	
	private static final int MOVINGUP = 0;
	private static final int MOVINGLEFT = 1;
	private static final int MOVINGDOWN = 2;
	private static final int MOVINGRIGHT = 3;
	private static final int IDLE = 4;
	private static final int DELAY = 125;
	
	
	private int[] idleOffset = {0,0,0,0};
	private int[] movingUpOffsetX = {0, 0, 0, 0};
	private int[] movingUpOffsetY = {-Tile.HEIGHT/4, -Tile.HEIGHT/2, -(3*Tile.HEIGHT)/4, -Tile.HEIGHT};
	private int[] movingLeftOffsetX = {-Tile.WIDTH/4, -Tile.WIDTH/2, -(3*Tile.WIDTH)/4, -Tile.WIDTH};
	private int[] movingLeftOffsetY = {0, 0, 0, 0};
	private int[] movingDownOffsetX = {0, 0, 0, 0};
	private int[] movingDownOffsetY = {Tile.HEIGHT/4, Tile.HEIGHT/2, (3*Tile.HEIGHT)/4, Tile.HEIGHT};
	private int[] movingRightOffsetX = {Tile.WIDTH/4, Tile.WIDTH/2, (3*Tile.WIDTH)/4, Tile.WIDTH};
	private int[] movingRightOffsetY = {0, 0, 0, 0};

	private boolean isInvulnerable, shooting;
	private long tookDamageTime, pDuration, powerupStartTime, powerupDurationLeft = 0;
	private Rectangle playerRect, fist;

	private Inventory inventory;
	
	Ellipse2D.Float range;
	int rangeDistance = 150;

	Weapon weapon;
	StateMachine sm;
	
	public Player(StateMachine sm, String playerInfoString, BufferedImage spriteIn) 
	{
		this.sm = sm;
		loadInfo(playerInfoString);
		speed = BASE_SPEED;
		playerRect = new Rectangle(x, y, WIDTH, HEIGHT);
		inventory = new Inventory(400, 500, sm);
		isInvulnerable = false;
		fist = new Rectangle(0, 0);

		Pistol startPistol = new Pistol(0, 0, 10, this);
		inventory.addItem(startPistol);
		weapon = startPistol;

		moving = false;
		spritesLoaded = false;
		animation = new Animation();
		spriteSheet = spriteIn;
		this.sliceSprites();
		range = new Ellipse2D.Float(x, y, rangeDistance, rangeDistance);
	}
	
	public void loadInfo(String playerInfoString) {
		String playerInfo[] = playerInfoString.split("#");
		
		name = playerInfo[0];
		playerClass = playerInfo[1];
		level = Integer.parseInt(playerInfo[2]);
		health = Integer.parseInt(playerInfo[3]);
		mana = Integer.parseInt(playerInfo[4]);
		exp = Integer.parseInt(playerInfo[5]);
		pointsToSpend = Integer.parseInt(playerInfo[6]);
		x = Integer.parseInt(playerInfo[7]);
		y = Integer.parseInt(playerInfo[8]);
		location = playerInfo[9];
		gold = Integer.parseInt(playerInfo[10]);
		
		strength = Integer.parseInt(playerInfo[11]);
		dexterity = Integer.parseInt(playerInfo[12]);
		stamina = Integer.parseInt(playerInfo[13]);
		intelligence = Integer.parseInt(playerInfo[14]);
	}
	
	public void update() {
		//detectEdges();
		animationUpdate();
		if(moving & animation.getHasPlayedOnce()){
			updateOncePerMove();
		}
		move(xDir, yDir);
		updateGun();
		checkForLevel();
		checkCanTakeDamage();
		checkPowerupDuration();
		range.setFrame(x-rangeDistance/2, y-rangeDistance/2, rangeDistance, rangeDistance);
		playerRect.setBounds(x, y, WIDTH, HEIGHT);
	}
	
	public void updateOncePerMove()
	{
		currentAnimation = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setCurrentFrame(direction);
		animation.setOffsetX(idleOffset);
		animation.setOffsetY(idleOffset);
		animation.setDelay(-1);
		animation.resetHasPlayedOnce();
		if(direction == MOVINGUP){
			//y -= speed;
		} else if (direction == MOVINGLEFT){
			//x -= speed;
		} else if (direction == MOVINGDOWN){
			//y += speed;
		} else if (direction == MOVINGRIGHT){
			//x += speed;
		}
		moving = false;
		System.out.println("Sending 'server' my location lol");
		sm.getCurrentState().calculateRandomEncounterChance();
	}
	public void move(int xDir, int yDir) {
		x += xDir;
		y += yDir;
	}

	public void animationUpdate(){
		animation.update();
	}
	
	public void draw(Graphics g) {
		if(spritesLoaded){
			int x = GamePanel.WIDTH/2 - (SCALE * this.WIDTH/2);
			int y = GamePanel.HEIGHT/2 - (SCALE * this.WIDTH/2) - SCALE*(this.HEIGHT - this.WIDTH);
			g.setColor(Color.black);
			g.fillOval(x-55, y-45, Math.round(range.width), Math.round(range.height));
			g.drawImage(animation.getImage(), x, y, null);
			drawEyes(g);
		}
		else {
			g.setColor(Color.black);
			//Just drawing the players box to be more similar to how the sprite will look.
			g.fillRect(x, y, WIDTH*SCALE, 50);
		}
	}
	
	public void drawEyes(Graphics g){
		if(eyeColor == null){
			return;
		}
		else if(currentAnimation == IDLE | currentAnimation == MOVINGDOWN)
		{
			g.setColor(eyeColor);
			g.fillRect(x+(6*SCALE), y+(8*SCALE), 2, 2);
			g.fillRect(x+(9*SCALE), y+(8*SCALE), 2, 2);
			g.setColor(eyeColor2);
			g.fillRect(x+(6*SCALE), y+(7*SCALE), 2, 2);
			g.fillRect(x+(9*SCALE), y+(7*SCALE), 2, 2);
		} 
		else if (currentAnimation == MOVINGLEFT)
		{
			g.setColor(eyeColor);
			g.fillRect(x+(4*SCALE), y+(8*SCALE), 2, 2);
			g.setColor(eyeColor2);
			g.fillRect(x+(4*SCALE), y+(7*SCALE), 2, 2);
		} 
		else if (currentAnimation == MOVINGRIGHT)
		{
			g.setColor(eyeColor);
			g.fillRect(x+(11*SCALE), y+(8*SCALE), 2, 2);
			g.setColor(eyeColor2);
			g.fillRect(x+(11*SCALE), y+(7*SCALE), 2, 2);
		}
	}
	
	private boolean sliceSprites(){
		sprites = new ArrayList<BufferedImage[]>();
		try{
//			Dynamic Armor Stuff
//			if(filePath == null){
//				//Load each of the armor pieces if not specified a sprite page
//				//For now this is hard coded but file path will depend on how we do inventory
//				BufferedImage head = ImageIO.read(new File("resources/Head/head.gif"));
//				BufferedImage arms = ImageIO.read(new File("resources/Arms/arms.gif"));
//				BufferedImage chest = ImageIO.read(new File("resources/Chest/chest.gif"));
//				BufferedImage legs = ImageIO.read(new File("resources/Legs/legs.gif"));
//				BufferedImage feet = ImageIO.read(new File("resources/Feet/feet.gif"));
//				
//				//Combine each of of the armor pieces into one image
//				spriteSheet = new BufferedImage(head.getWidth(), head.getHeight(), BufferedImage.TYPE_INT_ARGB);
//				Graphics g = spriteSheet.getGraphics();
//				g.drawImage(head, 0, 0, null);
//				g.drawImage(arms, 0, 0, null);
//				g.drawImage(chest, 0, 0, null);
//				g.drawImage(legs, 0, 0, null);
//				g.drawImage(feet, 0, 0, null);
//			} else {
//				//Load spriteSheet if given one
//				spriteSheet = ImageIO.read(new File(filePath));
//			}
			
			
			//Slice the spriteSheet into arrays of frames for animations
			for( int n = 0; n < numFrames.length; n++)
			{
				BufferedImage[] temp = new BufferedImage[numFrames[n]];
				for(int m = 0; m < numFrames[n]; m++)
				{
					temp[m] = spriteSheet.getSubimage(WIDTH * m * SCALE, HEIGHT * n * SCALE, WIDTH * SCALE, HEIGHT * SCALE);
				}
				sprites.add(temp);
			}
			
			//default to IDEL animation
			currentAnimation = IDLE;
			animation.setFrames(sprites.get(IDLE));
			animation.setCurrentFrame(MOVINGDOWN);
			animation.setDelay(-1);
			animation.setOffsetX(idleOffset);
			animation.setOffsetY(idleOffset);
			
			spritesLoaded = true;
		
		} catch(Exception e){
			e.printStackTrace();
			spritesLoaded =  false;
		}
		return spritesLoaded;
		
	}
	
	public String getAllCharInfo()
	{
		return name + "#" + playerClass + "#" + level + "#" + health + "#" + mana + "#" + strength + "#" + dexterity + "#" + 
				stamina + "#" + intelligence + "#" + pointsToSpend + "#" +
				x + "#" + y + "#" + location;
	}

	public boolean isAlive() {
		if (health > 0)
			return true;
		else
			return false;
	}
	
	public StateMachine getStateMachine() {
		return sm;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getPlayerClass() {
		return playerClass;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getMana() {
		return mana;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	
	public int getPointsToSpend() {
		return pointsToSpend;
	}
	
	
	public void setPointsToSpend(int n)
	{
		pointsToSpend = n;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setFist(Rectangle f) {
		fist = f;
	}

	public Rectangle getRect() {
		return playerRect;
	}

	public int getGold() {
		return gold;
	}

	public void loseGold(int i) {
		gold -= i;
	}

	public int getHealth() {
		return health;
	}

	public int getExp() {
		return exp;
	}

	public void buffDamage(int addDamage, long duration) {
		weapon.setDamage(weapon.getDamage() + addDamage);
		powerupDurationLeft = duration;
		powerupStartTime = System.currentTimeMillis();
	}

	public void buffSpeed(int addSpeed, long duration) {
		speed += addSpeed;
		powerupDurationLeft = duration;
		powerupStartTime = System.currentTimeMillis();
	}

	public void updateGun() {
		weapon.update();
	}

	public boolean isShooting() {
		return shooting;
	}
	
	public void attack(int xDir, int yDir) {
		if(playerClass.equals("Warrior")) {
			swingSword(xDir, yDir);
		}
		else {
			setShooting(xDir, yDir);
		}
	}
	
	public void swingSword(int xDir, int yDir) {
		for(Enemy e : sm.getGameState().getEnemies()) {
			if(range.contains(e.getX(), e.getY()) 
				|| range.contains(e.getX()+e.WIDTH, e.getY()) 
				|| range.contains(e.getX(), e.getY()+e.HEIGHT) 
				|| range.contains(e.getX()+e.WIDTH, e.getY()+e.WIDTH)
			){
				e.takeDamage(50);
			}
		}
	}

	public void setShooting(int xDir, int yDir) {
		shooting = true;
		float finalXDir, finalYDir;
		float ratio;
		finalXDir = xDir - (GamePanel.WIDTH/2 + WIDTH / 2);
		finalYDir = yDir - (GamePanel.HEIGHT/2 + HEIGHT / 2);
		if (finalXDir > finalYDir) {
			if (Math.abs(finalXDir) > Math.abs(finalYDir)) {
				ratio = finalYDir / finalXDir;
				finalXDir = 30.0f;
				finalYDir = ratio * 30.0f;
			} else {
				ratio = finalXDir / finalYDir;
				finalYDir = -30.0f;
				finalXDir = ratio * 30.0f * -1;
			}
		} else {
			if (Math.abs(finalXDir) > Math.abs(finalYDir)) {
				ratio = finalYDir / finalXDir;
				finalXDir = -30.0f;
				finalYDir = 30.0f * ratio * -1;
			} else {
				ratio = finalXDir / finalYDir;
				finalYDir = 30.0f;
				finalXDir = ratio * 30.0f;
			}
		}
		weapon.setXYDir((int) finalXDir, (int) finalYDir);
		weapon.shooting = true;
	}

	public void setNotShooting() {
		weapon.stopUse();
		shooting = false;
	}

	public void setXDirection(int newX) {
		xDir = newX;
	}

	public void setYDirection(int newY) {
		yDir = newY;
	}

	public void moveUp(){
		if(moving == false & currentAnimation == IDLE){
			moving = true;
			currentAnimation = MOVINGUP;
			direction = MOVINGUP;
			animation.setFrames(sprites.get(MOVINGUP));
			animation.setDelay(DELAY);
			animation.setOffsetX(movingUpOffsetX);
			animation.setOffsetY(movingUpOffsetY);
		}
	}
	
	public void moveDown(){
		if(moving == false & currentAnimation == IDLE){
			moving = true;
			currentAnimation = MOVINGDOWN;
			direction = MOVINGDOWN;
			animation.setFrames(sprites.get(MOVINGDOWN));
			animation.setDelay(DELAY);
			animation.setOffsetX(movingDownOffsetX);
			animation.setOffsetY(movingDownOffsetY);
		}
	}
	
	public void moveLeft(){
		if(moving == false & currentAnimation == IDLE){
			moving = true;
			currentAnimation = MOVINGLEFT;
			direction = MOVINGLEFT;
			animation.setFrames(sprites.get(MOVINGLEFT));
			animation.setDelay(DELAY);
			animation.setOffsetX(movingLeftOffsetX);
			animation.setOffsetY(movingLeftOffsetY);
		}
	}
	
	public void moveRight(){
		if(moving == false & currentAnimation == IDLE){
			moving = true;
			currentAnimation = MOVINGRIGHT;
			direction = MOVINGRIGHT;
			animation.setFrames(sprites.get(MOVINGRIGHT));
			animation.setDelay(DELAY);
			animation.setOffsetX(movingRightOffsetX);
			animation.setOffsetY(movingRightOffsetY);
		}
	}

	public int getSpeed() {
		return speed;
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

	public void useWeapon(int xDir, int yDir) {

	}

	public void stopUseWeapon() {
		weapon.stopUse();
	}

	public void setWeapon(Weapon wep) {
		weapon = wep;
		weapon.setWielder(this);
	}

	public void reload() {
		weapon.reload();
	}

	public void checkCanTakeDamage() {
		if (System.currentTimeMillis() - tookDamageTime > 100) {
			isInvulnerable = false;
		}
	}

	public void checkIfHit() {
		for (int x = 0; x < sm.getGameState().getEnemies().size(); x++) {
			if (sm.getGameState().getEnemies().get(x).enemyRect.intersects(playerRect)) {
				takeDamage(sm.getGameState().getEnemies().get(x).damage);
			}
		}
	}

	public void checkPowerupDuration() {
		if (((powerupDurationLeft / 100000) - (int) (System.currentTimeMillis() - powerupStartTime) / 1000) > 0) {
			// System.out.println((powerupDurationLeft/100000) -
			// (int)(System.currentTimeMillis() - powerupStartTime)/1000);
		} else {
			// System.out.println("That's it");
			powerupDurationLeft = 0;
			speed = BASE_SPEED;
			weapon.setDamage(weapon.damage);
		}
	}

	public int getPowerupDurationLeft() {
		int duration = (int) ((powerupDurationLeft / 100000)
				- (int) (System.currentTimeMillis() - powerupStartTime) / 1000);
		if (duration > 0 && duration < 100)
			return duration;
		else
			return 0;
	}

	public void takeDamage(int d) {
		if (!isInvulnerable) {
			health -= d;
			if(health <= 0)
				die();
			isInvulnerable = true;
			tookDamageTime = System.currentTimeMillis();
		}
	}

	public void gainHealth(int h) {
		health += h;
		if(health > getMaxHealth())
		{
			health = getMaxHealth();
		}
	}
	
	public void gainMana(int m) {
		System.out.println("Unimplemented atm :)");
	}

	public void gainGold(int g) {
		gold += g;
	}

	public void gainExp(int e) {
		exp += e;
	}

	public void checkForLevel() {
		if (exp >= getXPToNextLevel()) {
			exp = 0;
			levelUp();
		}
	}

	public void levelUp() {
		level++;
		health = getMaxHealth();
	}

	public void die() {
		System.out.println("Oh no I died and I'm not handling it in any way :(");
	}

	public void punch(int dir) {
		if (dir == 1) // Left
		{
			fist = new Rectangle(x - 25, y + HEIGHT / 4, 20, 20);
		}
		if (dir == 2) // Right
		{
			fist = new Rectangle(x + WIDTH + 5, y + HEIGHT / 4, 20, 20);
		}
		if (dir == 3) // Up
		{
			fist = new Rectangle(x, y - 25, 20, 20);
		}
		if (dir == 4) // Down
		{
			fist = new Rectangle(x, y + HEIGHT + 5, 20, 20);
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public int getMaxHealth()
	{
		/*
		if(playerClass.equals("Mage"))
			return (20*level) + stamina;
		else if(playerClass.equals("Rogue"))
			return (int) ((20*level) + stamina * 1.5);
		else
			return (20*level) + stamina * 2;
			*/
		return (10*level) + (10*stamina);
	}
	
	public int getMaxMana()
	{
		if(playerClass.equals("Warrior"))
			return intelligence;
		else if(playerClass.equals("Rogue"))
			return intelligence * 2;
		else
			return intelligence * 3;
	}

	public int getXPToNextLevel() {
		return 100 + level * 50;
	}

	public void pickupItem(Item i) {
		if(!inventory.addItem(i)) {
			System.out.println("Inventory is full!");
		}
		else {
			if(i instanceof Weapon)
				weapon = (Weapon) i;
		}
	}
}
