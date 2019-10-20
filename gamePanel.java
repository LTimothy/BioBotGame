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

/**
 * Update Ideas:
 * Add Questions & Instructions & Fix HighScores Panel -> NOW DO HIGHSCORE PANEL + INSTRUCTIONS + COMMENTS
 * Work on RewardSystem(): eg maxvivion+ or maxresources+ & PunishmentSystem(); ~
 * Add Return to Home & More Game Settings ~ a bit
 * Polish Up Difficulty Scaling ~
 * Fix Capacity ~
 * Add File Verification ~
 * Fix HighScore thing ~
 * Improve color choices everywhere (menu buttons, jlabels in shop, etc.)
 * NOTES: update other menu for instructions, add shop components, add increases in difficulty by days past(dusts+, other threats+, resource-, flux-, etc), update instructions, make highscores, enable hide and show extra panels to clicker, add death, improve lifeandstatuspanel, add win, add mission objectives, resources do different things besides income? (bonus energy, health, and income), questions and drill effectiveness, night-time vision reduction + enemies/threats, add lava pits (last threat) rover can go over but takes damage, change days better (aka midnight changing) + change color of navbar accordingly, energy regen in day time only?, add maximum capacity of how much to hold, set a universal textarea on the lifeandstatus panel that shows notices for all panels
 */

import javax.imageio.ImageIO; // Imports necessary tools
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class gamePanel extends JPanel { // The Game Panel
	// High Level Variables
	static int fromload; // Keeps track of if the game was loaded
	public JPanel boardpanel; // Contains the game board
	public static JFrame questionframe; // The JFrame which contains the questions
	public static JFrame lifeandstatus; // The JFrame which contains the rover status updates
	public static JFrame gameshop; // The JFrame which contains the shop and the notification bar
	public File iofile, qfile, highfile; // File names to load/save from/to
	public PrintWriter output, highoutput; // Writers to write to files
	public Scanner input, qinput, highinput; // Readers to read from files
	public int daytracker; // Tracks the time of the day (out of 500)
	public JTextArea question; // Question Text Area
	public JRadioButton button1, button2, button3, button4; // Buttons for the question answers
	public String highscore, highscorename; // High Score Components for saving
	public String[][] hscore = new String[500][2]; // Loads 500 high scores, place 498 and 499 are used for saving in new high scores
	public String[][]questions = new String[500][6]; // Loads the questions (holds up to 500)
	public int currenthighscore; // Keeps track of the current high score
	public boolean gameover = false; // Game is not over by default
	public boolean griddisabled = true; // Grid is shown by default
	public boolean betatester = true; // Debugger's Hacks (PRESS R) to obtain additional information & reveals very descriptive information about the rover (perhaps question answers?)
	public double verifysave; // Verification code to ensure save files are not tampered with
	public JTextArea notifbar; // Notification Bar JLabel (some updates are shown here)
	public boolean gamecomplete = false, funding; // Game is not complete by default, funding boolean to determine if there is continous income
	public boolean gamestarted = false; // Game has not started by default (prevents dying -> highscore entering before game starts)
	// Normal Variables
	public int[][]rovloc = new int[25][25]; // Keeps the rover's coordinates on the grid
	public int[][]resources = new int[25][25]; // Keeps the location of resources on the grid
	public int[][]threats = new int[25][25]; // Keeps the location of threats on the grid
	public int rovx, rovy, deux, deuy, dustx, dusty, resx2, resy2, resy3, resx3, craterx, cratery; // Sets coordinates for different things
	public int[][]dusts = new int[500][4]; // Keeps track of the location of dusts on the grid
	public int[][]craterst = new int[25][25]; // Keeps track of the location of lava pits on the grid
	public int currentquestioncount = 0; // Default question is 0
	public int rovvision; // Sets your rover's field of view
	public int maxrovvision; // Sets maximum field of view
	public Image backgroundimage, roverimage, r1image, r2image, r3image, t1image, t2image; // Image for background, rover, 3 resources, and the 2 threats
	private boolean receivedpause = false; // Game starts without being paused
	public int resourcemax; // Total amount of resources on the board
	public static JProgressBar bar, energybar; // Keeps track of your rover's health and energy
	public int health = 1, maxhealth, energy, maxenergy, energyregenrate, healthregenrate; // Rover's health, maxhealth, maxenergy, and regeneration rates
	public boolean revealall; // Debugger's variables
	public double roverbucks; // Keeps track of how much money you have
	//public JTextArea def, exchan, rovstats, inventory; 
	public double r1e, r2e, r3e; // Exchange rates for each resource
	public int days; // Keeps track of days past
	public int dusttotal; // Keeps track of total amount of dust storms
	public int fluxcolorr, fluxcolorg, fluxcolorb, fluxgrade; // Keeps track of day color changing
	public boolean downtrend; // Keeps track of how which way the color is changing
	public Color fluxcom; // Keeps track of the color of the day
	public int maxquestions; // Keeps track of maximum questions
	public int r1amount, r2amount, r3amount; // Keeps track of the amount of each variable
	public int rrreduction; // Keeps track of the reduction amount for answering questions slower than 1 second
	public int drillmaxcollect; // Keeps track of the maximum amount of resources you can obtain from a question by default
	public int resourcetypeselected; // Keeps track of the type of resource selected
	public int craters; // Keeps track of the amount of Lava Pits
	public boolean invalid, spawnmoredust, capmet; // Keeps track of several invalid moves, if the game should spawn more dust, and if the maximum capacity for resources has been met
	public int maximumcapacity; // Maximum capacity for resources
	public int questionscorrect, questionswrong; // Keeps track of the amount of questions right/wrong.
	public gamePanel () {
		BorderLayout bor = new BorderLayout();
		this.setLayout(bor);
		this.setFocusable(true);
		JMenuBar menuBar; // The MenuBar (contains everything)
		JMenu menu, menu2; // The Drop Down Menus
		JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6, saveItem1, loadItem1, hideItems1, revealItems1, specialNotice1, specialNotice2, hsItem1; // Menu Items
		menuBar = new JMenuBar();
		//Builds the Menu and everything in it
		menu = new JMenu("Navigation");
		menu.setMnemonic(KeyEvent.VK_Q); // Shows a letter to represent the bar
		menu2 = new JMenu("Game Functions");
		menu2.setMnemonic(KeyEvent.VK_G); // Shows a letter to represent the bar
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
		specialNotice2 = new JMenuItem("Standard Use"); // Shows a notification for standard users
		menu2.add(specialNotice2);
		menu2.add(new JSeparator());
		saveItem1 = new JMenuItem("Save Game", KeyEvent.VK_S); // Launches the save game operation
		saveItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveItem1.addActionListener(new SwitchSave());
		menu2.add(saveItem1);
		loadItem1 = new JMenuItem("Load Game", KeyEvent.VK_L); // Launches the load game operation
		loadItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		loadItem1.addActionListener(new SwitchLoad());
		menu2.add(loadItem1);
		hideItems1 = new JMenuItem("Hide Extra Panels", KeyEvent.VK_H); // Launches the hide extra panels operation
		hideItems1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		hideItems1.addActionListener(new SwitchHide());
		menu2.add(hideItems1);
		revealItems1 = new JMenuItem("Reveal Extra Panels", KeyEvent.VK_R); // Launches the reveal extra panels operation
		revealItems1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		revealItems1.addActionListener(new SwitchReveal());
		menu2.add(revealItems1);
		menuBar.add(menu2);
		menuItem6 = new JMenuItem("Return To Home"); // Returns  to the home screen
		menuItem6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItem6.addActionListener(new SwitchB());
		menu2.add(menuItem6);
		menuBar.add(menuItem6);
		menuItem5 = new JMenuItem("Quit Game"); // Quits the game
		menuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		menuItem5.addActionListener(new SwitchQ());
		menuBar.add(menuItem5);
		menuBar.setPreferredSize(new Dimension(0, 15)); // Sets the dimensions of the menubar
		this.add(menuBar, BorderLayout.NORTH);
		//End of Menu
		
		if (fromload == 1) {fromload = 0; initializeVariables();} // If from load, initialize variables
		setBackgroundImage(); // Sets the background Image
		boardpanel = new gameBoard(); // Initializes the GameBoard and sets it in the center
		this.add(boardpanel, BorderLayout.CENTER);
		boardpanel.setSize(800, 800);
		this.setSize(805,805);
		
		questionframe = new JFrame("Question & Answer"); // Initializes an invisible Question Frame
		questionframe.setVisible(false);		
  		questionframe.setResizable(false); // Disables resizing of the frame
  		questionframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Disables the ability to close this frame
  		questionframe.setSize(416,440); // Sets the default size of the frame
		questionframe.setLocation(200, 200);
		JPanel questionPanel = new questionPanel();
		questionframe.add(questionPanel);
		questionPanel.setVisible(true);
		qfile = new File("questions.txt"); // Loads questions from this file
		
		highfile = new File("highscores.txt"); // Loads highscores from this file
		// Life and Status Panel
		lifeandstatus = new JFrame("Life & Status"); // Makes the life and status panel
		lifeandstatus.setVisible(false);
		lifeandstatus.setResizable(false);
		lifeandstatus.setFocusable(false);
		lifeandstatus.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevents closing
		lifeandstatus.setLocation(816, 510);
		lifeandstatus.setSize(300, 340);
		JPanel lfs = new lifeandstatusPanel();
		lifeandstatus.add(lfs, BorderLayout.CENTER);
		// Shop Panel
		gameshop = new JFrame("Shop"); // Makes the shop panel
		gameshop.setVisible(false);
		gameshop.setResizable(false);
		gameshop.setFocusable(true);
		gameshop.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevents closing
		gameshop.setLocation(816, 0);
		gameshop.setSize(300, 500);
		JPanel shop = new shopPanel();
		gameshop.add(shop, BorderLayout.CENTER);
		
		LoadQuestions(); // Loads the questions
		
		menuItem5.setBackground(Color.black); // Sets color scheme of the menu items
		menuItem5.setForeground(Color.white);
		menuItem6.setForeground(Color.white);
		menuItem6.setBackground(Color.black);
		menu.setForeground(Color.white);
		menu.setBackground(Color.black);
		menu2.setForeground(Color.white);
		menu2.setBackground(Color.black);
		menuBar.setBackground(Color.black);
		menuBar.setForeground(Color.white);
	}
	public void HighScoreLoad() { // Loads the HighScores from the file
		try {
			highinput = new Scanner(highfile);
		} catch(FileNotFoundException hfnf) {
			System.err.println("ERROR: File cannot be found");
			System.exit(5);
		}
		int count = 0;
		while (highinput.hasNext()) { // Loads all the high scores as saved from the file (should not go over 500 unless tampered with)
			String line = highinput.nextLine();
			hscore[count][0] = line.substring(0, line.indexOf(';'));
			hscore[count][0] = hscore[count][0].trim();
			line = line.substring(line.indexOf(';') + 1);
			hscore[count][1] = line.substring(0);
			hscore[count][1] = hscore[count][1].trim();
			count++;
		}
		highinput.close();
		for (int i = 0; i < 500; i++) { // If a highscore slot is null for the score, then it is 0
			if (hscore[i][0] == null) hscore[i][0] = "0";
		}
	}
	public void SaveHighScore() { // Saves the highscore
		receivedpause = true; // Pauses the game
		HighScoreLoad(); // Loads the high scores
		currenthighscore = (int)((r1amount * r1e) + (r2amount * r2e) + (r3amount * r3e) + roverbucks + (maxenergy * 8) + (maxhealth * 2) + (maxrovvision * 10) + (dusttotal * 2) + (energyregenrate * 10) + (healthregenrate * 5) + (craters * 2)) / ((days * questionswrong)/2 + 1) * ((days * questionscorrect)*2 + 1); // Equation to determine the highscore
		String name = JOptionPane.showInputDialog(this, "Game Score: " + currenthighscore + " | Your Name: "); // Asks for the user's name
		if (name == null || name.equals("")) name = "Guest";
		if (name.equals("highscores") || name.equals("questions") || name.equals("readme")) name = "Guest"; // Prevents deletion of essential files
		hscore[498][0] = String.format("%d", currenthighscore); // Loads the highscore into slot 498
		hscore[498][1] = name; // Loads the name into slot 498
		boolean swapped = true; // Default is true
	    int j = 0; // Offset
	    String score, user; // Keeps the score and name
	    while (swapped) { // While the file is not completely sorted (aka in order)
	        swapped = false; // Finishes by default
	        j++; // Lowers the amount of spaces to go through if completed
	        for (int i = 0; i < hscore.length - j; i++) { // Goes through each score all the way across
	        	int curr = Integer.valueOf(hscore[i][0]); // Keeps track of the current score
	        	int next = Integer.valueOf(hscore[i+1][0]); // Keeps track of the next score
	        	System.out.println("[DEBUG]: " + curr + " " + next); // DEBUG
	            if (curr < next) { // If the current score is smaller, moves it forward a back
	                score = hscore[i][0];
	                user = hscore[i][1];
	                hscore[i][0] = hscore[i + 1][0];
	                hscore[i][1] = hscore[i + 1][1];
	                hscore[i + 1][0] = score;
	                hscore[i + 1][1] = user;
	                swapped = true;
	            }
	        }
	    }
		try { // Loads the writer
			highoutput = new PrintWriter(highfile); 
		} catch (IOException s) {
			System.err.println("[ERROR]: Cannot modify save file");
			System.exit(4);
		}
	    for (int i = 0; i < 500; i++) { // Writes the highscores in order
	    	highoutput.println(hscore[i][0] + "; " + hscore[i][1]);
	    }
	    highoutput.close();
	    receivedpause = true; // Game still paused
		    if (gameover == true) { // Wipes the grid clean
		    for (int i = 0; i < 25; i++) {
				for (int a = 0; a < 25; a++) {
					threats[i][a] = 0;
					resources[i][a] = 0;
					rovloc[i][a] = 0;
					craterst[i][a] = 0;
				}
			}
			for (int i = 0; i < 500; i++) {
				dusts[i][0] = 0;
				dusts[i][1] = 0;
			}
	    }
		    if (gamecomplete) receivedpause = false; // If game is won, allow user to continue
		    gamestarted = false; // If game is now no longer in session
		    if (gamecomplete) gamestarted = true; // Unless game has been won and this method has been called
	}
	public void LoadQuestions() { // Loads the Questions & Answers
		try { // Loads the reader
			qinput = new Scanner (qfile);
		} catch(FileNotFoundException nfe) {
			System.err.println("[ERROR]: File cannot be found");
			//System.err.flush();
		}
		int count = 0; // 
		while(qinput.hasNext()) { // Goes through the entire question file and sets the Question, and 4 Answers's text
			String part = qinput.nextLine();
			questions[count][0] = part.substring(0, part.indexOf(';')); // Loads the question
			part = part.substring(part.indexOf(';') + 1);
			questions[count][1] = part.substring(0, part.indexOf(';')); // Loads the first answer
			part = part.substring(part.indexOf(';') + 1);
			questions[count][2] = part.substring(0, part.indexOf(';')); // Loads the second answer
			part = part.substring(part.indexOf(';') + 1);
			questions[count][3] = part.substring(0, part.indexOf(';')); // Loads the third answer
			part = part.substring(part.indexOf(';') + 1);
			questions[count][4] = part.substring(0, part.indexOf(';')); // Loads the fourth answer
			part = part.substring(part.indexOf(';') + 1);
			questions[count][5] = part.substring(0); // Loads the correct answer number
			questions[count][0] = questions[count][0].trim();
			questions[count][1] = questions[count][1].trim();
			questions[count][2] = questions[count][2].trim();
			questions[count][3] = questions[count][3].trim();
			questions[count][4] = questions[count][4].trim();
			questions[count][5] = questions[count][5].trim();
			//System.out.println(questions[count][0] + questions[count][1] + questions[count][2] + questions[count][3] + questions[count][4] + questions[count][5]);
			count++; // Increases the count of questions so we can load the entire file into the program
		}
		qinput.close();
	}
	public void initializeVariables () { // Loads from a save file
		SaveorLoadFile(); // Gets the name of this savefile
		for (int i = 0; i < 25; i++) { // Wipes the current grid clean
			for (int a = 0; a < 25; a++) {
				threats[i][a] = 0;
				resources[i][a] = 0;
				rovloc[i][a] = 0;
				craterst[i][a] = 0;
			}
		}
		for (int i = 0; i < 500; i++) { // Wipes the dust locations clean
			dusts[i][0] = 0;
			dusts[i][1] = 0;
		}
		boolean valid = true;
		try { // Opens the reader
			input = new Scanner (iofile);
		} catch(Exception fnfe) { // No Longer FileNotFoundException
			System.err.println("[ERROR]: File cannot be found");
			valid = false;
		}
		if (!valid) defaultVariables();
		if (valid) {
			days = input.nextInt(); // Loads the day value
			roverbucks = input.nextDouble(); // Loads the amount of money the user has
			r1e = input.nextDouble(); // Loads the exchange rates
			r2e = input.nextDouble();
			r3e = input.nextDouble();
			health = input.nextInt(); // Loads the health and maximum health
			maxhealth = input.nextInt();
			rovvision = input.nextInt(); // Loads the current amount of vision and the maximum vision
			maxrovvision = input.nextInt();
			resourcemax = input.nextInt(); // Loads the maximum amount of resources on the map
			revealall = input.nextBoolean();  // Debugger's Hack
			dusttotal = input.nextInt(); // Loads the amount of dust storms on the map
			rovx = input.nextInt(); // Loads the location of the rover
			rovy = input.nextInt();
			receivedpause = input.nextBoolean(); receivedpause = false; // nulled for now
			energy = input.nextInt(); // Loads the current energy and maximum energy amount
			maxenergy = input.nextInt();
			energyregenrate = input.nextInt(); // Loads the energy regen rate
			fluxcolorr = input.nextInt(); // Loads the color of the map
			fluxcolorg = input.nextInt();
			fluxcolorb = input.nextInt();
			fluxgrade = input.nextInt();
			downtrend = input.nextBoolean(); // Loads the current trend of the colors
			daytracker = input.nextInt(); // Loads the time of day
			currentquestioncount = input.nextInt(); // Loads the current question count (old uses)
			maxquestions = input.nextInt(); // Maximum amount of questions
			r1amount = input.nextInt(); // Loads the resources
			r2amount = input.nextInt();
			r3amount = input.nextInt();
			drillmaxcollect = input.nextInt(); // Loads the default maximum resource collection amount
			craters = input.nextInt(); // Loads the amount of lava pits on the map
			healthregenrate = input.nextInt(); // Loads the amount of health the rover will regenerate 
			maximumcapacity = input.nextInt(); // Loads the amount of capacity the rover has for resources
			questionscorrect = input.nextInt(); // Loads the amount of questions correct
			questionswrong = input.nextInt(); // Loads the amount of questions wrong
			gameover = input.nextBoolean(); // Loads if the game is over
			gamecomplete = input.nextBoolean(); // Loads if the last game objective is complete
			funding = input.nextBoolean(); // Loads if funding is happening
			verifysave = input.nextDouble(); // Verifies the save file
			input.close();
			fluxcom = (new Color(fluxcolorr, fluxcolorg, fluxcolorb, fluxgrade)); // Sets the default time of day color
			rovloc[rovx][rovy] = 1; // Sets the rover onto the map
			for (int i = 0; i < (int)(resourcemax / 2); i++) { // Spawns half of the original amount of resources
				boolean complete = false;
				while (complete != true) { // Tries again if location is taken
					int x = (int) (25 * Math.random());
					int y = (int) (25 * Math.random());
					int resourcetype = (int) (1 + 3 * Math.random());
					if (resources[x][y] == 0 && rovloc[x][y] == 0 && threats[x][y] == 0) { resources[x][y] = resourcetype; complete = true; break; }
				}
			}
			for (int d = 0; d < dusttotal; d++) { // Spawns the total amount of dust storms
				dustx = (int) (25 * Math.random());
				dusty = (int) (25 * Math.random());
				if (rovloc[dustx][dusty] != 0) d--;
				else if (resources[dustx][dusty] != 0) d--;
				else if (threats[dustx][dusty] != 0) d--;
				else { 
					threats[dustx][dusty] = 1; 
					dusts[d][0] = dustx;
					dusts[d][1] = dusty;
				} 
			}
			for (int i = 0; i < craters; i++) { // Spawns the total amount of lava pits
				craterx = (int) (25 * Math.random());
				cratery = (int) (25 * Math.random());
				if (rovloc[craterx][cratery] != 0) i--;
				else if (resources[craterx][cratery] != 0) i--;
				else if (threats[craterx][cratery] != 0) i--;
				else { 
					craterst[craterx][cratery] = 2; 
					threats[craterx][cratery] = 2;
				} 
				//System.out.println("(" + dustx + "," + dusty + ")");
			}
			repaint(); // Repaints
			//Reset variables with a predefined save file
			System.out.println("[DEBUG]: LF Variables Implemented"); // LF = Load File
			if (verifysave != ((r1e + 1) * (r2e + 1) * (r3e + 1) * (roverbucks + 1) * (days + 1) * (maxhealth + 1) * (maxrovvision + 1) * (resourcemax + 1) * (dusttotal + 1) * (rovx + 1) * (rovy + 1) * (maxenergy + 1) * (maxhealth + 1) * (energyregenrate + 1) * (healthregenrate + 1) * (currentquestioncount + 1) * (r1amount + 1) * (r2amount + 1) * (r3amount + 1) * (drillmaxcollect + 1) * (craters + 1) * (maximumcapacity + 1) * (questionscorrect + 1) * (questionswrong + 1))) { defaultVariables(); System.out.println("[ERROR]: Invalid Save File"); } // If the save file is invalid, loads default variables
			else {gamestarted = true; } // Otherwise, start the game
		}
	}
	public void defaultVariables () { // The Default Variables
		System.out.println("[DEBUG]: Default Variables Implemented");
		days = 1; // Starts on Day 1
		roverbucks = 0; // No $
		r1e = 5.0; // Default Exchange Rates
		r2e = 5.0;
		r3e = 5.0;
		health = 100; // 100 Health
		maxhealth = 100; // 100 Energy
		rovvision = 3; // 3 Vision
		maxrovvision = 3; // 3 Maximum Vision
		resourcemax = 25;  // 25 Total Resources
		revealall = false;
		dusttotal = 3; // Starts with 3 dust storms
		for (int i = 0; i < 25; i++) { // Wipes the grid clean
			for (int a = 0; a < 25; a++) {
				threats[i][a] = 0;
				resources[i][a] = 0;
				rovloc[i][a] = 0;
				craterst[i][a] = 0;
			}
		}
		for (int i = 0; i < 500; i++) {
			dusts[i][0] = 0;
			dusts[i][1] = 0;
		}
		rovx = (int) (25 * Math.random()); // Random Rover Spawn Loaction
		rovy = (int) (25 * Math.random());
		rovloc[rovx][rovy] = 1; // Sets the rover into position
		receivedpause = false;
		energyregenrate = 1; // 1 Energy Regen
		energy = 4; // 4 Energy
		maxenergy = 4; // 4 Total Energy
		fluxcolorr = 0; // Default color & gradient
		fluxcolorg = 0;
		fluxcolorb = 0;
		fluxgrade = 175;
		fluxcom = (new Color(fluxcolorr, fluxcolorg, fluxcolorb, fluxgrade)); // Sets the default color
		downtrend = true; // Day timer uses
		daytracker = 0; // Day starts at 0/500
		maxquestions = 60; maxquestions-=1; // Maximum Questions, code issues have been resolved with reduction of 1 (should be 59 - index)
		currentquestioncount = (int) (1 + maxquestions * Math.random()); // Starting Question is random
		r1amount = 0; // Starts without any resources
		r2amount = 0;
		r3amount = 0;
		drillmaxcollect = 3; // Maximum can collect 3 resources
		craters = 30; // Starts with 30 lava pits
		healthregenrate = 1; // 1 Health Regen
		invalid = false;
		maximumcapacity = 8; // Maximum Capacity is 8 for storing resources
		spawnmoredust = false; // Does not spawn any additional dust storms
		capmet = false; // Capacity has not been met by default
		currenthighscore = 0; // Score is 0
		questionscorrect = 0; // Question Correct is 0
		questionswrong = 0; // Questions Wrong is 0
		//Spawn Ore
		for (int i = 0; i < 4; i++) {
			deux = (int) (25 * Math.random());
			deuy = (int) (25 * Math.random());
			if (rovloc[deux][deuy] != 0) i--; // Prevents overlap
			else if (threats[deux][deuy] != 0) i--;
			else if (resources[deux][deuy] != 0) i--;
			else { resources[deux][deuy] = 1; }
		}
		// Spawn Crystal
		for (int i = 0; i < 4; i++) {
			resx2 = (int) (25 * Math.random());
			resy2 = (int) (25 * Math.random());
			
			if (rovloc[resx2][resy2] != 0) i--; // Prevents overlap
			else if (threats[resx2][resy2] != 0) i--;
			else if (resources[resx2][resy2] != 0) i--;
			else { resources[resx2][resy2] = 2; }
		}
		// Spawn Liquid
		for (int i = 0; i < 4; i++) {
			resx3 = (int) (25 * Math.random());
			resy3 = (int) (25 * Math.random());
			
			if (rovloc[resx3][resy3] != 0) i--; // Prevents overlap
			else if (threats[resx3][resy3] != 0) i--;
			else if (resources[resx3][resy3] != 0) i--;
			else { resources[resx3][resy3] = 3; }
		}
		//Spawn Dust Storms
		for (int i = 0; i < dusttotal; i++) {
			dustx = (int) (25 * Math.random());
			dusty = (int) (25 * Math.random());
			if (rovloc[dustx][dusty] != 0) i--; // Prevents overlap
			else if (resources[dustx][dusty] != 0) i--;
			else if (threats[dustx][dusty] != 0) i--;
			else { 
				threats[dustx][dusty] = 1; 
				dusts[i][0] = dustx;
				dusts[i][1] = dusty;
			} 
			//System.out.println("(" + dustx + "," + dusty + ")");
		}
		// Spawn Lava Pits
		for (int i = 0; i < craters; i++) {
			craterx = (int) (25 * Math.random());
			cratery = (int) (25 * Math.random());
			if (rovloc[craterx][cratery] != 0) i--;
			else if (resources[craterx][cratery] != 0) i--;
			else if (threats[craterx][cratery] != 0) i--;
			else { 
				craterst[craterx][cratery] = 2;
				threats[craterx][cratery] = 2; 
			} 
			//System.out.println("(" + dustx + "," + dusty + ")");
		}
		gamestarted = true; // Game has begun
	}
	private class SwitchP implements ActionListener { // Switches to the pre boot animation panel
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false || gamestarted == false) loadScreen.record("Pre");
		}
	}
	private class SwitchB implements ActionListener { // Switches to the boot panel
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false || gamestarted == false) loadScreen.record("Boot");
		}
	}
	private class SwitchG implements ActionListener { // Switches to the game panel
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false || gamestarted == false) loadScreen.record("Game");
		}
	}
	private class SwitchI implements ActionListener { // Switches to the instructions panel
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false || gamestarted == false) loadScreen.record("Instructions");
		}
	}
	private class SwitchH implements ActionListener { // Switches to the highscore panel
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false || gamestarted == false) loadScreen.record("HighScore");
		}
	}
	private class SwitchQ implements ActionListener { // Exits the game
		public void actionPerformed (ActionEvent m) {
			System.exit(1);
		}
	}
	public void SaveorLoadFile() { // Asks for the save file name
		String tempname = JOptionPane.showInputDialog(this, "SaveFile Name: ");
		iofile = new File("" + tempname + ".txt");
	}
	private class SwitchSave implements ActionListener { // Save the game
		public void actionPerformed (ActionEvent m) {
			System.out.println("[DEBUG]: Game Saved");
			SaveorLoadFile(); // Asks for a save file name
			if (receivedpause == false) {
				receivedpause = true;
				try { // Loads the writer
					output = new PrintWriter(iofile); 
				} catch (IOException s) {
					System.err.println("[ERROR]: Cannot modify save file");
					System.exit(2);
				}
				output.println(days + " " + roverbucks + " " + r1e + " " + r2e + " " + r3e); // Saves all the variables in. The format is recognized by the load operation
				output.println(health + " " + maxhealth + " " + rovvision + " " +  maxrovvision + " " + resourcemax);
				output.println(revealall + " " + dusttotal + " " + rovx + " " + rovy + " " + receivedpause);
				output.println(energy + " " + maxenergy + " " + energyregenrate + " " + fluxcolorr + " " + fluxcolorg);
				output.println(fluxcolorb + " " + fluxgrade + " " + downtrend + " " + daytracker + " " + currentquestioncount);
				output.println(maxquestions + " " + r1amount + " " + r2amount + " " + r3amount + " " + drillmaxcollect);
				output.println(craters + " " + healthregenrate + " " + maximumcapacity + " " + questionscorrect + " " + questionswrong);
				output.println(gameover + " " + gamecomplete + " " + funding);
				output.println((r1e + 1) * (r2e + 1) * (r3e + 1) * (roverbucks + 1) * (days + 1) * (maxhealth + 1) * (maxrovvision + 1) * (resourcemax + 1) * (dusttotal + 1) * (rovx + 1) * (rovy + 1) * (maxenergy + 1) * (maxhealth + 1) * (energyregenrate + 1) * (healthregenrate + 1) * (currentquestioncount + 1) * (r1amount + 1) * (r2amount + 1) * (r3amount + 1) * (drillmaxcollect + 1) * (craters + 1) * (maximumcapacity + 1) * (questionscorrect + 1) * (questionswrong + 1)); // VERIFICATION CODE
				output.close();
				System.out.println("SAVE COMPLETE");
				receivedpause = false; // Game has been resumed
			}
		}
	}
	private class SwitchLoad implements ActionListener { // Loads the Game
		public void actionPerformed (ActionEvent m) {
			if (receivedpause == false) initializeVariables();
		}
	}
	private class SwitchHide implements ActionListener { // Hides the Information Panels
		public void actionPerformed (ActionEvent m) {
			lifeandstatus.setVisible(false);
			gameshop.setVisible(false);
		}
	}
	private class SwitchReveal implements ActionListener { // Reveals the Information Panels
		public void actionPerformed (ActionEvent m) {
			lifeandstatus.setVisible(true);
			gameshop.setVisible(true);
		}
	}
	public void setBackgroundImage() { // Loads all the images used
		String imagename = "marsmap3.jpg"; //http://i.telegraph.co.uk/multimedia/archive/01590/3d_1590101i.jpg
		backgroundimage = null;
	    try { // Does this
	       	backgroundimage = ImageIO.read(new File(imagename)); // Loads the passed image into img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file MAP");
			System.exit(1); // Exits
		}
	    String threat1 = "threat1.png";
	    t1image = null;
	    try { // Does this
	       	t1image = ImageIO.read(new File(threat1)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file THREAT 1");
			System.exit(1); // Exits
		}
	    String threat2 = "threat2.png";
	    t2image = null;
	    try { // Does this
	       	t2image = ImageIO.read(new File(threat2)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file THREAT 2");
			System.exit(1); // Exits
		}
	    String roverimage1 = "rimage.png";
	    roverimage = null;
	    try { // Does this
	       	roverimage = ImageIO.read(new File(roverimage1)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file ROVER");
			System.exit(1); // Exits
		}
	    String resourceimage1 = "r1image.png";
	    r1image = null;
	    try { // Does this
	       	r1image = ImageIO.read(new File(resourceimage1)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file RESOURCE 1");
			System.exit(1); // Exits
		}
	    String resourceimage2 = "r2image.png";
	    r2image = null;
	    try { // Does this
	       	r2image = ImageIO.read(new File(resourceimage2)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file RESOURCE 2");
			System.exit(1); // Exits
		}
	    String resourceimage3 = "r3image.png";
	    r3image = null;
	    try { // Does this
	       	r3image = ImageIO.read(new File(resourceimage3)); // Loads the passed image to img
		} catch (IOException e) { // If image does not exist...
			System.err.println("ERROR: Cannot open image file RESOURCE 3");
			System.exit(1); // Exits
		}
	}
	
	class shopPanel extends JPanel { // The Shop
		public JButton upenergyregen, uphealthregen, upenergymax, uphealthmax, upmaxcapacity, updrill, sellresource1, sellresource2, sellresource3, healrepair, energyrepair; // The buttons for the shop
		public Refresher refreshed = new Refresher(); 
		public Timer refresh = new Timer(500, refreshed); // Timer refreshes the pricing
		public Notification clear = new Notification();
		public Timer cleared = new Timer(15000, clear); // Timer refreshes the notification bar
		public float hpregencost, energyregencost, healthmaxcost, energymaxcost, updrillcost, upmaxcapacitycost, energyrepaircost, healrepaircost; // Keeps track of the costs of these items
		public shopPanel() {
			this.setLayout(new GridLayout(10, 0)); // Grid Layout
			this.setFocusable(true);
			refresh.start();
			JLabel shopsign = new JLabel("               BioBot Shop"); // Title
			shopsign.setFocusable(false);
			shopsign.setForeground(Color.white);
			shopsign.setFont(new Font("Serif", Font.ITALIC, 24));
			this.add(shopsign);
			JPanel sells = new JPanel(); // Panel containing the 3 buttons to sell resources
			sells.setLayout(new GridLayout(0, 3));
			sellresource1 = new JButton("Sell Ore"); // Allows selling of all the ore resources
			sellresource1.addActionListener(new SellResource());
			sellresource1.setForeground(Color.white);
			sellresource1.setBackground(Color.black);
			sells.add(sellresource1);
			sellresource2 = new JButton("Sell Crystal"); // Allows selling of all the crystal resources
			sellresource2.addActionListener(new SellResource());
			sellresource2.setForeground(Color.white);
			sellresource2.setBackground(Color.black);
			sells.add(sellresource2);
			sellresource3 = new JButton("Sell Liquid"); // Allows selling of all the liquid resources
			sellresource3.addActionListener(new SellResource());
			sellresource3.setForeground(Color.white);
			sellresource3.setBackground(Color.black);
			sells.add(sellresource3);
			this.add(sells);
			JLabel regensign = new JLabel("            Upgrade Regenerate Rates:"); // SubHeading
			regensign.setFocusable(false);
			regensign.setForeground(Color.white);
			regensign.setFont(new Font("Arial", Font.BOLD, 14));
			this.add(regensign);
			JPanel regenrates = new JPanel(); // Panel containing the buttons to upgrade regeneration rates
			regenrates.setLayout(new GridLayout(0, 2));
			uphealthregen = new JButton("HP");
			uphealthregen.addActionListener(new UpRegen());
			uphealthregen.setForeground(Color.white);
			uphealthregen.setBackground(Color.black);
			regenrates.add(uphealthregen);
			upenergyregen = new JButton("ENERGY");
			upenergyregen.addActionListener(new UpRegen());
			upenergyregen.setForeground(Color.white);
			upenergyregen.setBackground(Color.black);
			regenrates.add(upenergyregen);
			this.add(regenrates);
			
			JLabel maxincreasesign = new JLabel("            Upgrade Maximum Amount:"); // SubHeading
			maxincreasesign.setFocusable(false);
			maxincreasesign.setForeground(Color.white);
			maxincreasesign.setFont(new Font("Arial", Font.BOLD, 14));
			this.add(maxincreasesign);
			JPanel maxincrease = new JPanel(); // Panel containing the maximum capacity upgrade buttons
			maxincrease.setLayout(new GridLayout(0, 2));
			uphealthmax = new JButton("HP"); // HP Upgrade Button
			uphealthmax.addActionListener(new UpRegen());
			uphealthmax.setForeground(Color.white);
			uphealthmax.setBackground(Color.black);
			maxincrease.add(uphealthmax);
			upenergymax = new JButton("ENERGY"); // ENERGY Upgrade Button
			upenergymax.addActionListener(new UpRegen());
			upenergymax.setForeground(Color.white);
			upenergymax.setBackground(Color.black);
			maxincrease.add(upenergymax);
			this.add(maxincrease);
			
			JLabel components = new JLabel("                Upgrade Components:"); // SubHeading
			components.setFocusable(false);
			components.setForeground(Color.white);
			components.setFont(new Font("Arial", Font.BOLD, 14));
			this.add(components);
			JPanel maxcap = new JPanel(); // Panel that contains the buttons to upgrade the drill and storage/capacity
			maxcap.setLayout(new GridLayout(0, 2));
			upmaxcapacity = new JButton("STORAGE"); // STORAGE Upgrade Button
			upmaxcapacity.addActionListener(new UpRegen()); 
			upmaxcapacity.setForeground(Color.white);
			upmaxcapacity.setBackground(Color.black);
			maxcap.add(upmaxcapacity);
			updrill = new JButton("DRILL"); // DRILL Upgrade Button
			updrill.addActionListener(new UpRegen());
			updrill.setForeground(Color.white);
			updrill.setBackground(Color.black);
			maxcap.add(updrill);
			this.add(maxcap);
			
			JPanel temporary = new JPanel(); // Panel that contains the ability to heal and recharge
			temporary.setLayout(new GridLayout(0, 2));
			healrepair = new JButton("REPAIR"); // REPAIR Button
			healrepair.addActionListener(new UpRegen()); 
			healrepair.setForeground(Color.white);
			healrepair.setBackground(Color.black);
			temporary.add(healrepair);
			energyrepair = new JButton("RECHARGE"); // RECHARGE Button
			energyrepair.addActionListener(new UpRegen());
			energyrepair.setForeground(Color.white);
			energyrepair.setBackground(Color.black);
			temporary.add(energyrepair);
			this.add(temporary);
			
			notifbar = new JTextArea(); // The Notification Bar
			notifbar.setFont(new Font("Arial", Font.BOLD, 14));
			notifbar.setFocusable(false);
			notifbar.setLineWrap(true);
			notifbar.setWrapStyleWord(true);
			notifbar.setEditable(false);
			notifbar.setForeground(Color.white);
			notifbar.setBackground(Color.black);
			this.add(notifbar);
			
			cleared.start();
		}
		private class SellResource implements ActionListener { // Sells the resource that has been selected, then reduces the exchange rates by a fraction of what was gained
			public void actionPerformed (ActionEvent l) {
				if (l.getSource() == sellresource1) { 
					if (r1amount == 0) { invalid = true; notifbar.setText("Insufficient Resources"); }
					if (r1amount > 0) {
						roverbucks += r1amount * r1e;
						double randomdecrease = (1 * Math.random());
						r1e-= (r1amount * r1e) - randomdecrease;
						r1amount = 0;
						notifbar.setText("Sold Ore Resource(s)"); // Notifies the user of this event
					}
				}
				if (l.getSource() == sellresource2) {
					if (r2amount == 0) { invalid = true; notifbar.setText("Insufficient Resources"); }
					if (r2amount > 0) {
						roverbucks += r2amount * r2e;
						double randomdecrease = (1 * Math.random());
						r2e-= (r2amount * r2e) - randomdecrease;
						r2amount = 0;
						notifbar.setText("Sold Crystal Resource(s)"); // Notifies the user of this event
					}
				}
				if (l.getSource() == sellresource3) {
					if (r3amount == 0) { invalid = true; notifbar.setText("Insufficient Resources"); }
					if (r3amount > 0) {
						roverbucks += r3amount * r3e;
						double randomdecrease = (1 * Math.random());
						r3e-= (r3amount) * r3e - randomdecrease;
						r3amount = 0;
						notifbar.setText("Sold Liquid Resource(s)"); // Notifies the user of this event
					}
				}
			}
		}
		private class Notification implements ActionListener { // Clears the notification bar
			public void actionPerformed (ActionEvent n) {
				notifbar.setText("");
				notifbar.getText();
				notifbar.setForeground(Color.white); // Allows for color options
			}
		}
		private class Refresher implements ActionListener { // Refreshes all the components
			public void actionPerformed (ActionEvent l) {
				// Sets the prices of the upgrades
				hpregencost = (float) ((float) (maxhealth / 10) * 12.5 * 0.8 * healthregenrate * 2); // 0.8 buffed
				energyregencost = (float) ((float) (maxenergy / 2) * 32 * 0.8 * (energyregenrate * 5));
				healthmaxcost = (float) ((maxhealth / 8) * healthregenrate * 3.5);
				energymaxcost = (float) ((maxenergy / 1.5) * 18 * energyregenrate);
				updrillcost = (float) ((float) (resourcemax / 6) * 4.5 * 1.2 * (drillmaxcollect * 4)); // 1.2 nerf
				upmaxcapacitycost = (float) ((float) (maximumcapacity * 3) * 8 * 0.4 * (drillmaxcollect / 3)); //0.4 buffed
				energyrepaircost = (float) (maxenergy - energy) * 5;
				healrepaircost = (float) (maxhealth - health) * 5;
				
				// Sets the minimum prices and then sets the text for the buttons
				if (hpregencost < 20) hpregencost = 30;
				if (energyregencost < 20) energyregencost = 30; // BUG: $20 patched.
				String hpreg = String.format("%.2f", hpregencost);
				String energyreg = String.format("%.2f", energyregencost);
				uphealthregen.setText("HP (+1): $" + hpreg);
				upenergyregen.setText("ENERGY: $" + energyreg);
				if (healthmaxcost < 20) healthmaxcost = 20;
				if (energymaxcost < 20) energymaxcost = 20;
				String hpmax = String.format("%.2f", healthmaxcost);
				String energymax = String.format("%.2f", energymaxcost);
				uphealthmax.setText("HP (+10): $" + hpmax);
				upenergymax.setText("ENERGY: $" + energymax);
				if (updrillcost < 80) updrillcost = 80;
				if (upmaxcapacitycost < 42) upmaxcapacitycost = 42;
				String udcost = String.format("%.2f", updrillcost);
				String umcost = String.format("%.2f", upmaxcapacitycost);
				String engcost = String.format("%.2f", energyrepaircost);
				String repaircost = String.format("%.2f", healrepaircost);
				updrill.setText("Drill: $" + udcost);
				upmaxcapacity.setText("Storage: $" + umcost);
				energyrepair.setText("Recharge: $" + engcost);
				healrepair.setText("Repair: $" + repaircost);
			}
		}
		private class UpRegen implements ActionListener { // Allows the user to buy upgrades. If they can afford the cost is reduced from the user's money supply, and the upgrade is given
			public void actionPerformed (ActionEvent l) {
				if (l.getSource() == upmaxcapacity) { // Maximum Resource Capacity Upgrade
					if (roverbucks >= upmaxcapacitycost) {
						roverbucks -= upmaxcapacitycost;
						maximumcapacity++;
						notifbar.setText("Upgraded Maximum Resource Capacity");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
				if (l.getSource() == energyrepair) { // Buys energy at the price of 5 per energy
					while (roverbucks >= 5 && energy < maxenergy) {
						energy++;
						roverbucks-=5;
						notifbar.setText("Restored Energy");
					}
					invalid = true;
				}
				if (l.getSource() == healrepair && health < maxhealth) { // Buys health at the price of 5 per health point
					while (roverbucks >= 5 && health <= maxhealth) {
						health++;
						roverbucks-=5;
						notifbar.setText("Restored Health");
					}
					invalid = true;
				}
				if (l.getSource() == updrill) { // Drill Upgrade
					if (roverbucks >= updrillcost) {
						roverbucks -= updrillcost;
						drillmaxcollect++;
						notifbar.setText("Upgraded Drill");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
				if (l.getSource() == uphealthregen) { // Life Regen Upgrade
					if (roverbucks >= hpregencost) {
						roverbucks -= hpregencost;
						healthregenrate++;
						notifbar.setText("Upgraded Health Regeneration Rate");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
				if (l.getSource() == upenergyregen) { // Energy Regen Upgrade
					if (roverbucks >= energyregencost) {
						roverbucks -= energyregencost;
						energyregenrate++;
						notifbar.setText("Upgraded Energy Regeneration Rate");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
				if (l.getSource() == upenergymax) { // Maximum Energy Upgrade
					if (roverbucks >= energymaxcost) {
						roverbucks -= energymaxcost;
						maxenergy++;
						notifbar.setText("Upgraded Maximum Energy Level");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
				if (l.getSource() == uphealthmax) { // Maximum Health Upgrade
					if (roverbucks >= healthmaxcost) {
						roverbucks -= healthmaxcost;
						maxhealth+=10;
						notifbar.setText("Upgraded Maximum Health Amount");
					}
					else { // If not enough money, notifies the user of this event
						invalid = true;
						notifbar.setText("Insufficient Resources");
					}
				}
			}
		}
		public void paintComponent(Graphics s) {
			super.paintComponent(s);
			setBackground(Color.black); // Background is black to show contrast and match with color scheme
			requestFocus(); // Requests focus, has priority over other panels
		}
	}
	
	class lifeandstatusPanel extends JPanel { // Updates of the rover's status
		public String sr1e, sr2e, sr3e, roverbuckssimple; // Simplified exchange rates and money
		public LifeUpdate lup = new LifeUpdate();
		public Timer lifeupdate = new Timer(100, lup); // Timer will update the life and status panel components
		public JLabel notes[] = new JLabel[16]; // The components
		public JPanel canvas, canvas2; // For easy implementation of the components and their spacings
		public lifeandstatusPanel() {
			this.setLayout(new GridLayout(4, 0)); // Holds 4 Items: 2 Progress Bars, and 2 JPanels containing the components
			bar = new JProgressBar(0,maxhealth);
			bar.setValue(health);
			bar.setForeground(Color.green); // Green for health
			bar.setBackground(Color.black);
			bar.setStringPainted(true);
			bar.setString("Health"); // Labels this as the health bar
			//bar.setFont(new Font("Arial", Font.BOLD, 20)); 
			//bar.setString("Health: " + health);
			//bar.getString();
			energybar = new JProgressBar(0,maxenergy);
			energybar.setValue(energy);
			energybar.setForeground(Color.cyan); // Cyan for energy
			energybar.setBackground(Color.black);
			energybar.setStringPainted(true);
			energybar.setString("Energy"); // Lables this as the energy amount bar
			lifeupdate.start();
			
			for (int i = 0; i < 16; i++) { // Initializes the components
				notes[i] = new JLabel("Loading..."); // Default message
				notes[i].setFont(new Font("Serif", Font.BOLD, 12));
				notes[i].setForeground(Color.white);
				notes[i].setBackground(Color.black);
			}
			
			// The Canvas(s) hold 8 components each
			canvas = new JPanel();
			canvas.setLayout(new GridLayout(4, 2));
			canvas.setBackground(Color.black);
			canvas2 = new JPanel();
			canvas2.setLayout(new GridLayout(4, 2));
			canvas2.setBackground(Color.black);
			// Simiplifies the double values
			roverbuckssimple = String.format("%.2f", roverbucks);
			sr1e = String.format("%.2f", r1e);
			sr2e = String.format("%.2f", r2e);
			sr3e = String.format("%.2f", r3e);
			
			notes[0].setText("Day: " + days); // Sets the Text of the components
			notes[1].setText("Rover$: " + roverbuckssimple);
			notes[2].setText("Connection Lv: " + rovvision + "/" + maxrovvision);
			notes[3].setText("Ore Exchange: " + sr1e);
			notes[4].setText("Crystal Exchange: " + sr2e);
			notes[5].setText("Liquid Exchange: " + sr3e);
			notes[6].setText("Health: " + health + "/" + maxhealth);
			notes[7].setText("Energy: " + energy + "/" + maxenergy);
			for (int i = 0; i < 8; i++) { // Adds the components in and repaints and validates to ensure proper function
				canvas.add(notes[i]);
				canvas.revalidate();
				canvas.repaint();
			}
			
			notes[8].setText("HP Regen: " + healthregenrate + "/800ms"); // Sets the Text of the components
			notes[9].setText("Energy Regen: " + energyregenrate + "/800ms");
			notes[10].setText("Capacity: " + maximumcapacity);
			notes[11].setText("Drill Lv. " + drillmaxcollect);
			notes[12].setText("Ore Amount: " + r1amount);
			notes[13].setText("Crystal Amount: " + r2amount);
			notes[14].setText("Liquid Amount: " + r3amount);
			notes[15].setText("Day Timer: " + daytracker + "/500");
			for (int i = 8; i < 16; i++) { // Adds the components in and repaints and validates to ensure proper function
				canvas2.add(notes[i]);
				canvas2.revalidate();
				canvas2.repaint();
			}
			
			this.add(bar); // Adds the components
			this.add(energybar);
			this.add(canvas);
			this.add(canvas2);
		}
		public void paintComponent(Graphics ls) {
			super.paintComponent(ls);
			setBackground(Color.black);
			// Sets the color of the energy and health bar according to the amount of health/energy each has
			bar.setMaximum(maxhealth); // Sets the maximum amount value
			bar.setValue(health); // Sets the current amount
			if (health < (int)(maxhealth / 5)) { bar.setForeground(Color.red); notes[6].setForeground(Color.red); } // Color changes depending on the amount
			else if (health < (int)(maxhealth / 3)) { bar.setForeground(Color.orange); notes[6].setForeground(Color.yellow); }  // Color changes depending on the amount
			else { bar.setForeground(Color.green); notes[6].setForeground(Color.white);} // Color changes depending on the amount
			energybar.setMaximum(maxenergy); // Sets the maximum amount value
			energybar.setValue(energy); // Sets the current amount
			if (energy < (int)(maxenergy / 4)) { energybar.setForeground(Color.magenta); notes[7].setForeground(Color.magenta); } // Color changes depending on the amount
			else if (energy < (int)(maxenergy / 2)) { energybar.setForeground(Color.yellow); notes[7].setForeground(Color.yellow); } // Color changes depending on the amount
			else { energybar.setForeground(Color.cyan); notes[7].setForeground(Color.white);} // Color changes depending on the amount
			// Sets the total capacity color depending on how full it is
			if (r1amount + r2amount + r3amount >= maximumcapacity) notes[10].setForeground(Color.orange);
			else { notes[10].setForeground(Color.white); }
			if (capmet) notes[10].setForeground(Color.cyan);
			else if (!capmet) notes[10].setForeground(Color.white);
			//Sets the color of selling resources differently if you do not have the option to do so
			if (invalid) { notes[14].setForeground(Color.red); notes[12].setForeground(Color.red); notes[13].setForeground(Color.red); notes[1].setForeground(Color.red);}
			else if (!invalid) { notes[14].setForeground(Color.white); notes[13].setForeground(Color.white); notes[12].setForeground(Color.white); notes[1].setForeground(Color.white);}
			
			// Refreshes the text of the components
			notes[0].setText("Day: " + days);
			notes[1].setText("Rover$: " + roverbuckssimple);
			notes[2].setText("Connection Lv: " + rovvision + "/" + maxrovvision);
			if (rovvision <= (int) maxrovvision /2) notes[2].setForeground(Color.yellow);
			if (rovvision <= 0) { notes[2].setText("Connection Lv: LOST"); notes[2].setForeground(Color.magenta); } // Changes text accordingly if vision is not existant
			else {notes[2].setForeground(Color.white);}
			notes[3].setText("Ore Exchange: " + sr1e);
			notes[4].setText("Crystal Exchange: " + sr2e);
			notes[5].setText("Liquid Exchange: " + sr3e);
			notes[6].setText("Health: " + health + "/" + maxhealth);
			notes[7].setText("Energy: " + energy + "/" + maxenergy);
			notes[8].setText("HP Regen: " + healthregenrate + "/800ms");
			notes[9].setText("Energy Regen: " + energyregenrate + "/800ms");
			notes[10].setText("Capacity: " + maximumcapacity);
			notes[11].setText("Drill Lv. " + drillmaxcollect);
			notes[12].setText("Ore Amount: " + r1amount);
			notes[13].setText("Crystal Amount: " + r2amount);
			notes[14].setText("Liquid Amount: " + r3amount);
			notes[15].setText("Day Timer: " + daytracker + "/500");
			// Revalidates and repaints to ensure optimal accuracy
			canvas.revalidate();
			canvas2.revalidate();
			canvas.repaint();
			canvas2.repaint();
		}
		private class LifeUpdate implements ActionListener { // Calls to refresh the components
			public void actionPerformed (ActionEvent l) {
				if (receivedpause != true || gameover == true) {
					//System.out.println("LIFE PANEL UPDATED ONCE");
					repaint();
					// Adjusted so energy cannot go negative, and simple formatting is used for doubles
					roverbuckssimple = String.format("%.2f", roverbucks);
					sr1e = String.format("%.2f", r1e);
					sr2e = String.format("%.2f", r2e);
					sr3e = String.format("%.2f", r3e);
					if (energy <= 0) energy = 0; // Energy cannot go negative
				}
			}
		}
	}
	
	class questionPanel extends JPanel { // Questions Panel
		public ButtonGroup choices;
		public questionPanel() { 
			this.setLayout(new GridLayout(6, 0)); // Holds 6 components
			// Initializes the components which are the question and the 4 answer text areas/buttons
			question = new JTextArea("QUESTION"); 
			question.setEditable(false);
			question.setLineWrap(true);
			question.setWrapStyleWord(true);
			question.setFocusable(false);
			button1 = new JRadioButton("ANSWER 1");
			button2 = new JRadioButton("ANSWER 2");
			button3 = new JRadioButton("ANSWER 3");
			button4 = new JRadioButton("ANSWER 4");
			choices = new ButtonGroup(); // Combines the buttons into a buttongroup so only one can be selected at a time
			choices.add(button1);
			choices.add(button2);
			choices.add(button3);
			choices.add(button4);
			JButton submit = new JButton("SUBMIT");
			submit.addActionListener(new Submitted()); // Does something upon submitting
			// Adds the components
			this.add(question);
			this.add(button1);
			this.add(button2);
			this.add(button3);
			this.add(button4);
			this.add(submit);
		}
		public void paintComponent(Graphics q) {
			super.paintComponent(q);
			setBackground(Color.white); // Default is white
			requestFocus(); // Has priority and requests focus
		}
		private class Submitted implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				System.out.println("[QUESTIONS] Received Submit");
				// Do Something
				questionframe.setVisible(false);
				receivedpause = false;
				String correctans = questions[currentquestioncount][5]; // Checks which answer is correct
				//System.out.println("correctans: " + correctans);
				int tempvarincrease = drillmaxcollect - rrreduction; // Creates reduction according to how much time has past
				if (tempvarincrease <= 1) tempvarincrease = 1; // Minimum resource gain is 1
				//Backups of the previous resource amount in case that the cap is reached (For Future Development)
				int br1a, br2a, br3a;
				br1a = r1amount;
				br2a = r2amount;
				br3a = r3amount;
				if (button1.isSelected()) {
					if (correctans.equals("1")) {
						 // If the first button is selected and the answer is correct
							notifbar.setForeground(Color.green);
						switch(resourcetypeselected) {
						case 1: r1amount += (tempvarincrease * 2); notifbar.setText("[CORRECT]: Collected: Ore: " + (tempvarincrease * 2)); break;
						case 2: r2amount += tempvarincrease; health+=(healthregenrate * 5); notifbar.setText("[CORRECT] Collected: Crystal: " + tempvarincrease +  " | Health + " + (healthregenrate * 5));break;
						case 3: r3amount += tempvarincrease; energy+=(energyregenrate * 2); notifbar.setText("[CORRECT] Collected: Liquid: " + tempvarincrease + " | Energy + " + (energyregenrate * 2));break;
						}
						questionscorrect++; RewardSystem();
					}
					else { spawnmoredust = true; questionswrong++; PunishmentSystem();} // If the answer is wrong, spawn a dust and check for penalty
				} 
				else if (button2.isSelected()) {
					if (correctans.equals("2"))  {
						 // If the second button is selected and the answer is correct
							notifbar.setForeground(Color.green);
						switch(resourcetypeselected) {
						case 1: r1amount += (tempvarincrease * 2); notifbar.setText("[CORRECT]: Collected: Ore: " + (tempvarincrease * 2)); break;
						case 2: r2amount += tempvarincrease; health+=(healthregenrate * 5); notifbar.setText("[CORRECT] Collected: Crystal: " + tempvarincrease +  " | Health + " + (healthregenrate * 5));break;
						case 3: r3amount += tempvarincrease; energy+=(energyregenrate * 2); notifbar.setText("[CORRECT] Collected: Liquid: " + tempvarincrease + " | Energy + " + (energyregenrate * 2));break;
						}
						questionscorrect++; RewardSystem();
					} 
					else { spawnmoredust = true; questionswrong++; PunishmentSystem();}//If the answer is wrong, spawn a dust and check for penalty
				}
				else if (button3.isSelected()) {
					if (correctans.equals("3")) { 
						 // If the third button is selected and the answer is correct
							notifbar.setForeground(Color.green);
						switch(resourcetypeselected) {
						case 1: r1amount += (tempvarincrease * 2); notifbar.setText("[CORRECT]: Collected: Ore: " + (tempvarincrease * 2)); break;
						case 2: r2amount += tempvarincrease; health+=(healthregenrate * 5); notifbar.setText("[CORRECT] Collected: Crystal: " + tempvarincrease +  " | Health + " + (healthregenrate * 5));break;
						case 3: r3amount += tempvarincrease; energy+=(energyregenrate * 2); notifbar.setText("[CORRECT] Collected: Liquid: " + tempvarincrease + " | Energy + " + (energyregenrate * 2));break;
						}
						questionscorrect++; RewardSystem();
					} 
					else { spawnmoredust = true; questionswrong++; PunishmentSystem();} // If the answer is wrong, spawn a dust and check for penalty
				}
				else if (button4.isSelected()) {
					if (correctans.equals("4")) {
						 // If the fourth button is selected and the answer is correct
							notifbar.setForeground(Color.green);
						switch(resourcetypeselected) {
						case 1: r1amount += (tempvarincrease * 2); notifbar.setText("[CORRECT]: Collected: Ore: " + (tempvarincrease * 2)); break;
						case 2: r2amount += tempvarincrease; health+=(healthregenrate * 5); notifbar.setText("[CORRECT] Collected: Crystal: " + tempvarincrease +  " | Health + " + (healthregenrate * 5));break;
						case 3: r3amount += tempvarincrease; energy+=(energyregenrate * 2); notifbar.setText("[CORRECT] Collected: Liquid: " + tempvarincrease + " | Energy + " + (energyregenrate * 2));break;
						}
						questionscorrect++; RewardSystem();
					} 
					else { spawnmoredust = true; questionswrong++; PunishmentSystem();}// If the answer is wrong, spawn a dust and check for penalty
				}
				else { spawnmoredust = true; questionswrong++; PunishmentSystem();}
				
				if ((r1amount + r2amount + r2amount) >= maximumcapacity) { // In the case that there are more resources than the maximum capacity, fixes these errors
					if (resourcetypeselected == 1) {
						r1amount = maximumcapacity - r2amount - r3amount;
					}
					else if (resourcetypeselected == 2) {
						r2amount = maximumcapacity - r1amount - r3amount;
					}
					else if (resourcetypeselected == 3) {
						r3amount = maximumcapacity - r2amount - r1amount;
					}
				}
				if (spawnmoredust == true) { dusttotal++; Spawnadust(); }// PUNISHMENT FOR INCORRECT ANSWER (More Dust Storms)
				currentquestioncount = (int) (1 + maxquestions * Math.random()); // Changes to another randomly chosen question
				questionframe.setVisible(false); // Frame is still invisible
				choices.clearSelection(); // The previous selection is cleared
			}
		}
		public void Spawnadust() { // Spawns a dust, if location is taken, tries again
			boolean complete = false;
			while (complete != true) { // Tries again if space is taken
				dustx = (int) (25 * Math.random());
				dusty = (int) (25 * Math.random());
				if (rovloc[dustx][dusty] != 0) complete = false;
				else if (resources[dustx][dusty] != 0) complete = false;
				else if (threats[dustx][dusty] != 0) complete = false;
				else { 
					threats[dustx][dusty] = 1; 
					dusts[dusttotal - 1][0] = dustx;
					dusts[dusttotal - 1][1] = dusty;
					complete = true;
				} 
			}
			spawnmoredust = false;
		}
		public void RewardSystem() { // Rewards that are given when the user gets a certain amount of questions correct
			switch(questionscorrect) {
			case 5: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project Fund has been increased!"); roverbucks += 180; receivedpause = false;break; // Increase amount of $ by a bit
			case 10: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has figured out how to identify resources better!"); resourcemax += 5; receivedpause = false;break; // Increases amount of resources on the map by a bit
			case 22: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project is now being constantly funded by a investor!"); roverbucks += 300; funding = true; receivedpause = false;break; // Enables continous funding and give a bit of $
			case 30: receivedpause = true; JOptionPane.showMessageDialog(this, "The resources found have demand back on Earth!"); r1e+=7; r2e+=7; r3e+=7; receivedpause = false;break; // Increases exchange rates by a bit
			case 38: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has invested in your rover's vision!"); maxrovvision++; receivedpause = false;break; // Increases vision by a bit
			case 46: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has invested in your rover's drill!"); drillmaxcollect+=2;receivedpause = false;break; // Increases the capability of the drill
			case 54: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has invested in your rover's battery and plating!"); maxenergy+=3; maxhealth+=100; receivedpause = false;break; // Increases overall stats
			case 68: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has helped make breakthroughs!"); roverbucks += 1500; receivedpause = false;break; // Give a large $ bonus
			case 76: receivedpause = true; JOptionPane.showMessageDialog(this, "The resources found have high demand back on Earth!"); r1e+=20; r2e+=20; r3e+=20; receivedpause = false;break; // Increases the exchange rates by a large amount
			case 85: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has invested in your rover's solar capabilities!"); energyregenrate++; receivedpause = false;break; // Increases the energy regen rate by a bit
			case 95: receivedpause = true; JOptionPane.showMessageDialog(this, "JONES has invested in your rover's repair capabilities!"); healthregenrate++;receivedpause = false;break; // Increases the health regen rate by a bit
			case 110: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has been completed! You have successfully completed the objective of the project, congradulations!"); gamecomplete = true; roverbucks += 3000;receivedpause = false;break; // Wins the game, save the first highscore, and give a $ bonus
			}
		}
		public void PunishmentSystem() { // Punishments that are given when the user gets a certain amount of questions wrong
			String correctans = questions[currentquestioncount][5];
			notifbar.setText("[WRONG] Correct Answer was " + questions[currentquestioncount][Integer.parseInt(correctans)] + ".");
			notifbar.setForeground(Color.red);
			switch(questionswrong) {
			case 5: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has lost some support!"); roverbucks -= 50; if (roverbucks < 0) roverbucks = 0;receivedpause = false;break; // Decreases $ by a bit
			case 8: receivedpause = true; JOptionPane.showMessageDialog(this, "The once expected abundance of resources doesn't seem to exist anymore!"); resourcemax -= 5; receivedpause = false;break; // Decreases overall amount of resources on the map
			case 10: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has lost even more support!"); roverbucks -= 100; if (roverbucks < 0) roverbucks = 0; receivedpause = false;break; // Decreases the amount of money by a bit
			case 15: receivedpause = true; JOptionPane.showMessageDialog(this, "The resources found have lost research potential!"); r1e-=5; r2e-=5; r3e-=5; receivedpause = false;break; // Lowers exchange rates by a bit
			case 22: receivedpause = true; JOptionPane.showMessageDialog(this, "Funding for rover vision have been cut!"); maxrovvision--;receivedpause = false; break; // Decreases overall vision
			case 29: receivedpause = true; JOptionPane.showMessageDialog(this, "Funding for the drill has been cut!"); drillmaxcollect-=2;receivedpause = false;break; // Decreases overall drill effectiveness
			case 35: receivedpause = true; JOptionPane.showMessageDialog(this, "Your rover's plating and battery have been severly damaged!"); maxenergy-=3; maxhealth-=70; receivedpause = false;break; // Decreases overal stats
			case 40: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has provided inaccurate information for researchers!"); roverbucks -= 500; if (roverbucks < 0) roverbucks = 0; funding = false;receivedpause = false; break; // Large $ reduction
			case 45: receivedpause = true; JOptionPane.showMessageDialog(this, "The resources found have severly lost research potential"); r1e-=10; r2e-=10; r3e-=10; receivedpause = false;break; // Decreases the exchange rates
			case 50: receivedpause = true; JOptionPane.showMessageDialog(this, "Your solar panels no longer work as effectively as before!"); energyregenrate-=2; if (energyregenrate < 1) energyregenrate = 1; receivedpause = false;break; // Decreases the energy regen potential 
			case 55: receivedpause = true; JOptionPane.showMessageDialog(this, "Your rover's repair abilities no longer work as effectively as before!"); healthregenrate-=3; if (healthregenrate < 0) healthregenrate = 0;receivedpause = false;break; // Decreases the health regen potential by a lot
			case 60: receivedpause = true; JOptionPane.showMessageDialog(this, "The JONES Project has been shut down."); gameover = true;receivedpause = false;break; // Game Over.
			}
		}
	}
	
	class gameBoard extends JPanel implements KeyListener, MouseListener {
		public DustMove dmove = new DustMove();
		public Timer dusttimer = new Timer(1000, dmove); // Moves the dust
		public Restore restore = new Restore();
		public Timer restored = new Timer(4000, restore); // Restores vision
		public Respawn respawn = new Respawn();
		public Timer respawned = new Timer(2000, respawn); // Respawns resources
		public FluxRates flux = new FluxRates();
		public Timer fluxrate = new Timer(1000, flux); // Changes the exchange rates
		public DayUpdate dup = new DayUpdate();
		public Timer duptimer = new Timer(25, dup); // Updates the time of day
		public EnergyRegen energyreg = new EnergyRegen();
		public Timer eregen = new Timer(800, energyreg); // Regenerates Energy & Health
		public ColorFlux rgbf = new ColorFlux();
		public Timer fluxc = new Timer(50, rgbf); // Updates the color of the time of day
		public ResourceReward rrprocess = new ResourceReward();
		public Timer rrtimer = new Timer(1000, rrprocess); // Reduces the amount of resources collected accordingly
		public gameBoard() {
			this.setFocusable(true);
			addKeyListener(this);
			addMouseListener(this);
			
			// Starts the timers
			dusttimer.start();
			restored.start();
			respawned.start();
			fluxrate.start();
			duptimer.start();
			eregen.start();
			fluxc.start();
			rrtimer.start();
		}
		private class FluxRates implements ActionListener { // Changes the rates of the shop
			public void actionPerformed (ActionEvent r) {
				if (receivedpause != true) {
					//System.out.println("FLUX RATES UPDATED ONCE");
					double r1flux = (1 * Math.random()); // The amount of decrease or increase to the exchange rates
					double r2flux = (1 * Math.random());
					double r3flux = (1 * Math.random());
					int econresult1 = (int) (1 + 2 * Math.random()); // Chooses whether a increase or decrease
					int econresult2 = (int) (1 + 2 * Math.random());
					int econresult3 = (int) (1 + 2 * Math.random());
					if (r1e <= 0) { // Exchange rates cannot go negative before the reduction or bonus
						econresult1 = 2;
						r1e = 0;
					}
					if (r2e <= 0) {
						econresult2 = 2;
						r2e = 0;
					}
					if (r3e <= 0) {
						econresult3 = 2;
						r3e = 0;
					}
					if (econresult1 == 1) r1e -= r1flux; // Applies the reduction or bonus
					if (econresult1 == 2) r1e += r1flux;
					if (econresult2 == 1) r2e -= r2flux;
					if (econresult2 == 2) r2e += r2flux;
					if (econresult3 == 1) r3e -= r3flux;
					if (econresult3 == 2) r3e += r3flux;
					if (r1e <= 0) { // Prevents negative exchange rates after the reduction or bonus
						r1e = 0.01;
					}
					if (r2e <= 0) {
						r2e = 0.01;
					}
					if (r3e <= 0) {
						r3e = 0.01;
					}
				}
			}
		}
		private class ColorFlux implements ActionListener { // Changes the color of the time of day accordingly
			public void actionPerformed (ActionEvent r) {
				if (receivedpause != true) {
					//System.out.println("COLOR UPDATED ONCE");
					if (fluxcolorg > 0 && downtrend == true) { fluxcolorr--; fluxcolorg--; fluxcolorb--; }
					else if (fluxcolorg >= 250) downtrend = true;
					else if (fluxcolorg <= 0 || downtrend == false) { downtrend = false; fluxcolorr++; fluxcolorg++; fluxcolorb++; }
					fluxcom = (new Color(fluxcolorr, fluxcolorg, fluxcolorb, fluxgrade));
					daytracker++;
					repaint();
				}
			}
		}
		private class DayUpdate implements ActionListener { // Updates the time of day
			public void actionPerformed (ActionEvent r) {
				// Also checks if game is over, if so, saves the high score and exits to menu
				if (health < 0 && gamestarted == true) { // 0 health = died
					health = 0;
					gameover = true;
				}
				if (gameover) { // If Game Is Over (possible run: died or 60 wrong questions)
					receivedpause = true;
					SaveHighScore(); // Saves the user's score
					//defaultVariables();
					lifeandstatus.setVisible(false);
					gameshop.setVisible(false);
					loadScreen.record("Boot");
					gameover = false; // This part on... resets the board and game to factory state
					health = 1; 
					gamecomplete = false;
					gamestarted = false;
					receivedpause = true;
					repaint();
				}
				// Checks if the game is complete, if so, saves the high score and allows continued play
				if (gamecomplete) { // Possible calls (if 110 questions correct) -> game is complete
					receivedpause = true;
					SaveHighScore(); // Saves the score
					gamecomplete = false;
				}
				if (funding) { // Gives additional income periodically
					if (daytracker % 100 == 0) {
						roverbucks+=((days/10) + 1);
					}
				}
				if (receivedpause != true) { // Standard Time of Day Modifiers // Runs if game is not paused (due to questions)
					if (daytracker == 500) { // New Day
						System.out.println("[GAME] Advanced in Time (Day)");
						days++; 
						daytracker = 0;
						if (days % 6 == 0) { // DIFFICULTY SCALING
							dusttotal++;
						}
						craters++;
						if (craters > 156) craters = 156;
					}
					if (daytracker % 50 == 0 && r1e < 20.0 && r2e < 20.0 && r3e < 20.0) { r1e+=0.8; r2e+=0.8; r3e+=0.8; } // NATURAL INFLATION RATES
					if (daytracker == 0) { // If Day has Ended
						for (int x = 0; x < 25; x++) { // New locations for lava pits
							for (int y = 0; y < 25; y++) {
								if (threats[x][y] != 0 && threats[x][y] != 1) threats[x][y] = 0;
								craterst[x][y] = 0;
							}
						}
						for (int i = 0; i < craters; i++) { // Relocates all lava pits
							craterx = (int) (25 * Math.random());
							cratery = (int) (25 * Math.random());
							if (rovloc[craterx][cratery] != 0) i--;
							else if (resources[craterx][cratery] != 0) i--;
							else if (threats[craterx][cratery] != 0) i--;
							else { 
								craterst[craterx][cratery] = 2;
								threats[craterx][cratery] = 2; 
							} 
						}
					}
					if (rovloc[rovx][rovy] == threats[rovx][rovy] -1) { // If rover is on a crater, does continuous damage
						health--;
					}
				}
			}
		}
		private class ResourceReward implements ActionListener { // Increases the realtime resource reduction variable
			public void actionPerformed (ActionEvent r) {
				if (receivedpause == true) { // Runs if game is not paused (due to questions)
					System.out.println("[QUESTIONS] +1 Reduction");
					rrreduction++; // Increases the amount of reduction
				}
			}
		}
		private class EnergyRegen implements ActionListener { // Regenerates energy and health (reduces if above max)
			public void actionPerformed (ActionEvent r) {
				if (receivedpause != true) { // Runs if game is not paused (due to questions)
					if (energy < maxenergy) energy+=energyregenrate;
					if (energy > maxenergy) energy--;
					if (health < maxhealth) health+=healthregenrate;
					if (health > maxhealth) health--;
				}
			}
		}
		private class Restore implements ActionListener { // Restores vision & invalid and capmet booleans
			public void actionPerformed (ActionEvent r) {
				if (receivedpause != true) { // Runs if game is not paused (due to questions)
					if (rovvision < maxrovvision) rovvision++;
					if (rovvision > maxrovvision) rovvision--;
				}
				invalid = false; // Resets the red text (notification for not enough resources)
				capmet = false; // Resets the maximum cap boolean
			}
		}
		private class Respawn implements ActionListener { // Respawns resources
			public void actionPerformed (ActionEvent r) {
				if (receivedpause != true) { // Runs if game is not paused (due to questions)
					int tempcount = 0;
					for (int i = 0; i < 25; i++) {
						for (int a = 0; a < 25; a++) {
							if (resources[i][a] != 0) tempcount++;
						}
					}
					boolean complete = false;
					while (complete != true) {
						if (tempcount < resourcemax) { // Tries again if space is taken
							int x = (int) (25 * Math.random());
							int y = (int) (25 * Math.random());
							int resourcetype = (int) (1 + 3 * Math.random());
							if (resources[x][y] == 0 && rovloc[x][y] == 0 && threats[x][y] == 0) { resources[x][y] = resourcetype; complete = true; break; }
							else {complete = false;}
						}
						else { complete = true; }
					}
				}
			}
		}
		private class DustMove implements ActionListener { // Moves Dust Around
			public void actionPerformed (ActionEvent e) {
				if (receivedpause != true) { // Runs if game is not paused (due to questions)
					for (int x = 0; x < 25; x++) { // Wipes all previous locations for lava pits
						for (int y = 0; y < 25; y++) {
							if (threats[x][y] == 1) threats[x][y] = 0;
							if (craterst[x][y] != 0) threats[x][y] = 2; // Sets new lava pit location
						}
					}
					for (int times = 0; times < dusttotal; times++) {
						dusts[times][2] = dusts[times][0];
						dusts[times][3] = dusts[times][1];
						int randmovement = (int)(1 + 9 * Math.random());
						switch (randmovement) { // Can Move in any 8 directions, including standing still
						case 1: dusts[times][1] = dusts[times][1] - 1; break;
						case 2: dusts[times][0] = dusts[times][0] - 1; break;
						case 3: dusts[times][1] = dusts[times][1] + 1; break;
						case 4: dusts[times][0] = dusts[times][0] + 1; break;
						case 5: dusts[times][1] = dusts[times][1] - 1; dusts[times][0] = dusts[times][0] - 1; break;
						case 6: dusts[times][1] = dusts[times][1] - 1; dusts[times][0] = dusts[times][0] + 1; break;
						case 7: dusts[times][1] = dusts[times][1] + 1; dusts[times][0] = dusts[times][0] - 1; break;
						case 8: dusts[times][1] = dusts[times][1] + 1; dusts[times][0] = dusts[times][0] + 1; break;
						case 9: break;
						}
						if (dusts[times][0] < 0 || dusts[times][0] > 24) dusts[times][0] = dusts[times][2];
						if (dusts[times][1] < 0 || dusts[times][1] > 24) dusts[times][1] = dusts[times][3];
						threats[dusts[times][0]][dusts[times][1]] = 1;
						int hpreduction = days / 2; // Reduces health according to half the amount of days past
						if (hpreduction < 10) hpreduction = 10; // Minimum of 10
						int energyreduction = (int) Math.round(0.1 * maxenergy); // Reduces energy according to 10% of total energy
						if (energyreduction < 1) energyreduction = 1; // Minimum of 1
						if (dusts[times][0] == rovx && dusts[times][1] == rovy) { // Inflicts on the rover if the rover is in the spot
							rovvision--;
							health-=(int)hpreduction;
							energy-=(int)energyreduction;
						}
					}
					repaint(); // Repaints
				}
			}
		}
		
		public void paintComponent(Graphics g) { // Paints the GUI
			super.paintComponent(g);
			setBackground(Color.blue);
			g.drawImage(backgroundimage, 0, 0, 800, 800, this); // Paints the background image
			if (!griddisabled) { // Prints the grid
				for (int i = 0; i <= 64; i++) {
					g.setColor(Color.white);
					g.drawLine(0, (16)*i, 800, (16)*i); // Draws the grid
					g.drawLine((16)*i, 0, (16)*i, 800);
					i++;
				}
			}
			if (receivedpause != true) requestFocus(); // Requests focus if game is not paused
			if (revealall) { // Debugger's Hack
				for (int row = 0; row < 25; row++) {
					for (int col = 0; col < 25; col++) {
						if (rovloc[row][col] == 1) { g.setColor(new Color(180, 180, 180, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(roverimage, row * 32, col * 32, 32, 32, this); } // ROVER
						if (resources[row][col] == 1) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r1image, row * 32, col * 32, 32, 32, this); } // ROCK ORE
						if (resources[row][col] == 2) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r2image, row * 32, col * 32, 32, 32, this); } // CRYSTAL 
						if (resources[row][col] == 3) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r3image, row * 32, col * 32, 32, 32, this); } // LIQUID
						if (threats[row][col] == 1) { g.setColor(new Color(180, 100, 100, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(t1image, row * 32, col * 32, 32, 32, this); } // DUST STORMS
						if (threats[row][col] == 2) { g.setColor(new Color(180, 100, 100, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(t2image, row * 32, col * 32, 32, 32, this); } // LAVA PITS
					}
				}
				revealall = false;
			}
			for (int row = 0; row < 25; row++) { // Allows field of view
				for (int col = 0; col < 25; col++) {
					if (Math.abs(row - rovx) + Math.abs(col - rovy) < rovvision && gamestarted == true) { // Gives the field of view a value of 4 grid spaces: meaning the rover can only see within 4 spaces
						if (rovloc[row][col] == 1) { g.setColor(new Color(180, 180, 180, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(roverimage, row * 32, col * 32, 32, 32, this); } // ROVER
						if (resources[row][col] == 1) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r1image, row * 32, col * 32, 32, 32, this); } // ROCK ORE
						if (resources[row][col] == 2) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r2image, row * 32, col * 32, 32, 32, this); } // CRYSTAL 
						if (resources[row][col] == 3) { g.setColor(new Color(150, 255, 102, 180)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(r3image, row * 32, col * 32, 32, 32, this); } // LIQUID
						if (threats[row][col] == 1) { g.setColor(new Color(180, 100, 100, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(t1image, row * 32, col * 32, 32, 32, this); } // DUST STORMS
						if (threats[row][col] == 2) { g.setColor(new Color(180, 100, 100, 150)); g.fillRect(row * 32, col * 32, 32, 32); g.drawImage(t2image, row * 32, col * 32, 32, 32, this); } // LAVA PITS
					}
					else {
						//g.setColor(new Color(70, 70, 70, 150)); g.fillRect(row * 32, col * 32, 32, 32);
						g.setColor(fluxcom); g.fillRect(row * 32, col * 32, 32, 32); // If not within the field of view, it will just be a dark square
					}
				}
			}
		}
		public void keyPressed(KeyEvent arg0) { // When the key is pressed
			if (receivedpause != true && gameover == false) { // If game is not paused
				System.out.println("[DEBUG] Key Pressed");
				int oldx = rovx; // Keeps track of old location
				int oldy = rovy;
				rovloc[rovx][rovy] = 0;
				if (arg0.getKeyCode() == KeyEvent.VK_UP && energy > 0) { rovy -= 1; energy--; } // Moves the rover and consumes a energy point
				else if (arg0.getKeyCode() == KeyEvent.VK_LEFT && energy > 0) { rovx -= 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_DOWN && energy > 0) { rovy += 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && energy > 0) { rovx += 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_W && energy > 0) { rovy -= 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_A && energy > 0) { rovx -= 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_S && energy > 0) { rovy += 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_D && energy > 0) { rovx += 1; energy--; }
				else if (arg0.getKeyCode() == KeyEvent.VK_R && betatester == true) { //DEBUG HACKS
					revealall = true; 
					roverbucks+=10000;
					rovvision++;
					if (rovvision > 10) rovvision = 10;
					days++;
				}
				if (rovx < 0 || rovx > 24) rovx = oldx; // If out of bounds, restore old location
				if (rovy < 0 || rovy > 24) rovy = oldy;
				rovloc[rovx][rovy] = 1; // Sets the location of the rover
				if (resources[rovx][rovy] != 0) { // If a resource is picked up
					receivedpause = true; // Pauses the game
					resourcetypeselected = resources[rovx][rovy]; // Records what type is picked up
					System.out.println("[GAME] Resource Collected");
					setQuestions(); // Loads the questions & answers
					rrreduction = 0; // Default reduction is 0
					questionframe.setVisible(true); // Opens the question
					questionframe.repaint();
					questionframe.requestFocus();
					resources[rovx][rovy] = 0; 
				}
				int hpreduction = days / 2;
				if (hpreduction < 10) hpreduction = 10;
				int energyreduction = (int) Math.round(0.1 * maxenergy);
				if (energyreduction < 1) energyreduction = 1;
				if (threats[rovx][rovy] == 1) { // If touched a dust storm
					rovvision--;
					health-=(int)hpreduction;
					energy-=(int)energyreduction;
				}
				if (threats[rovx][rovy] == 2) { // If touched a crater
					health-=(int)(hpreduction * 2);
				}
				repaint();
				revalidate();
			}
		}
		public void setQuestions() { // Sets the question and prints a debug of the question
			if (betatester) System.out.println("[DEBUG]: " + questions[currentquestioncount][0] + questions[currentquestioncount][1] + questions[currentquestioncount][2] + questions[currentquestioncount][3] + questions[currentquestioncount][4] + questions[currentquestioncount][5]);
			question.setText(questions[currentquestioncount][0]);
			button1.setText(questions[currentquestioncount][1]);
			button2.setText(questions[currentquestioncount][2]);
			button3.setText(questions[currentquestioncount][3]);
			button4.setText(questions[currentquestioncount][4]);
		}
		public void keyReleased(KeyEvent arg0) {
			System.out.println("[DEBUG] Key Released");
		}
		public void keyTyped(KeyEvent arg0) { // Required Unused...
		}
		public void mouseClicked(MouseEvent arg0) {
		}
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {
		}
		public void mousePressed(MouseEvent arg0) {
		}
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
