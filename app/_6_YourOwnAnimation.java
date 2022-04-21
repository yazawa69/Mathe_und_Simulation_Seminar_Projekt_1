package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.*;

import utils.ApplicationTime;

public class _6_YourOwnAnimation extends Animation {
	
	public static double x1 = 400;
	public static double x2 = 600;
	public static double sppX = 1;


	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create Frame
		 */
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new _6_YourOwnAnimationPanel(applicationTimeThread);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);

		//Create SecondaryFrame
		 JFrame secondaryFrame = new JFrame("Position");
		 secondaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 JPanel secondaryPanel = new SecondaryGraphicsContent();
		 secondaryFrame.add(secondaryPanel);
		 secondaryFrame.pack(); // adjusts size of the JFrame to fit the size of it's components
		 secondaryFrame.setVisible(true);

		 // Textfeld wird erstellt
		 // Text und Spaltenanzahl werden dabei direkt gesetzt
		JLabel label = new JLabel("Masse 1:");
        panel.add(label);
        JTextField tfName = new JTextField("", 5);
        panel.add(tfName);

		JLabel labelv = new JLabel("velocity 1:");
        panel.add(labelv);
        JTextField tfv1 = new JTextField("", 5);
        panel.add(tfv1);

		JLabel label2 = new JLabel("Masse 2:");
        panel.add(label2);
		JTextField tfName2 = new JTextField("", 5);
		panel.add(tfName2);

		JLabel labelv2 = new JLabel("velocity 2:");
        panel.add(labelv2);
        JTextField tfv2 = new JTextField("", 5);
        panel.add(tfv2);
		
		JLabel label3 = new JLabel("Federhärte:");
        panel.add(label3);
		JTextField tfName3 = new JTextField("", 5);
		panel.add(tfName3);

		JLabel label4 = new JLabel("Gleichgewichtsabstand:");
        panel.add(label4);
		JTextField tfName4 = new JTextField("", 5);
        // Textfeld wird unserem Panel hinzugefügt
		panel.add(tfName4);
 
        JButton buttonOK = new JButton("OK");
        panel.add(buttonOK);
		

		frame.setVisible(true);

		
		frames.add(frame);
		frames.add(secondaryFrame);
		return frames;
	}

}

@SuppressWarnings("serial")
class _6_YourOwnAnimationPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;

	public _6_YourOwnAnimationPanel(ApplicationTime thread) {
		this.t = thread;
	}

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
	}
	
	int width = _0_Constants.WINDOW_WIDTH;
	int height = _0_Constants.WINDOW_HEIGHT;



	//Erste Masse
	double mass1 = 5;
	double x1_start = 400;//_6_YourOwnAnimation.x1;
	double x1t;
	double v1 = 10;
	double v1_start = v1;
	
	//Zweite Masse
	double mass2 = 5;
	double x2_start =  600;//_6_YourOwnAnimation.x2;
	double x2t;	
	double v2 = -10;
	double v2_start = v2;
	
	//Gleichgewichtsabstand l0
	double l0 = 200;
	
	//Schwerpunktposition X(t)
	double masseinvers = 1;
	double sppX = 500;
	
	//Effektiver Abstand s(t)
	final double d = 1;
	double a = 1;
	double b = 1;
	double u = 1;
	double s = 1;
	
	//Zeit
	double collisionTime = time;
	double deltaTime = 0.0; 
	double lastFrameTime = 0.0;


	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		time = t.getTimeInSeconds();

		deltaTime = time - lastFrameTime; 
		lastFrameTime = time;
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);
		
		//Schwerpunktposition X(t)
		masseinvers = (1/(mass1 + mass2));
		/*_6_YourOwnAnimation.*/sppX = masseinvers * (mass1*x1_start + mass2*x2_start) + masseinvers * (mass1*v1_start + mass2*v2_start) * time;
		
		//Effektive Abstand s(t)
		u = (mass1 * mass2) / (mass1 + mass2);
		a = 100;//_6_YourOwnAnimation.x2 - _6_YourOwnAnimation.x1 - l0;
		b = Math.sqrt(u/d) * (v2 - v1);
		double zwischenErgebnis = Math.sqrt(d/u) * time;// phaseangle
		s = a * Math.cos(zwischenErgebnis) + b * Math.sin(zwischenErgebnis);
		
		// Position der beiden Massen
		/*_6_YourOwnAnimation.*/x1t = /*_6_YourOwnAnimation.*/sppX - (mass2/(mass1 + mass2)) * (s + l0);
		/*_6_YourOwnAnimation.*/x2t = /*_6_YourOwnAnimation.*/sppX + (mass1/(mass1 + mass2)) * (s + l0);
		
		
		
		System.out.println("Masse 1: " + mass1 + ", Masse 2: " + mass2);
		System.out.println("x1t: " + x1t + ", x2t: " + x2t);
		System.out.println("x 1: " + _6_YourOwnAnimation.x1 + ", x 2: " + _6_YourOwnAnimation.x2);
		System.out.println("x start 1: " + x1_start + ", x start 2: " + x2_start);
		System.out.println("v 1: " + v1 + ", v 2: " + v2);
		System.out.println("v start 1: " + v1_start + ", v start 2: " + v2_start);
		
		System.out.println("Gleichgewichtsabstand: " + l0);
		System.out.println("Schwerpunktposition: " + _6_YourOwnAnimation.sppX);
		System.out.println("effektiver abstand: " + s);

		System.out.println("a: " + a + " , b: " + b + " ,u: " + u + " ,ergebnisKlammmer: " + zwischenErgebnis);

		g.setColor(Color.black);
		g.fillRect((int)/*_6_YourOwnAnimation.*/x1t, 200, 50, 50);
		g.fillRect((int)/*_6_YourOwnAnimation.*/x2t, 200, 50, 50);
	}
}

@SuppressWarnings("serial")
class SecondaryGraphicsContent extends JPanel {
	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(300, 100);
	}

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double masseschwerpunkt_M1 = _6_YourOwnAnimation.x1 + 25;
		double masseschwerpunkt_M2 = _6_YourOwnAnimation.x2 + 25;
		g.drawString("Massenmittelpunkt von Masse 1: x: " + (int)masseschwerpunkt_M1 + "  y: " + 225, 10, 20);
		g.drawString("Massenmittelpunkt von Masse 2: x: " + (int)masseschwerpunkt_M2 + "  y: " + 225, 10, 35);
		g.drawString("Schwerpunktposition: " + _6_YourOwnAnimation.sppX, 10, 50);
		g.setColor(Color.LIGHT_GRAY);
	}
}