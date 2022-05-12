package app;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.*;

import org.w3c.dom.events.Event;

import utils.ApplicationTime;

public class _6_YourOwnAnimation extends Animation {
	public double addedTime = 0;
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
		JLabel mass1l = new JLabel("Masse 1:");
        panel.add(mass1l);
        JTextField mass1tf = new JTextField("5", 5);
        panel.add(mass1tf);

		JLabel x1sl = new JLabel("Startwert 1:");
        panel.add(x1sl);
        JTextField x1stf = new JTextField("300", 5);
        panel.add(x1stf);

		JLabel v1l = new JLabel("velocity 1:");
        panel.add(v1l);
        JTextField tfv1 = new JTextField("30", 5);
        panel.add(tfv1);

		JLabel mass2l = new JLabel("Masse 2:");
        panel.add(mass2l);
		JTextField tfmass2 = new JTextField("5", 5);
		panel.add(tfmass2);

		JLabel x2sl = new JLabel("Startwert 2:");
        panel.add(x2sl);
		JTextField tfx2s = new JTextField("600", 5);
		panel.add(tfx2s);

		JLabel labelv2 = new JLabel("velocity 2:");
        panel.add(labelv2);
        JTextField tfv2 = new JTextField("-30", 5);
        panel.add(tfv2);
		
		JLabel dl = new JLabel("Federhärte:");
        panel.add(dl);
		JTextField tfd = new JTextField("20", 5);
		panel.add(tfd);

		JLabel l0l = new JLabel("Gleichgewichtsabstand:");
        panel.add(l0l);
		JTextField tfl0 = new JTextField("200", 5);
		panel.add(tfl0);
		
        JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				addedTime += _6_YourOwnAnimationPanel.time;
				_6_YourOwnAnimationPanel.resetTime = addedTime;

				_6_YourOwnAnimationPanel.x1_start = Double.parseDouble(x1stf.getText());
				_6_YourOwnAnimationPanel.mass1 = Double.parseDouble(mass1tf.getText());
				_6_YourOwnAnimationPanel.v1 = Double.parseDouble(tfv1.getText());

				_6_YourOwnAnimationPanel.x2_start = Double.parseDouble(tfx2s.getText());
				_6_YourOwnAnimationPanel.mass2 = Double.parseDouble(tfmass2.getText());
				_6_YourOwnAnimationPanel.v2 = Double.parseDouble(tfv2.getText());

				_6_YourOwnAnimationPanel.d = Double.parseDouble(tfd.getText());
				_6_YourOwnAnimationPanel.l0 = Double.parseDouble(tfl0.getText());
			}
		});
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
	public static double time;
	public static double resetTime;

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
	public static double mass1 = 5;
	public static double x1_start = 300;
	public static double x1t;
	public static double v1 = 30;
	
	//Zweite Masse
	public static double mass2 = 5;
	public static double x2_start =  600;
	public static double x2t;	
	public static double v2 = -30;
	
	//Gleichgewichtsabstand l0
	public static double l0 = 200;
	
	//Schwerpunktposition X(t)
	double masseinvers = 1;
	public static double sppX = 500;
	double sspV = 1;
	double schwingAmplitude = 1;
	double schwingT = 1;
	double schwingT2 = 1;
	
	//Effektiver Abstand s(t)
	public static double d = 20;
	double a = 1;
	double b = 1;
	double u = 1;
	double s = 1;

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d;
		g2d = (Graphics2D) g;
		
		super.paintComponent(g);
		time = t.getTimeInSeconds() - resetTime;
		System.out.println("Zeitpunkt: " + time);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);
		
		//Schwerpunktposition X(t)
		masseinvers = (1/(mass1 + mass2));
		sppX = masseinvers * (mass1*x1_start + mass2*x2_start) + masseinvers * (mass1*v1 + mass2*v2) * time;
		
		//Effektive Abstand s(t)
		u = (mass1 * mass2) / (mass1 + mass2);
		a = x2_start - x1_start - l0;
		b = Math.sqrt(u/d) * (v2 - v1);
		double phaseangle = Math.sqrt(d/u) * time;
		s = a * Math.cos(phaseangle) + b * Math.sin(phaseangle);
		
		// Position der beiden Massen
		x1t = sppX - (mass2/(mass1 + mass2)) * (s + l0);
		x2t = sppX + (mass1/(mass1 + mass2)) * (s + l0);

		/*if (time >= 3.0 && time <= 3.03){
			System.out.println(x1t + " " + x2t);
		}*/

		// Validierung
		sspV = (1/(mass1 + mass2)) * (mass1 * v1 + mass2 * v2);
		schwingAmplitude = s * Math.sin(Math.sqrt(d/u) * time);
		schwingT = 2 * Math.PI * Math.sqrt(mass1/d);
		schwingT2 = 2 * Math.PI * Math.sqrt(mass2/d);

		System.out.println("Schwerpunktgeschwindigkeit: " + sspV + "\n" +
							"Schwingungsamplitude: " + schwingAmplitude + "\n" +
							"Schwingungsperiodendauer von masse1: " + schwingT + "\n" +
							"Schwingungsperiodendauer von masse2: " + schwingT2 + "\n");

		// zeichnen
		g.setColor(Color.red);
		g.fillRect((int)x1t, 200, 50, 50);
		g.setColor(Color.blue);
		g.fillRect((int)x2t, 200, 50, 50);
		g.setColor(Color.black);
		g.drawLine((int)x1t + 50, 225, (int)x2t, 225);
		g.fillOval((int)sppX + 20, 220, 10, 10);
		g.fillOval((int)x1t + 20, 220, 10, 10);
		g.fillOval((int)x2t +20, 220, 10, 10);

		double abbruchbedingung = l0 - Math.sqrt(a*a + b*b);
		//System.out.println(abbruchbedingung);
		if (abbruchbedingung < 50){
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.PLAIN, 50));
			g.drawString("Nicht Physikalisch korrekte Darstellung,", 100, 100);
			g.drawString("da eine Kollision,", 100, 200);
			g.drawString("der beiden Massen stattfindet!!!", 100, 300);
		}

		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Zeitpunkt: " + time, 10, height - 125);
		g.drawString("Schwerpunktgeschwindigkeit: " + sspV, 10, height - 100);
		g.drawString("Schwingungsamplitude: " + schwingAmplitude, 10, height - 75);
		g.drawString("Schwingperiode Masse 1: " + schwingT, 10, height - 50);
		g.drawString("Schwingperiode Masse 2: " + schwingT2, 10, height - 25);

		// Weg Zeit Diagramm
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g2d.setStroke(new BasicStroke(5.0f));
		g.drawLine(width - 400, height - 50, width, height - 50);
		g.drawLine(width - 200, height - 250, width - 200, height);

		g.drawString("200", width - 225, height - 50 - 20);
		g.drawString("400", width - 225, height - 50 - 40);
		g.drawString("600", width - 225, height - 50 - 60);
		g.drawString("800", width - 225, height - 50 - 80);
		g.drawString("x", width - 211, height - 50 - 150);

		g.drawString("5", width - 200 + 10, height - 50 + 11);
		g.drawString("10", width - 200 + 20, height - 50 + 11);
		g.drawString("20", width - 200 + 40, height - 50 + 11);
		g.drawString("30", width - 200 + 60, height - 50 + 11);
		g.drawString("40", width - 200 + 80, height - 50 + 11);
		g.drawString("50", width - 200 + 100, height - 50 + 11);
		g.drawString("60", width - 200 + 120, height - 50 + 11);
		g.drawString("t", width - 200 + 180, height - 50 + 11);

		g.setColor(Color.red);
		g.fillOval(width - 200 + (int)time*2, height - 50 - (int)x1t/10, 10, 10);
		g.setColor(Color.blue);
		g.fillOval(width - 200 + (int)time*2, height - 50 - (int)x2t/10, 10, 10);
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
		double masseschwerpunkt_M1 = _6_YourOwnAnimationPanel.x1t + 25;
		double masseschwerpunkt_M2 = _6_YourOwnAnimationPanel.x2t + 25;
		g.drawString("Massenmittelpunkt von Masse 1: x: " + (int)masseschwerpunkt_M1 + "  y: " + 225, 10, 20);
		g.drawString("Massenmittelpunkt von Masse 2: x: " + (int)masseschwerpunkt_M2 + "  y: " + 225, 10, 35);
		g.drawString("Schwerpunktposition: " + _6_YourOwnAnimationPanel.sppX, 10, 50);
		g.setColor(Color.LIGHT_GRAY);
	}
}