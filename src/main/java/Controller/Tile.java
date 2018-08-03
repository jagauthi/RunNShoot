package Controller;

import java.awt.Image;

public class Tile {
	
	int x, y, type;
	double randomEncounterChance = 0.0;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	Image tileImage;
	
	public Tile(int newX, int newY, int newType)
	{
		x = newX;
		y = newY;
		type = newType;
		
		if(type == 1)
			randomEncounterChance = 0.0;
		else
			randomEncounterChance = 5.0;
	}

	public int getType()
	{
		return type;
	}
	
	public double getRandomEncounterChance()
	{
		return randomEncounterChance;
	}
}
