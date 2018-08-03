package View;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class BackgroundedPanel extends JPanel{
	private NinePatchImage background;
	public BackgroundedPanel(NinePatchImage np){
		super();
		background = np;
		//this.setOpaque(false);
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
	}
	public NinePatchImage getNinePatch(){
		return background;
	}
	@Override
	  protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    g.drawImage(background.getScaledImage(this.getWidth(), this.getHeight()), 0, 0, null);
	    
	}
}
