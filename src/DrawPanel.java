import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawPanel extends JPanel implements ActionListener, ChangeListener, Runnable {

	private static final long serialVersionUID = 1L;

	private final int radius = 300;
	private final Point middle;
	private final String[] optSelectRunner = { "Runner", "Number of Points" };

	private int numPoints;
	private int runner;
	private boolean go;
	private long pause;
	private boolean goRunner;

	private JComboBox<String> selectRunner;
	private JButton startstop;
	private JButton reset;
	private JSlider sliderRunner;
	private JSlider sliderNumPoints;
	private JSlider sliderSleep;

	public DrawPanel() {
		setLayout(null);
		setBackground(Color.BLACK);

		middle = new Point(640, 360);
		runner = 0;
		numPoints = 360;
		go = false;
		pause = 1000;
		goRunner = true;

		startstop = new JButton("Start");
		startstop.setBounds(1100, 50, 150, 20);
		startstop.addActionListener(this);
		add(startstop);

		reset = new JButton("Reset");
		reset.setBounds(1100, 80, 150, 20);
		reset.addActionListener(this);
		add(reset);

		selectRunner = new JComboBox<>();
		for (String item : optSelectRunner) {
			selectRunner.addItem(item);
		}
		selectRunner.setBounds(1100, 110, 150, 20);
		selectRunner.addActionListener(this);
		add(selectRunner);

		sliderRunner = new JSlider(0, 1000, 0);
		sliderRunner.setBounds(1100, 140, 150, 20);
		sliderRunner.addChangeListener(this);
		add(sliderRunner);

		sliderNumPoints = new JSlider(4, 36000, 360);
		sliderNumPoints.setBounds(1100, 170, 150, 20);
		sliderNumPoints.addChangeListener(this);
		add(sliderNumPoints);
		
		sliderSleep = new JSlider(1, 10000, 1000);
		sliderSleep.setBounds(1100, 200, 150, 20);
		sliderSleep.addChangeListener(this);
		add(sliderSleep);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source == startstop) {
			if (go) {
				go = false;
				startstop.setText("Start");
				reset.setEnabled(true);
				selectRunner.setEnabled(true);
				sliderNumPoints.setEnabled(true);
				sliderRunner.setEnabled(true);
			} else {
				go = true;
				startstop.setText("Stop");
				reset.setEnabled(false);
				selectRunner.setEnabled(false);
				sliderNumPoints.setEnabled(false);
				sliderRunner.setEnabled(false);
			}
		} else if (source == reset) {
			reset();
		} else if (source == selectRunner) {
			if (selectRunner.getSelectedIndex() == 0) {
				goRunner = true;
			} else {
				goRunner = false;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.drawString("Runner: " + runner, 50, 50);
		g.drawString("Number of Points: " + numPoints, 50, 80);
		g.drawString("Sleep for: " + pause + "ms", 50, 110);

		double[][] locPoints = new double[numPoints][2];
		for (double i = 0; i < locPoints.length; i++) {
			locPoints[(int) i][0] = (double) (middle.getX() + radius * Math.sin(2 * Math.PI * (i / numPoints)));
			locPoints[(int) i][1] = (double) (middle.getY() + radius * Math.cos(2 * Math.PI * (i / numPoints)));
		}

		for (double[] point : locPoints) {
			g.drawRect((int) (point[0]), (int) (point[1]), 1, 1);
		}

		for (double i = 0; i < numPoints; i++) {
			int destination = (int) ((runner * i) % numPoints);
			int x1 = (int) Math.round(locPoints[(int) i][0]);
			int y1 = (int) Math.round(locPoints[(int) i][1]);
			int x2 = (int) Math.round(locPoints[destination][0]);
			int y2 = (int) Math.round(locPoints[destination][1]);

			double rgb = 255;
			int red = (int) (Math.abs(Math.sin(2 * Math.PI * (i / numPoints)) * rgb));
			int green = (int) (Math.abs(Math.sin(2 * Math.PI * (i / numPoints) + (2 * Math.PI) / 3) * rgb));
			int blue = (int) (Math.abs(Math.sin(2 * Math.PI * (i / numPoints) + (4 * Math.PI) / 3) * rgb));
			g.setColor(new Color(red, green, blue));

			g.drawLine(x1, y1, x2, y2);
		}
	}

	private void reset() {
		runner = 0;
		numPoints = 360;
		repaint();
	}

	@Override
	public void run() {
		while (true) {
			if (go) {
				if (goRunner) {
					runner++;
				} else {
					numPoints++;
				}

				repaint();
			}

			try {
				Thread.sleep(pause);
			} catch (Exception e) {
				// Do nothing
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		Object source = arg0.getSource();
		if (source == sliderNumPoints) {
			numPoints = sliderNumPoints.getValue();
			repaint();
		} else if (source == sliderRunner) {
			runner = sliderRunner.getValue();
			repaint();
		} else if (source == sliderSleep) {
			pause = (long) sliderSleep.getValue();
			repaint();
		}
	}
}
