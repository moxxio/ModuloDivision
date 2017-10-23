import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.*;

public class DrawPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private final int radius = 300;
	private final Point middle;
	private final double[][] locPoints;

	private int numPoints;
	private int runner;

	public DrawPanel() {
		setLayout(null);
		setBackground(Color.BLACK);

		middle = new Point(640, 360);
		runner = 0;
		numPoints = 360;

		locPoints = new double[numPoints][2];
		for (double i = 0; i < locPoints.length; i++) {
			locPoints[(int) i][0] = (double) (middle.getX() + radius * Math.sin(2 * Math.PI * (i / numPoints)));
			locPoints[(int) i][1] = (double) (middle.getY() + radius * Math.cos(2 * Math.PI * (i / numPoints)));
		}
		
		setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		
		for (double[] point : locPoints) {
			g.drawRect((int) (point[0]), (int) (point[1]), 1, 1);
		}
		
		for (int i = 0; i < numPoints; i++) {
			int destination = (runner * i) % numPoints;
			int x1 = (int) (locPoints[i][0]);
			int y1 = (int) (locPoints[i][1]);
			int x2 = (int) (locPoints[destination][0]);
			int y2 = (int) (locPoints[destination][1]);
			g.drawLine(x1, y1, x2, y2);
		}
		
		g.drawString("" + runner, 50, 50);
	}

	@Override
	public void run() {
		while (true) {
			runner++;
			// numPoints++;
			
			repaint();
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// Do nothing
			}
		}
	}
}
