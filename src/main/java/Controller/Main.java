package Controller;

import javax.swing.JFrame;

import View.GamePanel;
import View.Launcher;

public class Main {

	/*
	public Main() {
		JFrame window = new JFrame("SWE Game");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
	*/

	public static void main(String[] args) {
		//new Main();
		new Launcher();
	}
}
