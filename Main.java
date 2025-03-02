package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame("Snake in Java, By:Lu");
		
		window.setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		window.setResizable(false);
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Panel panel = new Panel(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		window.add(panel);
		window.pack();
	}
}
