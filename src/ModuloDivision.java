import java.awt.BorderLayout;

import javax.swing.*;

public class ModuloDivision extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Thread drawThread;
	private DrawPanel drawPanel;

	public ModuloDivision() {
		setLayout(new BorderLayout());
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		drawPanel = new DrawPanel();
		add(drawPanel);
		
		drawThread = new Thread(drawPanel);
		
		setVisible(true);
		drawThread.start();
	}
	
	public static void main(String[] args) {
		new ModuloDivision();
	}

}
