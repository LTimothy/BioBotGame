/* Timothy Lee
 * 5/22/14
 * BioBot.java
 * This is the final project game.
 *
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
 * Image Sources:
 * http://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/NASA_Mars_Rover.jpg/300px-NASA_Mars_Rover.jpg
 * http://aboutfacts.net/SpacePlanets16.htm
 * http://photojournal.jpl.nasa.gov/jpeg/PIA12840.jpg
 * http://img713.imageshack.us/img713/3569/lake20found20on20mars.jpg
 * http://www.fundamentallyreformed.com/wp-content/uploads/2010/09/rockcliparttt3-100x100.gif
 * http://clipart.coolclips.com/150/wjm/tf05107/CoolClips_symb0094.jpg
 * http://openclipart.org/image/2400px/svg_to_png/22734/papapishu_Lab_icon_1.png
 * http://www.jeffjonesillustration.com/images/illustration/00745-tornado-twister-icon.jpg
 */

import java.awt.BorderLayout; // for BorderLayout
import javax.swing.*;		// for class JFrame

public class BioBot {
	public static JFrame frame; // Main Frame
	public static JPanel mainprogram = new loadScreen(); // Allows reference to the main program
	public static void main (String[] args) { // Initialize and Run BioBot
		BioBot ote = new BioBot();
		ote.Run();
	}
	public void Run() { // Runs the program
		frame = new JFrame("BioBot"); // Gives the JFrame a name of "BioBot"
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allows closing upon clicking the [X]
		BorderLayout defaultlayout = new BorderLayout(); // BorderLayout
		frame.setLayout(defaultlayout);
		
		mainprogram.setVisible(true); // Allows the user to see the game
		frame.add(mainprogram, BorderLayout.CENTER); // Adds the grid panel to the frame
		
  		frame.setResizable(false); // Disables resizing of the frame
  		frame.setSize(806, 845); // Sets the default size of the game
		frame.setVisible(true); // Allows the JPanel to be seen
	}
}
