package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;

import Controller.StateMachine;
import Controller.ML;
import Model.Player;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;

	JFrame window;

	private Thread thread;
	private boolean running;

	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	private StateMachine sm;

	public GamePanel(String playerInfo) {
		super();

		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		setBackground(Color.GREEN);
		setVisible(true);

		sm = new StateMachine(this, playerInfo);
		addKeyListener(this);
		addMouseListener(new ML(sm.getPlayer()));

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void doRequestFocus() {
		requestFocus();
	}

	public void setMouseListenerPlayer(Player player) {
		addMouseListener(new ML(player));
	}

	public void addComponent(JComponent comp) {
		this.add(comp);
	}

	public void removeComponent(JComponent comp) {
		this.remove(comp);
	}

	public void disposeWindow() {
		window.dispose();
	}

	@Override
	public void run() {
		long start;

		long timer = System.currentTimeMillis();

		long elapsed;
		long wait;

		while (running) {

			if (System.currentTimeMillis() - timer > 1000) {
				// if we make timer+=1000, it will go off once per second.
				// if we make timer+=500, it will go off twice per second.
				timer += 500;
				sm.oncePerSecondUpdate();
			}

			start = System.nanoTime();

			update();
			repaint();

			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;
			if (wait < 0) {
				wait = 5;
			}
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		sm.update();
	}

	public void paintComponent(Graphics g) {
		sm.render(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		sm.getCurrentState().keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		sm.getCurrentState().keyReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
