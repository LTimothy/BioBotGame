//package game;

import java.awt.CardLayout; // For CardLayout
import javax.swing.JPanel; // For JPanel

public class loadScreen extends JPanel {
	public static CardLayout mainpanels; // Allows static reference to the mainpanels
	//public static int fromload = 0; // Default 
	public static JPanel bootScreen, instructionsPanel, preBoot, highScorePanel; // Default JPanels for Card Layout
	//private static String name;
	public static gamePanel gamePanel = new gamePanel(); // Allows static reference to the game
	public loadScreen () {
		mainpanels = new CardLayout(); // Initializes Main JPanels
		setLayout(mainpanels);
		preBoot = new preBoot();
		bootScreen = new bootScreen();
		//gamePanel = new gamePanel();
		instructionsPanel = new instructionsPanel();
		highScorePanel = new highScorePanel();
		
		add(preBoot, "Pre"); // Adds Main JPanels to the Card Layout, by default will show the PreBoot Animation
		add(bootScreen, "Boot");
		add(gamePanel, "Game");
		add(instructionsPanel, "Instructions"); 
		add(highScorePanel, "HighScore");
		//mainpanels.show(this, "Boot");
	}
	public static void record(String sname) { // Allows switiching between panels through this static method
		mainpanels.show(BioBot.mainprogram, sname);
	}
}
