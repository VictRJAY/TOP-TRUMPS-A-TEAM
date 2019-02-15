package commandline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameCalc extends Deck {

	// Consider making player/card objects to more easily pass stuff around

	ArrayList<Integer> shuffledDeck = new ArrayList<Integer>();
	ArrayList<Integer> drawPile = new ArrayList<Integer>(); // where we store drawn cards
	ArrayList<Integer> roundCards = new ArrayList<Integer>(); // cards played in a given round
	ArrayList<Integer> playerDecks[]; // players decks are stored here syso('you have: ' + playerDecks[0] + ' cards
										// left';
	ArrayList<Integer> activePlayerPositions = new ArrayList<Integer>(); // here we store the positions of the players
	// left in the game

	int numberOfPlayers; // needed for calculations
	int roundCounter = 1; // next two variables are needed for specs
	int drawCounter;
	int currentPlayerPosition;// determines who's turn it is to select an attribute
	String winner = "";

	boolean playerWins = false; // while (playerWins = false) run game CONSIDER REMOVING THIS AND JUST USING
	boolean userEliminated = false; // needed so scanner doesnt run if user isn't in game
	boolean playerEliminatedThisRound = false;
	boolean draw = false; // needed to not run AssignCard methods in OneRound() (currently line 38)
	boolean testLog = false;
	boolean startOfGame = true;

	int[][] playerRoundWins = new int[5][2]; // Here we store original player ID's and how many rounds they've won

	Scanner s = new Scanner(System.in);

	public void StartOfGame() { // this runs once at the start of every game
		getNumberOfPlayers();
		FileReader();
		ShuffleDeck();
		DistributePlayerDecks(numberOfPlayers, shuffledDeck);
		randomizeStartingPosition(); // add who goes first as a syso
		PlayerID(numberOfPlayers);

		if (testLog) {
			FileWriter("This is the original loaded deck:\n\n ");
			Writing2D(classDeckArray);
			FileWriter("This is the shuffled deck:\n\n ");
			WriteCardInformation(shuffledDeck);
			FileWriter("These are the player decks:\n\n ");
			WriteCardIDs(playerDecks);
		}

	}

	public void OneRound() { // this runs every round until all but one players are eliminated
		beginningOfRound();

		int choice = ChooseAttribute(currentPlayerPosition);
		takeTopCards();

		if (testLog) {
			FileWriter("These are the cards played this round:\n\n ");
			WriteCardInformation(roundCards);
			WriteCorrespondingAttributes(choice, roundCards);
		}

		int winner = CompareRoundCards(choice);

		// wouldYouLikeToContinue();
		// This method breaks up every round so the game is
		// actually usable
		endOfRound(winner);

	}

	public void beginningOfRound() {
		draw = false;
		System.out.println("\nThe current round is: " + roundCounter);

		if (!userEliminated && activePlayerPositions.get(currentPlayerPosition) == 0) { // displays who's turn it is
			System.out.println("\nIt is the User's turn to select an attribute.");
		} else {
			System.out.println("\nIt is AI-Player" + activePlayerPositions.get(currentPlayerPosition)
					+ "'s turn to select an attribute.");
		}

		if (!userEliminated) { // displays amount of cards and which card the user has
			System.out.println("\nYou have: " + playerDecks[0].size() + " cards left.");
			System.out.println("\nYour card is: \n" + ShowCardInformation(classDeckArray, playerDecks[0].get(0)));

		}
		if (testLog) {
			FileWriter("ROUND: " + roundCounter + "\r\n");
		}
	}

	public void endOfRound(int winnerOfRound) { // do we need this?
		if (draw == false) {
			AwardAllCards(winnerOfRound);

		} else {
			System.out.println("THIS ROUND WAS A DRAW: " + roundCounter); // testing
			System.out.println("There are now: " + drawPile.size() + " cards in the common pile");
			if (testLog) {
				FileWriter("There are now: " + drawPile.size() + " cards in the common pile");
				FileWriter("This is the drawPile:\n\n ");
				WriteCardInformation(drawPile);
			}

		}
		if (testLog) {

			FileWriter("These are the contents of players' decks after the round:\n\n ");
			WriteCardIDs(playerDecks);
		}

		checkLoser();
		while (playerEliminatedThisRound) { // this only runs if there are more than one player eliminated in one round
			checkLoser();
		}
		roundCounter++; // the method increments the roundCounter at the very end of the round
	}

	public void ShuffleDeck() { // shuffles deck positions

		ArrayList<Integer> shuffledCardPositions = new ArrayList<Integer>();

		for (int i = 0; i < (classDeckArray.length - 1); i++) { // Size of the deck - headers
			shuffledCardPositions.add(i + 1);
		}
		Collections.shuffle(shuffledCardPositions);

		shuffledDeck = shuffledCardPositions;
	}

	public String ShowCardInformation(String[][] cards, int cardID) { // takes the ID of a card, compares it to the deck
																		// and returns all attributes

		String information = "";

		for (int j = 1; j < cards.length; j++) {
			if (cardID == Integer.valueOf(cards[j][0])) {
				information = cards[0][1] + ": " + cards[j][1] + "\n(1)" + cards[0][2] + ": " + cards[j][2] + "\n(2)"
						+ cards[0][3] + ": " + cards[j][3] + "\n(3)" + cards[0][4] + ": " + cards[j][4] + "\n(4)"
						+ cards[0][5] + ": " + cards[j][5] + "\n(5) " + cards[0][6] + ": " + cards[j][6] + "\n";
			}
		}

		return information;
	}

	public ArrayList<Integer>[] DistributePlayerDecks(int numOfPlayers, ArrayList<Integer> shuffledDeck) {

		// distributes deck between 2-5 players

		playerDecks = new ArrayList[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++) {
			playerDecks[i] = new ArrayList<Integer>();
		}
		int j = 0;
		for (int i = 0; i < shuffledDeck.size(); i++) {

			if (j == numOfPlayers) {
				j = 0;
			}
			playerDecks[j].add(shuffledDeck.get(i));
			j++;

		}
		return playerDecks;
	}

	public void takeTopCards() { // this method is called at the start of every round

		// the method takes all the top cards and stores them in an ArrayList called
		// roundCards
		ArrayList<Integer> tempRoundCards = new ArrayList<Integer>();
		for (int i = 0; i < numberOfPlayers; i++) {
			tempRoundCards.add(playerDecks[i].get(0));
			playerDecks[i].remove(0);
		}

		roundCards = tempRoundCards;

	}

	public int CompareRoundCards(int attributeNumber) {
		// this method is passed an attributeNumber and then checks that attribute in an
		// array of cards

		ArrayList<Integer> temp = new ArrayList<Integer>();
		// this loop adds the attribute values to the temp arraylist
		for (int i = 0; i < roundCards.size(); i++) {
			int tempCardNo = roundCards.get(i);
			for (int j = 1; j < classDeckArray.length; j++) {
				if (tempCardNo == Integer.valueOf(classDeckArray[j][0])) {
					temp.add(Integer.valueOf(classDeckArray[j][attributeNumber + 1]));
				}
			}
		}
		int maxNumber = 0;
		int winningCardNumberPosition = 0;
		for (int i = 0; i < temp.size(); i++) { // this loop determines which position in temp is the highest(winner)
			if (maxNumber < temp.get(i)) {
				maxNumber = temp.get(i);
				winningCardNumberPosition = i; // position of winning card in order
			}
		}
		int count = 0;
		for (int i = 0; i < temp.size(); i++) { // if you find a maxNumber twice, count is incremented to 2 and
												// indicates a drawn round
			if (maxNumber == temp.get(i)) {
				count++;
			}
		}
		if (count > 1) {
			winningCardNumberPosition = 1337; // This tells the program that there has been a draw
			drawPile.addAll(roundCards); // adds cards to drawPile
			drawCounter++; // increments drawCounter for database
			draw = true;
		}

		return winningCardNumberPosition;
	}

	public void AwardAllCards(int winningNumber) { // checks for a draw and assigns cards potential winner
		// winningNumber is the position in activePlayerPositions/roundCards of the
		// winning player/card

		if (!userEliminated && winningNumber == 0) {
			System.out.println("\nThe winner of the round was the user.");

		} else {
			System.out.println("\nThe winner of the round was AI-player" + activePlayerPositions.get(winningNumber));
		}

		System.out.println("The winning card was: \n");
		System.out.println(ShowCardInformation(classDeckArray, roundCards.get(winningNumber)));

		if (!drawPile.isEmpty()) { // checks drawpile for cards

			roundCards.addAll(drawPile);
			drawPile.clear();
			if (testLog) {
				FileWriter("There are now: " + drawPile.size() + " cards in the common pile");

			}

		}

		playerDecks[winningNumber].addAll(roundCards);
		roundCards.clear();
		// winning number depends on a set list of players (user + AI1-4)
		AssignRoundWin(winningNumber);
		currentPlayerPosition = winningNumber;

	}

	public void AssignRoundWin(int playerNumber) {
		for (int i = 0; i < playerRoundWins.length; i++) {
			if (playerRoundWins[i][0] == activePlayerPositions.get(playerNumber)) {
				playerRoundWins[i][1]++;
			}

		}
	}

	public void checkLoser() { // to be run after every round

		if (playerDecks.length == 1) {
			playerWins = true;
			determineWinner(activePlayerPositions.get(0));

			if (testLog) {
				WriteWinnerToLog();
			}

		}

		for (int i = 0; i < playerDecks.length; i++) {
			if (playerDecks[i].isEmpty()) {
				if (i == 0) {
					userEliminated = true;
				}
				playerEliminatedThisRound = true;
				playerEliminated(i);

				if (currentPlayerPosition - i > 0) { // This checks whether the eliminated player's position was before
														// the current player, and if so adjusts the position
														// accordingly
					currentPlayerPosition -= 1;
				}
				break;
			}
			playerEliminatedThisRound = false;
			// if a player is eliminated, the checkLoser method is run again to check if
			// there is more than one player eliminated. if this loop completes with no
			// empty decks, the boolean is set to false and ends the
			// while(!playerEliminatedThisRound) loop
		}

	}

	public void newPlayerDecks(int playerNumber) {

		int j = 0;
		ArrayList<Integer>[] temp = new ArrayList[playerDecks.length - 1];
		for (int i = 0; i < playerDecks.length; i++) {
			if (!(i == playerNumber)) {
				temp[i - j] = playerDecks[i];
			} else {
				j++;
			}

		}
		playerDecks = temp;
		// setPlayerDecks(temp);
		numberOfPlayers = numberOfPlayers - 1;

		// return temp;
	}

	public void playerEliminated(int playerNumber) { // complete this when you know what you are doing

		newPlayerDecks(playerNumber);
		if (userEliminated && playerNumber == 0) {
			System.out.println("The user was eliminated.");
		} else {
			System.out.println("AI-Player" + activePlayerPositions.get(playerNumber) + " WAS ELIMINATED. "); // playerlist
		}

		activePlayerPositions.remove(playerNumber);

	}

	public int randomizeStartingPosition() { // self explanatory
		int firstRound;
		firstRound = (int) (Math.random() * numberOfPlayers);
		currentPlayerPosition = firstRound;
		return currentPlayerPosition;

	}

	public ArrayList<String> ListOfPlayers(int numberOfPlayers) { // makes a list of players and sets the inputted
		// number of AI

		ArrayList<String> playerList = new ArrayList<String>();
		playerList.add("Player");
		for (int i = 1; i < numberOfPlayers; i++) {
			String AIPlayer = "AIPlayer" + (i);
			playerList.add(AIPlayer);
		}
		return playerList;
	}

	public ArrayList<Integer> PlayerID(int numberOfPlayers) { // makes player ID's,
// number of AI

		for (int i = 0; i < numberOfPlayers; i++) {
			activePlayerPositions.add(i); // maybe + 1 so it's 1-5 instead of 0-4?
			playerRoundWins[i][0] = i;
		}

		return activePlayerPositions;
	}

	public int ChooseAttribute(int currentPlayerPosition) { // determines whether an AI player or a user
															// is playing

		int choice = -1;
		if (currentPlayerPosition == 0 && !userEliminated) {
			// choice = (int) (Math.random() * 5 + 1);
			choice = PlayerChooseAttribute();
			System.out.println("\nUser selected attribute: " + classDeckArray[0][choice + 1]);
			if (testLog) {
				FileWriter("\nUser selected attribute: " + classDeckArray[0][choice + 1]);
			}
		} else {
			choice = (int) (Math.random() * 5 + 1);
			System.out.println("\nAI-Player" + activePlayerPositions.get(currentPlayerPosition)
					+ " selected attribute: " + classDeckArray[0][choice + 1]);
			if (testLog) {
				FileWriter("\nAI-Player" + activePlayerPositions.get(currentPlayerPosition) + " selected attribute: "
						+ classDeckArray[0][choice + 1]);
			}

		}
		return choice;
	}

	public void getNumberOfPlayers() { // asks for numbers of AI players and then sets numberOfPlayers variable

		int number;
		while (true) {
			System.out.println("Enter numbers of AI Players(1-4):");
			number = s.nextInt();
			s.nextLine();
			if (number >= 1 && number <= 4) {
				numberOfPlayers = number + 1;
				break;
			}
		}
	}

	public void wouldYouLikeToContinue() {
		System.out.println("Enter '1' to see the winner of the round");
		String goon = s.nextLine();
		while (!goon.equals("1")) {
			System.out.println("Please enter 1 to continue");
			goon = s.nextLine();

		}
	}

	public int PlayerChooseAttribute() { // chooses an attribute :)

		int number;
//		String showCardInfo = ShowCardInformation(classDeckArray, playerDecks[0].get(0)); // shows top card of users
//																							// deck
//		System.out.println(showCardInfo);

		while (true) {
			System.out.println("Choose an attribute(1-5):");
			number = s.nextInt();
			s.nextLine();
			if (number >= 1 && number <= 5) {
				break;
			}
		}
		return number;
	}

	public void FileWriter(String content) {

		File f = new File("TestLog.txt");
		String divider = "-------------------------------\r\n";
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (startOfGame) { // this boolean overwrites an existing TestLog.txt if it is the start of the
								// game
				FileWriter fw = new FileWriter("TestLog.txt", false);
				startOfGame = false;
			}
			FileWriter fw = new FileWriter("TestLog.txt", true); // right now it always appends if the file exists
			fw.write(content + "\r\n");
			fw.write(divider);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method takes an ArrayList<Integer> filled with card ID's and adds the
	 * card information to a readable string ArrayLists to be run in this method for
	 * 
	 * testlog: contents of shuffledDeck, contents of drawPile, contents of
	 * roundCards
	 */
	public void WriteCardInformation(ArrayList<Integer> al) {
		String temp = "";
		for (int i = 0; i < al.size(); i++) {
			temp += ShowCardInformation(classDeckArray, al.get(i));
			temp += "\r\n";
		}
		FileWriter(temp);
	}

	/*
	 * This method takes an ArrayList<Integer>[] playerDeck arrays, which are in
	 * turned filled with card ID's the method then adds these card ID's to a
	 * readable string and writes it to the testlog.
	 * 
	 * Things to be run by this method: playerDecks after distribution playerDecks
	 * after cards are added/removed
	 */

	public void WriteCardIDs(ArrayList<Integer>[] al) { // INstead of a method that does this, write something that
														// displays card name
		String temp = "";
		for (int i = 0; i < al.length; i++) {
			if (activePlayerPositions.get(i) == 0) { // distinguishes between the user and AI players for the
														// FileWriter
				temp += "\r\nUser:\r\n";
			} else {

				temp += "\r\nAIPlayer:" + activePlayerPositions.get(i) + "\r\n";

			}
			for (int j = 0; j < al[i].size(); j++) {
				temp += "CardID: " + al[i].get(j);
				temp += "\r\n";

			}
		}
		FileWriter(temp);
	}

	// This method takes a String[][] and reformats it to a string that can be
	// written to the testlog

	public void Writing2D(String[][] string2d) { // to print the OG deck to testlog
		String temp = "";
		for (int i = 0; i < string2d.length; i++) {
			for (int j = 0; j < string2d[i].length; j++) {
				temp += string2d[i][j];
				temp += "\t";
			}
			temp += "\r\n";
		}
		FileWriter(temp);
	}

	public void WriteAllRoundCards(ArrayList<Integer> roundCards) {

		String temp = "";
		for (int i = 0; i < roundCards.size(); i++) {
			temp += ShowCardInformation(classDeckArray, roundCards.get(i));
			temp += "\r\n";
		}
		FileWriter(temp);
	}

	public void WriteWinnerToLog() {
		String temp;
		if (!userEliminated) {
			temp = ("The winner of the game was the user.");
		} else {
			temp = ("The winner of the game was AIplayer" + activePlayerPositions.get(0));

		}
		// playerlist thing
		FileWriter(temp);
	}

	public void WriteCorrespondingAttributes(int choice, ArrayList<Integer> roundCards) {
		String temp = "";
		temp += "The chosen attribute is: " + classDeckArray[0][choice + 1] + "(" + choice + ")\r\n";
		for (int i = 0; i < roundCards.size(); i++) {
			temp += "Card: " + (i + 1) + " value is: " + classDeckArray[roundCards.get(i)][choice + 1];
			temp += "\r\n";
		}
		FileWriter(temp);

	}

	public void determineWinner(int playerNumber) {
		if (!userEliminated && playerNumber == 0) {
			winner = "User";
		} else {
			winner = "AIPlayer" + playerNumber;
		}
	}

}
