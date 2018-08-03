package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StatField {

	public static final int NEW = 0;
	public static final int IN_GAME = 1;
	public static final int LEVEL_UP = 2;
	public JPanel panel;
	
	private JButton minus;
	private JButton plus;
	private JTextField points, pointsToAdd, pointsLeft;
	private JLabel label;
	private ImageIcon plusIcon, minusIcon;
	private StatField[] otherFields;
	private int[] valueArray;
	private int index, startValue, type;
	
	public StatField(String name, JTextField pointsLeftIn, int[] values, StatField[] othersIn, int indexin, int start, int typeIn)
	{
		valueArray = values;
		index = indexin;
		startValue = start;
		pointsLeft = pointsLeftIn;
		otherFields = othersIn;
		type = typeIn;
		
		panel = new JPanel();
		panel.setBackground(Launcher.FAKETRANS);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;    
        c.anchor = GridBagConstraints.EAST;
		
        minusIcon = new ImageIcon(Launcher.minusIcon);
        plusIcon = new ImageIcon(Launcher.plusIcon);
        
		minus = new JButton();
		minus.setBackground(Launcher.FAKETRANS);
		minus.setBorder(Launcher.emptyBorder);
		minus.setMinimumSize(new Dimension(minusIcon.getIconWidth(), minusIcon.getIconHeight()));
//		minus.setBounds(0,0,minus.getWidth(), minus.getHeight());
        minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusStat(evt);
            }
        });
        
        points = new JTextField(2);
        points.setEditable(false);
        points.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        points.setBackground(Launcher.FIELDGRAY);
		points.setMinimumSize(new Dimension(50,30));
		points.setHorizontalAlignment(JTextField.CENTER);
		
		pointsToAdd = new JTextField(3);
		pointsToAdd.setText("+0");
		pointsToAdd.setEditable(false);
		pointsToAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		pointsToAdd.setBackground(Launcher.FIELDGRAY);
		pointsToAdd.setMinimumSize(new Dimension(50,30));
		pointsToAdd.setHorizontalAlignment(JTextField.CENTER);
//		points.setBounds(minus.getWidth() + 10, 0, points.getWidth(), points.getHeight());
        
        plus = new JButton();
        
        plus.setBackground(Launcher.TRANSPARENT);
        plus.setBorder(Launcher.emptyBorder);
        plus.setMinimumSize(new Dimension(plusIcon.getIconWidth(), plusIcon.getIconHeight()));
//        plus.setBounds(minus.getWidth() + points.getWidth() + 20, 0, plus.getWidth(), plus.getHeight());
        plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusStat(evt);
            }
        });
        
        label = new JLabel();
		label.setText(name);  
		//label.setMinimumSize(new Dimension(170, plusIcon.getIconHeight()));
		
		if(type == NEW)
		{
			plus.setIcon(plusIcon);
			minus.setIcon(minusIcon);
			points.setText(String.valueOf(valueArray[index]));
			
			c.insets = new Insets(0,0,10,10);
			c.anchor = c.EAST;
			
		    c.gridx = 0;
		    c.gridy = 0;
		    panel.add(minus, c);
		    
		    c.gridx = 1;
		    c.gridy = 0;
		    panel.add(points, c);
		    
		    c.gridx = 2;
		    c.gridy = 0;
		    panel.add(plus, c);
		    
		    c.gridx = 3;
		    c.gridy = 0;
		    panel.add(label, c);
		}
		else
		{
			plus.setIcon(plusIcon);
			minus.setIcon(null);
			points.setText(String.valueOf(start));
			
			c.insets = new Insets(0,0,10,10);
			c.anchor = c.EAST;
			
		    c.gridx = 0;
		    c.gridy = 0;
		    panel.add(label, c);
		    
		    c.gridx = 1;
		    c.gridy = 0;
		    panel.add(points, c);
		    
		    c.gridx = 2;
		    c.gridy = 0;
		    panel.add(minus, c);
		    
		    c.gridx = 3;
		    c.gridy = 0;
		    panel.add(pointsToAdd, c);

		    c.gridx = 4;
		    c.gridy = 0;
		    panel.add(plus, c);
		}
		if(type == IN_GAME)
		{
			plus.setIcon(null);
			plus.setEnabled(false);
			minus.setIcon(null);
			minus.setEnabled(false);
			pointsToAdd.setBackground(Launcher.TRANSPARENT);
			pointsToAdd.setForeground(Launcher.TRANSPARENT);
			pointsToAdd.setBorder(Launcher.emptyBorder);
		}
	}
	
	public void setPointValue(int n)
	{
		points.setText(String.valueOf(n));
	}
	
	public void minusStat(ActionEvent evt)
	{
		if(valueArray[index] > 0)
		{
			if(type == NEW)
			{
				valueArray[index]--;
	    		points.setText(String.valueOf(valueArray[index]));
	    		valueArray[0]++;
	    		pointsLeft.setText(String.valueOf(valueArray[0]));
			}
			else if(type == LEVEL_UP)
			{
				valueArray[index]--;
	    		pointsToAdd.setText("+"+String.valueOf(valueArray[index]));
	    		valueArray[0]++;
	    		pointsLeft.setText(String.valueOf(valueArray[0]));
			}
		}
		this.updateAll();
	}
	
	public void plusStat(ActionEvent evt)
	{
		if(valueArray[0] > 0)
		{
			if(type == NEW)
			{
				valueArray[index]++;
	    		points.setText(String.valueOf(valueArray[index]));
	    		valueArray[0]--;
	    		pointsLeft.setText(String.valueOf(valueArray[0]));
			}
			else if(type == LEVEL_UP)
			{
				valueArray[index]++;
	    		pointsToAdd.setText("+"+String.valueOf(valueArray[index]));
	    		valueArray[0]--;
	    		pointsLeft.setText(String.valueOf(valueArray[0]));
			}
		}
		this.updateAll();
	}
	public void updateAll()
	{
		for(int n=0; n < otherFields.length; n++)
		{
			otherFields[n].update();
		}
	}
	
	public void update()
	{
		if(valueArray[0] <= 0 | type == IN_GAME)
		{
			plus.setIcon(null);
			plus.setEnabled(false);
		}
		else if(plus.getIcon() == null)
		{
			plus.setIcon(plusIcon);
			plus.setEnabled(true);
		}
		
		if(valueArray[index] <= 0 | type == IN_GAME)
		{
			minus.setIcon(null);
			minus.setEnabled(false);
		}
		else if(minus.getIcon() == null)
		{
			minus.setIcon(minusIcon);
			minus.setEnabled(true);
		}
		panel.repaint();
	}
	
	public String getStringValue()
	{
		if(type == NEW)
		{
			return points.getText();
		} 
		else 
		{
			return pointsToAdd.getText();
		}
	}
	
	public void reset(){
		pointsToAdd.setText("+"+String.valueOf(valueArray[index]));
	}
	
	public void showPlus(boolean b)
	{
		if(b)
		{
			plus.setIcon(plusIcon);
			plus.setEnabled(true);
			if(valueArray[index] == 0)
			{
				minus.setIcon(null);
			}
			else
			{
				minus.setIcon(minusIcon);
			}
			minus.setEnabled(true);
			pointsToAdd.setBackground(Launcher.FIELDGRAY);
			pointsToAdd.setForeground(label.getForeground());
			pointsToAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		else
		{
			plus.setIcon(null);
			plus.setEnabled(false);
			minus.setIcon(null);
			minus.setEnabled(false);
			pointsToAdd.setBackground(Launcher.TRANSPARENT);
			pointsToAdd.setForeground(Launcher.TRANSPARENT);
			pointsToAdd.setBorder(Launcher.emptyBorder);
		}
	}
}
