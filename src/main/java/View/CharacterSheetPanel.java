package View;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Controller.StateMachine;
import Model.Player;

public class CharacterSheetPanel extends BackgroundedPanel{
	public static final int WIDTH = 3*GamePanel.WIDTH/8;
	public static final int HEIGHT = 3*GamePanel.HEIGHT/4;
	Player player;
	StateMachine sm;
	JLabel charNameClassLab, healthLab, manaLab, pointsLeftLab;
	JTextField pointsLeftField;
	StatField strengthField, dexterityField, constitutionField,
			intelligenceField, willpowerField, luckField;
	JButton spendPointsBut, comitBut;
	boolean levelingUp;
	int startingPoints;
	int[] statValueArray;
	
	
	public CharacterSheetPanel(Player p, StateMachine smIn)
	{
		super(new NinePatchImage(200,200,18,18, Launcher.npBackground));
		this.setLayout(null);
		this.setBounds(GamePanel.WIDTH/2, GamePanel.HEIGHT/8, WIDTH, HEIGHT);
		player = p;
		sm = smIn;
		
		int cornerW = this.getNinePatch().getCornerWidth();
		int cornerH = this.getNinePatch().getCornerHeight();
		Dimension size;
		
        
		charNameClassLab = new JLabel(player.getName()+"  :  Level "+player.getLevel()+" "+player.getPlayerClass());
		size = charNameClassLab.getPreferredSize();
		charNameClassLab.setBounds(cornerW, cornerH, this.getWidth() - (cornerW *2), size.height);
		charNameClassLab.setHorizontalAlignment(JLabel.CENTER);
		
		healthLab = new JLabel("Health: "+player.getHealth()+"/"+player.getMaxHealth());
		healthLab.setHorizontalAlignment(JLabel.CENTER);
		size = healthLab.getPreferredSize();
		healthLab.setBounds(cornerW, cornerH + charNameClassLab.getHeight(), (this.getWidth() - (cornerW *2))/2, size.height);
		
		manaLab = new JLabel("Mana: "+player.getMana()+"/"+player.getMaxMana());
		manaLab.setHorizontalAlignment(JLabel.CENTER);
		size = manaLab.getPreferredSize();
		manaLab.setBounds(cornerW + healthLab.getWidth(), cornerH + charNameClassLab.getHeight(), (this.getWidth() - (cornerW *2))/2, size.height);
		
		//Stat Values {pointsRemaining, Str, Dex, Con, Int, Will, Luck}
		startingPoints = player.getPointsToSpend();
		statValueArray = new int[]{startingPoints, 0, 0, 0, 0, 0, 0};
		StatField[] statFieldArray = new StatField[6];
		
		pointsLeftLab = new JLabel("Points To Spend: ");
		size = pointsLeftLab.getPreferredSize();
		pointsLeftLab.setBounds(cornerW, healthLab.getY() + healthLab.getHeight()+50, size.width, size.height);
		pointsLeftLab.setHorizontalAlignment(JLabel.CENTER);
		
		pointsLeftField  = new JTextField(2);
		pointsLeftField.setHorizontalAlignment(JTextField.CENTER);
		pointsLeftField.setBackground(Launcher.FAKETRANS);
		pointsLeftField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pointsLeftField.setText(String.valueOf(statValueArray[0]));
		pointsLeftField.setEditable(false);
        size = pointsLeftField.getPreferredSize();
        pointsLeftField.setBounds(pointsLeftLab.getX()+pointsLeftLab.getWidth(), pointsLeftLab.getY(), size.width, size.height);
        
        spendPointsBut = new JButton();
        spendPointsBut.setText("Spend Points");
        spendPointsBut.setFont(Launcher.smallFont);
        spendPointsBut.setHorizontalTextPosition(JButton.CENTER);
        spendPointsBut.setBackground(Launcher.TRANSPARENT);
        spendPointsBut.setBorder(Launcher.emptyBorder);
        size = spendPointsBut.getPreferredSize();
        spendPointsBut.setBounds(pointsLeftField.getX()+pointsLeftField.getWidth()+20, pointsLeftField.getY(), size.width+20, size.height+20);
        spendPointsBut.setIcon(new ImageIcon(new NinePatchImage(size.width+20, size.height+20, 7, 7, Launcher.npBasic_50_7).getScaledImage(size.width+20, size.height+20)));
        spendPointsBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLevelingUp(!levelingUp);
            }
        });
        
        comitBut = new JButton();
        comitBut.setText("Save");
        comitBut.setHorizontalTextPosition(JButton.CENTER);
        comitBut.setBackground(Launcher.TRANSPARENT);
        comitBut.setBorder(Launcher.emptyBorder);
        size = comitBut.getPreferredSize();
        comitBut.setBounds(WIDTH - cornerW - size.width - 30, HEIGHT - cornerH - size.height - 30, size.width+20, size.height+20);
        comitBut.setIcon(new ImageIcon(new NinePatchImage(size.width+20, size.height+20, 7, 7, Launcher.npBasic_50_7).getScaledImage(size.width+20, size.height+20)));
        comitBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	spendPoints();
            }
        });
        
        int statSpace = this.getWidth() - cornerW - cornerW - 50;
        
        strengthField = new StatField("Strength", pointsLeftField, statValueArray, statFieldArray, 1, player.getStrength(), StatField.LEVEL_UP);
        size = strengthField.panel.getPreferredSize();
        strengthField.panel.setBounds(statSpace -size.width, pointsLeftLab.getY() + pointsLeftLab.getHeight()+30, size.width+30, size.height-10);
        statFieldArray[0] = strengthField;
        
        dexterityField = new StatField("Dexterity", pointsLeftField, statValueArray, statFieldArray, 2, player.getDexterity(), StatField.LEVEL_UP);
        size = dexterityField.panel.getPreferredSize();
        dexterityField.panel.setBounds(statSpace -size.width, strengthField.panel.getY() + strengthField.panel.getHeight(), size.width+30, size.height-10);
        statFieldArray[1] = dexterityField;
        
        constitutionField = new StatField("Constitution", pointsLeftField, statValueArray, statFieldArray, 3, player.getStamina(), StatField.LEVEL_UP);
        size = constitutionField.panel.getPreferredSize();
        constitutionField.panel.setBounds(statSpace -size.width, dexterityField.panel.getY() + dexterityField.panel.getHeight(), size.width+30, size.height-10);
        statFieldArray[2] = constitutionField;
        
        intelligenceField = new StatField("Intelligence", pointsLeftField, statValueArray, statFieldArray, 4, player.getIntelligence(), StatField.LEVEL_UP);
        size = intelligenceField.panel.getPreferredSize();
        intelligenceField.panel.setBounds(statSpace -size.width, constitutionField.panel.getY() + constitutionField.panel.getHeight(), size.width+30, size.height-10);
        statFieldArray[3] = intelligenceField;
		
		this.add(charNameClassLab);
		this.add(healthLab);
		this.add(manaLab);
		this.add(pointsLeftLab);
		this.add(pointsLeftField);
		this.add(spendPointsBut);
		this.add(strengthField.panel);
		this.add(dexterityField.panel);
		this.add(constitutionField.panel);
		this.add(intelligenceField.panel);
		this.add(willpowerField.panel);
		this.add(luckField.panel);
		this.add(comitBut);
		
		this.setLevelingUp(false);
	}
	
	public void setLevelingUp(boolean b)
	{
		if(b)
        {
            spendPointsBut.setVisible(false);
            spendPointsBut.setEnabled(false);
            levelingUp = true;
        }
        else
        {
        	if(player.getPointsToSpend() != 0)
    		{
    			spendPointsBut.setVisible(true);
    			spendPointsBut.setEnabled(true);
    		}
        	spendPointsBut.setText("Spend Points");
        	statValueArray[0] = player.getPointsToSpend();
        	statValueArray[1] = 0;
        	statValueArray[2] = 0;
        	statValueArray[3] = 0;
        	statValueArray[4] = 0;
        	statValueArray[5] = 0;
        	statValueArray[6] = 0;
        	pointsLeftField.setText(String.valueOf(statValueArray[0]));
        	strengthField.reset();
            dexterityField.reset();
            constitutionField.reset();
            intelligenceField.reset();
            willpowerField.reset();
            luckField.reset();
        	levelingUp = false;
        }
		
		pointsLeftField.setText(String.valueOf(statValueArray[0]));
        comitBut.setVisible(levelingUp);
		comitBut.setEnabled(levelingUp);
        strengthField.showPlus(levelingUp);
        dexterityField.showPlus(levelingUp);
        constitutionField.showPlus(levelingUp);
        intelligenceField.showPlus(levelingUp);
        willpowerField.showPlus(levelingUp);
        luckField.showPlus(levelingUp);
	}
		
	public void spendPoints()
	{
		player.setStrength(player.getStrength() + statValueArray[1]);
		strengthField.setPointValue(player.getStrength());
		player.setDexterity(player.getDexterity() + statValueArray[2]);
		dexterityField.setPointValue(player.getDexterity());
		player.setStamina(player.getStamina() + statValueArray[3]);
		constitutionField.setPointValue(player.getStamina());
		player.setIntelligence(player.getIntelligence() + statValueArray[4]);
		intelligenceField.setPointValue(player.getIntelligence());
		
		player.setPointsToSpend(statValueArray[0]);
    	
    	setLevelingUp(false);
	
	    System.out.println("UPDATECHARINFO#" + player.getAllCharInfo());
	}
}
