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

import java.awt.CardLayout; // For CardLayout
import javax.swing.JPanel; // For JPanel

public class loadScreen extends JPanel {
	public static CardLayout mainpanels; // Allows static reference to the mainpanels
	public static JPanel bootScreen, instructionsPanel, preBoot, highScorePanel; // Default JPanels for Card Layout
	public static gamePanel gamePanel = new gamePanel(); // Allows static reference to the game
	public loadScreen () {
		mainpanels = new CardLayout(); // Initializes Main JPanels
		setLayout(mainpanels);
		preBoot = new preBoot();
		bootScreen = new bootScreen();
		instructionsPanel = new instructionsPanel();
		highScorePanel = new highScorePanel();
		
		add(preBoot, "Pre"); // Adds Main JPanels to the Card Layout, by default will show the PreBoot Animation
		add(bootScreen, "Boot");
		add(gamePanel, "Game");
		add(instructionsPanel, "Instructions"); 
		add(highScorePanel, "HighScore");
	}
	public static void record(String sname) { // Allows switiching between panels through this static method
		mainpanels.show(BioBot.mainprogram, sname);
	}
}
