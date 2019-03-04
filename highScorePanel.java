//package game;

//import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
//import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class highScorePanel extends JPanel {
	//private int isize = 400;
	//private int sizef = 700;
	public SlowUpdate shu = new SlowUpdate(); // Updates the High Score
	public Timer slowupdated = new Timer(3000, shu); // Updates the High Scores every 3 seconds
	public JLabel blank[] = new JLabel[10]; // Holds up to 10 high scores
	public File highfile; // File to load from
	public Scanner highinput; // Reads from the file
	public JPanel contains; // Formatting
	public Image backgroundhigh; // Background Image
	public highScorePanel () {
		this.setLayout(null);
		this.setFocusable(true);
		// The Menu
		JMenuBar menuBar; // The Menu's Bar
		JMenu menu, menu2; // The Drop Down Menus
		JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, saveItem1, loadItem1, hideItems1, revealItems1, specialNotice1, hsItem1; // Menu Items
		menuBar = new JMenuBar();
		//Build the first menu.
		menu = new JMenu("Navigation"); // The Navigation Bar
		menu.setMnemonic(KeyEvent.VK_Q); // Shows a letter for the notification bar
		menu2 = new JMenu("Game Functions"); // The Game Functions Bar
		menu2.setMnemonic(KeyEvent.VK_G); // Shows a letter for the game functions bar
		//menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu);
		specialNotice1 = new JMenuItem("Advanced Users Only"); // Shows a notice
		menu.add(specialNotice1);
		menu.add(new JSeparator());
		menuItem = new JMenuItem("Pre", KeyEvent.VK_P); // Brings to the animated start screen
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.addActionListener(new SwitchP());
		menu.add(menuItem);
		menuItem2 = new JMenuItem("Boot", KeyEvent.VK_B); // Brings to the boot screen
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem2.addActionListener(new SwitchB());
		menu.add(menuItem2);
		menuItem3 = new JMenuItem("Game", KeyEvent.VK_G); // Brings to the game panel
		menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		menuItem3.addActionListener(new SwitchG());
		menu.add(menuItem3);
		menuItem4 = new JMenuItem("Instructions", KeyEvent.VK_I); // Brings to the instructions panel
		menuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
		menuItem4.addActionListener(new SwitchI());
		menu.add(menuItem4);
		hsItem1 = new JMenuItem("High Scores", KeyEvent.VK_H); // Brings to the high score panel
		hsItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
		hsItem1.addActionListener(new SwitchH());
		menu.add(hsItem1);
		hideItems1 = new JMenuItem("Return To Game", KeyEvent.VK_G); // Brings to the game panel
		hideItems1.addActionListener(new SwitchG());
		menu2.add(hideItems1);
		menuBar.add(menu2);
		menuItem6 = new JMenuItem("Return To Home"); // Brings back to the home panel
		menuItem6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItem6.addActionListener(new SwitchB());
		menuBar.add(menuItem6);
		menuItem5 = new JMenuItem("Quit Game"); // Quits the game
		menuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		menuItem5.addActionListener(new SwitchQ());
		menuBar.add(menuItem5);
		menuBar.setPreferredSize(new Dimension(0, 15)); // Sets the preferred dimensions
		menuBar.setBounds(0, 0, 800, 15); // Sets location on the screen
		this.add(menuBar);
		menuBar.setBounds(0, 0, 800, 15);
		
		// Loads the background Image
		String imagename4 = "instructions4.JPG"; 
		backgroundhigh = null;
	    try { // Does this
	        backgroundhigh = ImageIO.read(new File(imagename4)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open highscores image file 4");
			System.exit(1); // Exits
		}
	    // Contains is a JPanel that contains the top 10 high scores
		contains = new JPanel();
		contains.setLayout(new GridLayout(10, 0));
		// Initializes the highscore's formatting and sets the JLabels
		for (int i = 0; i < 10; i++) {
			blank[i] = new JLabel("Loading: " + i + "");
			contains.add(blank[i]);
			blank[i].setFont(new Font("Arial", Font.BOLD, 14));
			blank[i].setForeground(Color.white);
			contains.revalidate();
			contains.repaint();
		}
		contains.setBackground(Color.darkGray);
		contains.setBounds(30, 200, 400, 400); // Sets the boundaries on the screen
		this.add(contains);
		
		// Title of the Panel
		JLabel text = new JLabel("High Scores: ");
		text.setForeground(Color.white);
		text.setFont(new Font("Arial", Font.BOLD, 40));
		text.setBounds(100, 50, 600, 100);
		this.add(text);
		
		// Sets the components to match the color scheme
		menuItem6.setForeground(Color.white);
		menuItem6.setBackground(Color.black);
		menuItem5.setBackground(Color.black);
		menuItem5.setForeground(Color.white);
		menu.setForeground(Color.white);
		menu.setBackground(Color.black);
		menu2.setForeground(Color.white);
		menu2.setBackground(Color.black);
		menuBar.setBackground(Color.black);
		menuBar.setForeground(Color.white);
		
		// Starts the updating process
		slowupdated.start();
		// Sets the highscore file name
		highfile = new File("highscores.txt");
	}
	public void updateHS() {
		try { // Reader is loaded
			highinput = new Scanner(highfile);
		} catch(FileNotFoundException hfnf) {
			System.err.println("ERROR: File cannot be found");
			System.exit(5);
		}
		int count = 0; // Starts at 0
		while (highinput.hasNext()) { // Loads and sets the highscores JLabels
			String line = highinput.nextLine();
			String score = line.substring(0, line.indexOf(";"));
			line = line.substring(line.indexOf(";"));
			String name = line.substring(1); // removes simicolon for formatting
			score = score.trim();
			line = line.trim();
			int tempcount = count + 1;
			//blank[count].setText("No. " + tempcount + ": " + line);
			blank[count].setText("No. " + tempcount + ": " + name + " w/ " + score);
			contains.repaint();
			contains.revalidate();
			count++;
			if (count > 9) break; // Caps out at 10 (length)
		}
	}
	private class SlowUpdate implements ActionListener { // Launches the Update Process
		public void actionPerformed (ActionEvent m) {
			updateHS();
		}
	}
	private class SwitchP implements ActionListener { // Switches to preboot animation
		public void actionPerformed (ActionEvent m) {
			loadScreen.record("Pre");
		}
	}
	private class SwitchB implements ActionListener { //Switches to the boot/menu screen
		public void actionPerformed (ActionEvent m) {
			loadScreen.record("Boot");
		}
	}
	private class SwitchG implements ActionListener { // Switches to the game panel
		public void actionPerformed (ActionEvent m) {
			loadScreen.record("Game");
		}
	}
	private class SwitchI implements ActionListener { // Switches to the instructions panel
		public void actionPerformed (ActionEvent m) {
			loadScreen.record("Instructions");
		}
	}
	private class SwitchH implements ActionListener { // Switches to the highscore panel
		public void actionPerformed (ActionEvent m) {
			loadScreen.record("HighScore");
		}
	}
	private class SwitchQ implements ActionListener { // Exits the game
		public void actionPerformed (ActionEvent m) {
			System.exit(1);
		}
	}
	public void paintComponent(Graphics i) {
		super.paintComponent(i);
		setBackground(Color.black);
		i.drawImage(backgroundhigh, 0, 15, 800, 815, this); // Draws the background image
		requestFocus(); // Has focus to change slides/instructions
	}
}
