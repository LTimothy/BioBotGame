/**
 * Copyright (C) 2014 Timothy Lee - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the GNU General Public License v3.0.
 *
 * You should have received a copy of the GNU General
 * Public License v3.0 with this file. If not, please
 * contact: timothyl@berkeley.edu, or visit:
 * https://github.com/LTimothy/BioBotGame
 */

import java.awt.Color; // Imports necessary Tools
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class instructionsPanel extends JPanel implements MouseListener, KeyListener { // Instructions
	private int panelnum = 0; // Starts on the first instruction
	public Image parts[] = new Image[5]; // 5 sets of images
	public JTextArea lables[] = new JTextArea[5]; // 5 sets of instructions for the images
	public instructionsPanel () {
		// Makes the Menu
		this.setLayout(null);
		this.setFocusable(true);
		JMenuBar menuBar; // The Menu's Bar
		JMenu menu, menu2; // The Drop Down Menus
		JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, saveItem1, loadItem1, hideItems1, revealItems1, specialNotice1, hsItem1; // Menu Items
		menuBar = new JMenuBar();
		//Build the first menu.
		menu = new JMenu("Navigation"); // The Navigation Bar
		menu.setMnemonic(KeyEvent.VK_Q); // Shows a letter for the notification bar
		menu2 = new JMenu("Game Functions"); // The Game Functions Bar
		menu2.setMnemonic(KeyEvent.VK_G); // Shows a letter for the game functions bar
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
		//end of first menu
		
		// Initializes the Menu Variables and Settings
		for (int i = 0; i < 5; i++) {
			lables[i] = new JTextArea("Loading...");
			lables[i].setLineWrap(true); // Wraps Lines
			lables[i].setWrapStyleWord(true); // Wraps Words
			lables[i].setBackground(new Color(120, 120, 120)); // Default Text Area Color
			lables[i].setForeground(Color.white); // Default Text Color
			lables[i].setEditable(false); // Not Editable
			lables[i].setVisible(false); // Not Visible
			lables[i].setFocusable(false); // Not Focusable
			lables[i].setFont(new Font("Arial", Font.PLAIN, 18)); // Sets the Font
			lables[i].addMouseListener(this); // Also accepts listener
			this.add(lables[i]);
		}
		// Sets where the instructions are to go
		lables[0].setBounds(300, 15, 500, 800);
		lables[1].setBounds(0, 15, 400, 800);
		lables[2].setBounds(0, 15, 800, 200);
		lables[3].setBounds(0, 15, 350, 800);
		lables[4].setBounds(310, 15, 490, 800);
		
		// Sets the instructions text
		lables[0].setText("\n\n\n\n\nWelcome to BioBot! A review program to study for Biology, in particular for dissection vocabulary. \n\nYou are the rover. The rover has a white aura which fills it's box to represent where it is. The diamond around the rover represents it's field of view. \n\nUsing the arrow keys (or WASD) you are able to move your rover in 4 directions (up, down, left, right). \n\nYour rover moves at the speed of 1 space per click/hold towards the direction of your choice. The amount of spaces is limited to the amount of energy you have. Energy is recharged naturally or through the collection of (liquid) resources. \n\nYour goal is to collect resources, sell resources, and buy upgrades to fulfill your task. \n\nTASK: JONES Co. has created a new project (aka The Jones Project) in which a rover will be placed on the planet of Marr. Your objective is successfully collect a total of 110 individual resources (tiles) for scientific analysis. When this is completed, you have completed the game. Your score will then be saved, and you will be given the option to continue.\n\nPress any key to continue...");
		lables[1].setText("\n\n\n\n\nThe rover may collect resources of 3 different types. \n\nOre: Ore is a simple resource to collect. Your drill was made to mine ore and you will receive 2x the amount of resources collected. \n\nCrystal: Your drill has difficulties mining crystal and will collect 1x the amount of resources. In order to make up for this issue, you will restore health equal to 5x your health regeneration rate. \n\nLiquid: Your rover also has difficulties collecting liquid and will collect 1x the amount of resources. In order to make up for this issue, you will restore energy to 2x the amount of your energy regeneration rate. \n\nYour drill's effectiveness is determined by your drill's level. Each second spent on the question will reduce the amount of resources that can be obtained from the resource (tile). The minimum amount of resources that you can collect is 1. The maximum is equal to your drill's level, which is by default 3.");
		lables[2].setText("When you land on a resource tile, a question will popup, and a timer that calculates the amount of resources reduced will begin. The faster you answer the question, the lesser the resource reduction will be. The minimum resources you can collect if the question is correct is 1. Upgrading the drill effectiveness will enable you to have a higher default resource collected amount, which will allow you to potentially collect more resources.\n\nIf you answer enough questions correct or wrong you will receive a bonus or punishment respectively.");
		lables[3].setText("\n\n\n\n\n\n\nThe rover may experience 2 different types of moving threats. \n\nLava Pit: Does initial damage upon hit (scales by days), and then do continuous damage. These relocate at the end of each day due to the cooling during the night on planet Marr. These threats increase by the amount of days past.\n\nDust Storm: Does initial damage upon hit (scales by days) and reduces your vision (scales by your maximum vision). Dust Storms move around the map and increase when you answer a question wrong. \n\nLosing the Game: At 60 questions wrong, The Jones Project will be shut down and the game will end.");
		lables[4].setText("\n\nThe rover status window and the shop window will give you updates on your rover's status and potential upgrades. \n\nSelling: The sell options will sell all of one resource in which you select.\n\nRegeneration Rates: Upgrading the regeneration rates will increase the amount of energy/health regen you obtain every 800ms. \n\nMaximum Capacity/Components: Upgrading the maximum capacity/components will allow your rover to have better plating, storage, or drill, thus upgrading the general stats of your rover.\n\nRepair & Refill: The repair and refill options are boosts in which you may choose to repair or energize your rover with. \n\nNotification Bar: The last section of the shop window will show any updates the game has and would like to inform you about.\n\nThe rover status window will update you on your rover's current conditions, as well as general knowledge (consisting of the day, time, and the current resource exchange rates). \n\nThe menu bar has options in which you may choose to use to navigate, some options may only be accessed in certain parts of the program.\n\nNew to this Game? The recommended upgrade items are energy regeneration and drill level.");
		
		// Sets components to match color scheme
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
		
		// Adds a Mouse & Key Listener
		addMouseListener(this);
		addKeyListener(this);
		
		// Loads the Images
		LoadImages();
	}
	public void LoadImages() { // Loads the iamges to be used
		String imagename = "instructions1.jpg"; 
		parts[0] = null;
	    try { // Does this
	        parts[0] = ImageIO.read(new File(imagename)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open instructions image file 1");
			System.exit(1); // Exits
		}
	    String imagename2 = "instructions2.jpg"; 
		parts[1] = null;
	    try { // Does this
	        parts[1] = ImageIO.read(new File(imagename2)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open instructions image file 2");
			System.exit(1); // Exits
		}
	    String imagename3 = "instructions3.JPG"; 
		parts[2] = null;
	    try { // Does this
	        parts[2] = ImageIO.read(new File(imagename3)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open instructions image file 3");
			System.exit(1); // Exits
		}
	    String imagename4 = "instructions4.JPG"; 
		parts[3] = null;
	    try { // Does this
	        parts[3] = ImageIO.read(new File(imagename4)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open instructions image file 4");
			System.exit(1); // Exits
		}
	    String imagename5 = "instructions5.JPG";
		parts[4] = null;
	    try { // Does this
	        parts[4] = ImageIO.read(new File(imagename5)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open instructions image file 5");
			System.exit(1); // Exits
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
	public void paintComponent(Graphics i) { // Paints the Image
		super.paintComponent(i);
		setBackground(new Color(120, 120, 120));
		lables[panelnum].setVisible(true);
		for (int b = 0; b < 5; b++) {
			if (b != panelnum) lables[b].setVisible(false); // Sets all but the active instruction text area as not visible
		}
		if (panelnum != 4 && panelnum != 2) i.drawImage(parts[panelnum], 0, 15, 800, 800, this); // Paints the image differently depending on which instruction is supposed to be shown
		else if (panelnum == 2) i.drawImage(parts[panelnum], 0, 215, 800, 600, this);
		else if (panelnum == 4) i.drawImage(parts[panelnum], 0, 15, 300, 800, this);
		requestFocus();
	}
	public void keyPressed(KeyEvent e) { // Changes the Image & Message Shown when a key is pressed
		panelnum++;
		if (panelnum > 4) panelnum = 0; // Loops Around
		repaint();
	}
	public void keyReleased(KeyEvent e) { // Changes the Image & Message Shown when a key is pressed
	}
	public void keyTyped(KeyEvent e) { // Required Unused...
	}
	public void mouseClicked(MouseEvent arg1) { 
	}
	public void mouseEntered(MouseEvent arg1) { 
	}
	public void mouseExited(MouseEvent arg1) {
	}
	public void mousePressed(MouseEvent arg1) {
		panelnum++;
		if (panelnum > 4) panelnum = 0; // Loops Around
		repaint();
	}
	public void mouseReleased(MouseEvent arg1) { // Required Unused...
	}
}
