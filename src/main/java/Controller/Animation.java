package Controller;

import java.awt.image.BufferedImage;

public class Animation {
	private BufferedImage[] frames;
	private int currentFrame;
	private long startTime;
	private long delay;
	int[] offsetX, offsetY;
	public static boolean playing = true;
	
	private Boolean playedOnce;
	private Boolean repeat;
	
	public Animation(){
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frameIn){
		this.frames = frameIn;
		startTime = System.nanoTime();
		
		repeat = false;
		currentFrame = 0;
	}
	
	public void setDelay(long delay){this.delay = delay;}
	public void setCurrentFrame(int n){this.currentFrame = n;}
	public void setOffsetX(int[] n){offsetX = n;}
	public void setOffsetY(int[] n){offsetY = n;}
	public void setRepeat(boolean b){repeat = b;}
	
	public void resetHasPlayedOnce(){playedOnce = false;}
	
	public int getFrame(){return currentFrame;}
	public BufferedImage getImage(){return frames[currentFrame];}
	public boolean getHasPlayedOnce(){return playedOnce;}
	public int getOffsetX(){return offsetX[currentFrame];}
	public int getOffsetY(){return offsetY[currentFrame];}
	
	public void update(){
		if(delay == -1) return;
		long elipsed = (System.nanoTime() - startTime)/1000000;
		if(elipsed > delay){
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length){
			playedOnce = true;
			if(repeat){
				currentFrame = 0;
			} else {
				currentFrame = frames.length - 1;
			}
			
		}
	}
}
