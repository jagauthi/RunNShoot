package View;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NinePatchImage{
	private int cornerW, cornerH;
	private int height, width;
	private BufferedImage baseImage, scaledImage;
	public NinePatchImage(int w, int h, int cw, int ch, BufferedImage i){
		height = h;
		width = w;
		cornerW = cw;
		cornerH = ch;
		baseImage = i;
		scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		initScaledImage(width, height);
	}
	private BufferedImage initScaledImage(int w, int h){
		height = h;
		width = w;
		scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = scaledImage.getGraphics();
		int baseH = baseImage.getHeight();
		int baseW = baseImage.getHeight();
		//Corners
		g.drawImage(baseImage.getSubimage(0, 0, cornerW, cornerH), 0, 0, null);//TR
		g.drawImage(baseImage.getSubimage(baseW - cornerW, 0, cornerW, cornerH), width - cornerW, 0, null);//TL
		g.drawImage(baseImage.getSubimage(0, baseH - cornerH, cornerW, cornerH), 0, height - cornerH, null);//BR
		g.drawImage(baseImage.getSubimage(baseW - cornerW, baseH - cornerH, cornerW, cornerH), width - cornerW, height - cornerH, null);//BL
		//Sides
		g.drawImage(baseImage.getSubimage(cornerW, 0, baseW-(2*cornerW), cornerH), cornerW, 0, width - (2*cornerW), cornerH, null);//Top
		g.drawImage(baseImage.getSubimage(0, cornerH, cornerW, baseH-(2*cornerH)), 0, cornerH, cornerW, height - (2*cornerH), null);//Left
		g.drawImage(baseImage.getSubimage(baseW - cornerW, cornerH, cornerW, baseH-(2*cornerH)), width-cornerW, cornerH, cornerW, height - (2*cornerH), null);//Right
		g.drawImage(baseImage.getSubimage(cornerW, baseH - cornerH, baseW-(2*cornerW), cornerH), cornerW, height - cornerH, width - (2*cornerW), cornerH, null);//Bottom
		//Center
		g.drawImage(baseImage.getSubimage(cornerW, cornerH, baseW-(2*cornerW), baseH-(2*cornerH)), cornerW, cornerH, width-(2*cornerW), height-(2*cornerH), null);
		return scaledImage;
	}
	public BufferedImage getScaledImage(int w, int h){
		if(w <= 0 || h <= 0)
		{
			return null;
		}
		else if(scaledImage != null && scaledImage.getWidth() == w & scaledImage.getHeight() == h)
		{
			return scaledImage;
		} else 
		{
			return initScaledImage(w, h);
		}
	}
	public int getCornerWidth(){
		return cornerW;
	}
	public int getCornerHeight(){
		return cornerH;
	}
	
	
}
