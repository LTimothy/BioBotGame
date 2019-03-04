//package game;

import java.awt.CardLayout; // Imports necessary tools
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class preBoot extends JPanel { // Before Boot Screen Animation
	static CardLayout mainpanels; // Allows static reference to the mainpanels
	private Mover mover = new Mover(); // Listener for the Timer
	private Timer timer = new Timer(2, mover); // Timer will reduce brightness at a certain set interval
	//private boolean finished255 = false;
	private boolean printtext = false; // By default no text is printed
	private int whitescale; // Keeps track of the gradient and color
	public preBoot () {
		whitescale = 255; // By default, color is white
		timer.start(); // Starts the timer
	}
	public void paintComponent(Graphics i) {
		super.paintComponent(i);
		setBackground(Color.white); 
		i.setColor(new Color(whitescale, whitescale, whitescale, 255 - whitescale)); // Sets the "background" color
		i.fillRect(0, 0, 1000, 1000); // Prints out a rectangle with the background color
		i.setFont(new Font("Serif", Font.ITALIC, 140)); // Sets the font type and color to display
		i.setColor(Color.white);
		if (printtext == true) { // Prints the text to display when needed
			i.drawString("BioBot", 205, 407);
			i.setFont(new Font("Serif", Font.ITALIC, 20));
			i.drawString("JAVA Final Project 2014 ~ Timothy Lee", 260, 427);
		}
	}
	private class Mover implements ActionListener {
		public void actionPerformed (ActionEvent m) {
			whitescale--; // Decreases brightness accordingly
			if (whitescale <= 5) printtext = true; // If low enough, print text
			if (whitescale <= 0) { // If less than 0, do this
				timer.stop(); // Stops the timer
				//finished255 = true;
				try { // Sleeps for 2 seconds before continuing
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				FlashEscape(); // Launches the exit routine
			}
			repaint();
		}
	}
	public void FlashEscape() {
		whitescale = 255;
		repaint();
		loadScreen.record("Boot"); // Switches to the Main Menu Panel
	}
}
