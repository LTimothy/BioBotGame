//package game;

import java.awt.BorderLayout; // Imports necessary tools
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class bootScreen extends JPanel { // The Main Menu / Boot Screen
	static CardLayout mainpanels; // Allows static reference to the mainpanels
	//private String imagename, gifname;
	//private Image img;
	private ImageIcon img2; // The Background
	private JLabel tlabel; // The Title Label
	public bootScreen () {
		this.setLayout(null); // Sets layout null, focusable, size, and the default background color
		this.setFocusable(true);
		this.setSize(800, 815);
		setBackground(Color.black);
		
		loadGIF(); // Loads the GIF Image
		Implement(); // Implements the image and components
		
		JMenuBar menuBar; // The MenuBar (contains everything)
		JMenu menu, menu2;
		JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, saveItem1, loadItem1, hideItems1, revealItems1, specialNotice1, hsItem1; // Menu Items. Unused -> Future Use
		JMenuItem menuItem7; // NEW *********************
		menuBar = new JMenuBar();
		//Build the first menu.
		menu = new JMenu("Navigation"); // Sets name of menu
		menu.setMnemonic(KeyEvent.VK_Q); 
		menu2 = new JMenu("Game Functions");
		menu2.setMnemonic(KeyEvent.VK_G);
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
		// NEW START ******************************
		menuItem7 = new JMenuItem("Force Start", KeyEvent.VK_F);
		menuItem7.addActionListener(new SwitchForce());
		menu.add(menuItem7);
		// END NEW *****************************************
		hideItems1 = new JMenuItem("Return To Game", KeyEvent.VK_G); // Brings to the game
		hideItems1.addActionListener(new SwitchG());
		menu2.add(hideItems1);
		menuBar.add(menu2);
		menuItem6 = new JMenuItem("Return To Game"); // Returns  to the home screen
		menuItem6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItem6.addActionListener(new SwitchG());
		menuBar.add(menuItem6);
		menuItem5 = new JMenuItem("Quit Game"); // Quits
		menuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		menuItem5.addActionListener(new SwitchQ());
		menuBar.add(menuItem5);
		//menuBar.setPreferredSize(new Dimension(0, 15));
		
		menuItem5.setBackground(Color.black); // Sets the default color of these menu items
		menuItem5.setForeground(Color.white);
		menuItem6.setBackground(Color.black);
		menuItem6.setForeground(Color.white);
		menu.setForeground(Color.white);
		menu.setBackground(Color.black);
		menu2.setForeground(Color.white);
		menu2.setBackground(Color.black);
		menuBar.setBackground(Color.black);
		menuBar.setForeground(Color.white);
		
		menuBar.setBounds(0, 0, 800, 15); // Sets where the menubar should go
		this.add(menuBar); // Adds the menubar (color scheme?)
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
	// NEW ********************
	private class SwitchForce implements ActionListener {
		public void actionPerformed (ActionEvent f) {
			loadScreen.record("Game");
			BeginGame();
		}
	}
	// END *******************
	public void Implement() {
		JLabel tarea = new JLabel("      BioBot. A review program for Biology created by Timothy Lee. ~ JAVA 2014 Final Program"); // Title Text
		tarea.setFont(new Font("Serif", Font.ITALIC, 20)); // Sets text style of the Title Text
		tarea.setForeground(Color.white); // Sets color of the Title Text
		//tarea.setBackground(new Color(200, 200, 200, 100));
		//tarea.setBackground(new Color(200, 200, 200));
		tarea.setBounds(0, 30, 800, 65); // Sets boundaries of the text
		//tarea.setEditable(false);
		//tarea.setLineWrap(true);
		//tarea.setWrapStyleWord(true);
		tarea.setFocusable(false); // Not Focusable
		this.add(tarea); // Adds the text
		
		JLabel title = new JLabel("BioBot"); // Sets the name that goes into the middle of the GIF image
		title.setFont(new Font("Arial", Font.ITALIC, 140));
		title.setForeground(Color.white);
		title.setBounds(190, 325, 750, 150);
		this.add(title);
		
		JButton newgame = new JButton("NEW GAME"); // Sets the New Game Button text and action
		newgame.setForeground(Color.white);
		//newgame.setBackground(new Color(200, 200, 200, 100));
		newgame.setBackground(new Color(100, 100, 100));
		newgame.setFont(new Font("Arial", Font.BOLD, 20));
		newgame.addActionListener(new BGame());
		//newgame.setBounds(250, 150, 200, 50);
		newgame.setBounds(0, 730, 165, 50);
		this.add(newgame);
		
		JButton loadfromsave = new JButton("LOAD GAME"); // Sets the Load Game Button text and action
		loadfromsave.setForeground(Color.white);
		//loadfromsave.setBackground(new Color(200, 200, 200, 100));
		loadfromsave.setBackground(new Color(100, 100, 100));
		loadfromsave.setFont(new Font("Arial", Font.BOLD, 20));
		loadfromsave.addActionListener(new LoadSave());
		//loadfromsave.setBounds(250, 220, 200, 50);
		loadfromsave.setBounds(165, 730, 165, 50);
		this.add(loadfromsave);
		
		JButton instructions = new JButton("INSTRUCTIONS"); // Sets the Instructions Button text and action
		instructions.setForeground(Color.white);
		//instructions.setBackground(new Color(200, 200, 200, 100));
		instructions.setBackground(new Color(100, 100, 100));
		instructions.setFont(new Font("Arial", Font.BOLD, 20));
		instructions.addActionListener(new Instruct());
		//instructions.setBounds(250, 450, 200, 50);
		instructions.setBounds(330, 730, 195, 50);
		this.add(instructions);
		
		JButton highscore = new JButton("HIGHSCORE"); // Sets the HighScore Button text and action
		highscore.setForeground(Color.white);
		//highscore.setBackground(new Color(200, 200, 200, 100));
		highscore.setBackground(new Color(100, 100, 100));
		highscore.setFont(new Font("Arial", Font.BOLD, 20));
		highscore.addActionListener(new HighScores());
		//instructions.setBounds(250, 450, 200, 50);
		highscore.setBounds(525, 730, 175, 50);
		this.add(highscore);
		
		JButton quit = new JButton("QUIT"); // Sets the Quit Button text and action
		quit.setForeground(Color.white);
		//quit.setBackground(new Color(200, 200, 200, 100));
		quit.setBackground(new Color(100, 100, 100));
		quit.setFont(new Font("Arial", Font.BOLD, 20));
		quit.addActionListener(new QuitGame());
		//instructions.setBounds(250, 450, 200, 50);
		quit.setBounds(700, 730, 100, 50);
		this.add(quit);
		this.requestFocus();
	}
	public void paintComponent(Graphics b) {
		super.paintComponent(b);
		setBackground(Color.black); // Default background is black
		requestFocus(); // RequestsFocus to ensure that this panel has priority
		b.setColor(Color.black); // Sets color as black
		b.fillRect(0,  0, 800, 815); // Draws a large black rectangle
	}
	public void loadGIF() { // Loads the GIF used in the main menu
		img2 = null;
		try {
			img2 = new ImageIcon("MarsRotating4.gif"); 
		} catch (Exception e) { e.printStackTrace(); }
		tlabel = new JLabel(img2); // Initializes the label to hold the image
		tlabel.setBounds(0, 13, 800, 813); //used to be 0, 7, 800, 807, sets boundaries of the image
		Implement(); // Implements the image
		this.add(tlabel); // Adds the label
	}
	private class BGame implements ActionListener { // These actionlisteners change which panel is shown, if at all.
		public void actionPerformed (ActionEvent e) {
			System.out.println("[DEBUG]: RECEIVED BEGIN");
			BeginGame();
		}
	}
	private class Instruct implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			System.out.println("[DEBUG]: RECEIVED INSTRUCT");
			OpenInstructions();
		}
	}
	private class LoadSave implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			System.out.println("[DEBUG]: RECEIVED LOAD");
			LoadFromSave();
		}
	}
	private class QuitGame implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			System.out.println("[DEBUG]: RECEIVED QUIT");
			System.exit(1);
		}
	}
	private class HighScores implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			System.out.println("[DEBUG]: RECEIVED HIGHSCORES");
			LoadHighScores();
		}
	}
	public void BeginGame() { // Starts a regular game
		loadScreen.gamePanel.defaultVariables(); // Loads default variables
		gamePanel.lifeandstatus.setVisible(true); // Sets the game info panels as visible
		gamePanel.gameshop.setVisible(true);
		loadScreen.record("Game"); // Switches to the game panel
	}
	public void OpenInstructions() { // Switches over to the instructions panel
		loadScreen.record("Instructions");
	}
	public void LoadFromSave() { // Loads a save file
		loadScreen.gamePanel.initializeVariables(); // Loads from the save file
		gamePanel.lifeandstatus.setVisible(true); // Sets the game info panels as visible
		gamePanel.gameshop.setVisible(true);
		loadScreen.record("Game"); // Switches to the game panel
	}
	public void LoadHighScores() { // Switches over to the highscores panel
		loadScreen.record("HighScore");
	}
}
