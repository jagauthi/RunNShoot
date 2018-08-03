package Controller;

import Model.Player;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

public class ML implements MouseListener {

	Player player;

	Timer timer = new Timer();
	TimerTask task;

	private class MyTimerTask extends TimerTask {
		public void run() {
			Point p = MouseInfo.getPointerInfo().getLocation();
			player.attack((int) p.getX(), (int) p.getY());
		}
	}

	public ML(Player p) {
		player = p;
	}

	public void mousePressed(MouseEvent e) {

		if (e.getButton() == 1) {
			task = new MyTimerTask();
			timer.scheduleAtFixedRate(task, 0, 50); // Time is in milliseconds
		} else
			player.reload();
	}

	public void mouseReleased(MouseEvent e) {

		task.cancel();
		player.setNotShooting();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent arg0) {

	}
}
