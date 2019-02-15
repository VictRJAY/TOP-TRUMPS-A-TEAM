package commandline;

import java.sql.SQLException;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	/**
	 * This main method is called by TopTrumps.java when the user specifies that
	 * they want to run in command line mode. The contents of args[0] is whether we
	 * should write game logs to a file.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file? SET THIS TO FALSE
		if (args[0].equalsIgnoreCase("true"))
			writeGameLogsToFile = true; // Command line selection

		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		//
		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {
			GameCalc c = new GameCalc();

			if (writeGameLogsToFile == true) {
				c.testLog = true;
			}

			c.StartOfGame();

			while (!c.playerWins) {
				c.OneRound();
			}

			System.out.println("GAME IS OVER");
			if (c.activePlayerPositions.get(0) == 0) {
				System.out.println("The winner of the game was the user.");
			} else {
				System.out.println("The winner of the game was AIplayer: " + c.activePlayerPositions.get(0));
			}
			
//			try {
//				dbConnect d = new dbConnect();
//				d.dbValuesImport(String.valueOf(c.drawCounter),c.winner,String.valueOf(c.roundCounter));
//			} catch (SQLException e) {
//				//e.printStackTrace();
//			}
			userWantsToQuit = true; // use this when the user wants to exit the game

		}

	}

}
