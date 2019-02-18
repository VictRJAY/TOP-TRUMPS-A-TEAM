package commandline;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

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
		if (writeGameLogsToFile == true) {
			GameCalc.testLog = true;
		}

		// GAME MENU HERE

		while (!GameSettings.userWantsToQuit) {
			System.out.println("\nDo you want to see past results or play a game?");
			System.out.println("\t1: Print Game Statistics");
			System.out.println("\t2: Play game");
			System.out.println("Enter the number for your selection: \n");
			
			gameMenu();
			
		}

			
			
		

	}

	public static void gameMenu() {
		GameCalc c = new GameCalc();
		dbConnect d = new dbConnect();
		
			Scanner s = new Scanner(System.in);
			
			int selection = -1;
			try {
				selection = s.nextInt();
			} catch (InputMismatchException e) {
			
			}
			
			if (selection == 1) {

				System.out.println("Print Game Statistics:\n");
				System.out.println("Total games played: \t\t\t\t" + d.getTotalGames());
				System.out.println("Total User victories: \t\t\t\t" + d.getTotalUserVictories());
				System.out.println("Total AI victories: \t\t\t\t" + d.getTotalAIVictories());
				System.out.println("Highest number of rounds played in a game: \t" + d.getHighestRoundNumber());
				System.out.println("Average number of draws: \t\t\t" + d.getAverageDraws());

			} else if (selection == 2) {
				c.StartOfGame();

				while (!c.playerWins) {
					c.OneRound();

				}

				try {

					d.dbInsertGameRow(String.valueOf(c.drawCounter), c.winner, String.valueOf(c.roundCounter), // get the variables from GameCalc, try db connect and get the values ready for db insert query
							c.playerRoundWins);
				} catch (SQLException e) {
					// e.printStackTrace();
				}

			}
			s.nextLine();
		
	}

}
