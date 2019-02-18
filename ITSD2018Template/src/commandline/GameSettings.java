package commandline;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.*;

public class GameSettings {

	Scanner s = new Scanner(System.in);
	static boolean userWantsToQuit = false;

	public void userQuitting(boolean b) {
		if (b) {
			System.out.println("You have quit the game, goodbye!");
			System.exit(0);
		}
	}

	public int getNumberOfAIPlayers() { // asks for numbers of AI players and then sets numberOfPlayers variable

		int number = -1;
		while (true) {
			System.out.println("\nEnter numbers of AI Players(1-4):");

			try {
				number = s.nextInt();
			} catch (InputMismatchException e) {
				// e.printStackTrace();
			}

			if (number >= 1 && number <= 4) {
				break;
			}
			if (number == 9) {
				userWantsToQuit = true;
			}
			userQuitting(userWantsToQuit);
			s.nextLine();
		}
		return number;
	}

	public void wouldYouLikeToContinue() {
		int number = -1;

		while (!(number == 1 || number == 9)) {
			System.out.println("Enter '1' to see the winner of the round, or 9 to quit the game.");
			try {
				number = s.nextInt();
			} catch (InputMismatchException e) {
				// e.printStackTrace();
			}

			if (number == 1) {
				break;
			}
			if (number == 9) {
				userWantsToQuit = true;
				break;
			}
			s.nextLine();
		}

		userQuitting(userWantsToQuit);
		s.nextLine();
	}

	public int PlayerChooseAttribute() { // chooses an attribute :)

		int number = -1;

		while (true) {
			System.out.println("Choose an attribute(1-5):");

			try {
				number = s.nextInt();
			} catch (InputMismatchException e) {
				// e.printStackTrace();
			}

			if (number >= 1 && number <= 5) {
				break;
			}
			if (number == 9) {
				userWantsToQuit = true;
			}
			userQuitting(userWantsToQuit);
			s.nextLine();
		}
		return number;
	}

}
