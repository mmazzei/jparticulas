package ar.uba.fi.jparticulas;

import javax.swing.JFrame;

/**
 * Hello world!
 * 
 */
public class Launcher extends JFrame {
	public static final int SCREEN_HEIGHT = 600;
	public static final int SCREEN_WIDTH = 800;

	public Launcher() {
		JFrame container = new JFrame("jParticulas");
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
	}
}
