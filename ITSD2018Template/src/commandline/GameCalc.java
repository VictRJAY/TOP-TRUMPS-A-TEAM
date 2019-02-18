package commandline;

import java.util.ArrayList;
import java.util.Collections;

public class GameCalc {

	/**
	 * This class deals with all the calculations done for a CLI TopTrumps game.
	 * This class calls the GameSettings for input, and FileWriting when the user
	 * specifies that they want to write the game log to a file.
	 * 
	 */

	ArrayList<Integer> shuffledDeck = new ArrayList<Integer>();
	ArrayList<Integer> drawPile = new ArrayList<Integer>(); // where we store drawn cards
	ArrayList<Integer> roundCards = new ArrayList<Integer>(); // cards played in a given round
	ArrayList<Integer> playerDecks[]; // players decks are stored here syso('you have: ' + playerDecks[0] + ' cards
										// left';
	ArrayList<Integer> activePlayerPositions = new ArrayList<Integer>(); // here we store the positions of the players
	// left in the game
	int currentPlayerPosition;// determines who's turn it is to select an attribute
	int numberOfPlayers; // needed for calculations

	int roundCounter = 1; // Persistent data variables
	int drawCounter = 0;
	String winner = "";
	int[] playerRoundWins = new int[5];

	boolean playerWins = false;
	boolean userEliminated = false;
	boolean playerEliminatedThisRound = false;
	boolean draw = false;
	static boolean testLog = false;

	protected String[][] classDeckArray = new String[41][7];

	GameSettings g = new GameSettings();
	FileWriting f = new FileWriting();

	public void StartOfGame() { // this runs once at the start of every game

		numberOfPlayers = (g.getNumberOfAIPlayers() + 1);

		Deck.FileReader();
		setClassDeckArray(Deck.classDeckArray);

		ShuffleDeck();
		DistributePlayerDecks(numberOfPlayers);
		randomizeStartingPosition(); // add who goes first as a syso
		setPlayerID(numberOfPlayers);

		if (testLog) {
			f.setClassDeckArray(classDeckArray);
			f.setActivePlayerPositions(activePlayerPositions);
			f.FileWriter("\nThis is the original loaded deck:\n\n ");
			f.Writing2D(classDeckArray);
			f.FileWriter("\nThis is the shuffled deck:\n\n ");
			f.WriteCardInformation(shuffledDeck);
			f.FileWriter("\nThese are the player decks:\n\n ");
			f.WriteCardIDs(playerDecks);
		}

	}

	public void OneRound() { // this runs every round until all but one players are eliminated
		beginningOfRound();
		int choice = ChooseAttribute(currentPlayerPosition);
		takeTopCards();

		if (testLog) {
			f.FileWriter("\nThese are the cards played this round:\n\n ");
			f.WriteCardInformation(roundCards);
			f.WriteCorrespondingAttributes(choice, roundCards);
		}

		int winner = CompareRoundCards(choice);

		if (!userEliminated) {
			g.wouldYouLikeToContinue();
		}

		endOfRound(winner);

	}

	public void beginningOfRound() { // All of the calculations that need to happen at the start of a round
		draw = false;
		System.out.println("\nThe current round is: " + roundCounter);

		if (activePlayerPositions.get(currentPlayerPosition) == 0) { // displays who's turn it is
			System.out.println("\nIt is the User's turn to select an attribute.");
		} else {
			System.out.println("\nIt is AIplayer" + activePlayerPositions.get(currentPlayerPosition)
					+ "'s turn to select an attribute.");
		}

		if (!userEliminated) { // displays amount of cards and which card the user has
			System.out.println("\nYou have: " + playerDecks[0].size() + " cards left.");
			System.out.println("\nYour card is: \n" + ShowCardInformation(classDeckArray, playerDecks[0].get(0)));

		}
		if (testLog) {
			f.FileWriter("ROUND: " + roundCounter + "\r\n");
		}
	}

	public void endOfRound(int winnerOfRound) { // All of the calculations that need to happen at the end of a round
		if (draw == false) {
			AwardAllCards(winnerOfRound);

		} else {
			System.out.println("THIS ROUND WAS A DRAW: " + roundCounter); // testing
			System.out.println("There are now: " + drawPile.size() + " cards in the common pile");
			if (testLog) {
				f.FileWriter("\nThere are now: " + drawPile.size() + " cards in the common pile");
				f.FileWriter("\nThis is the drawPile:\n\n ");
				f.WriteCardInformation(drawPile);
			}
		}
		if (testLog) {

			f.FileWriter("\nThese are the contents of players' decks after the round:\n\n ");
			f.WriteCardIDs(playerDecks);
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

	public ArrayList<Integer>[] DistributePlayerDecks(int numOfPlayers) {

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

		if (!drawPile.isEmpty()) { // checks drawpile for cards

			roundCards.addAll(drawPile);
			drawPile.clear();
			if (testLog) {
				f.FileWriter("\nThere are now " + drawPile.size() + " cards in the common pile.\n\n");

			}

		}

		if (!userEliminated && winningNumber == 0) {
			System.out.println("\nThe winner of the round was the user.");
			System.out.println("\nYou have been awarded " + roundCards.size() + " cards after winning the round.");

		} else {
			System.out.println("\nThe winner of the round was AIplayer" + activePlayerPositions.get(winningNumber));
		}

		System.out.println("The winning card was: \n");
		System.out.println(ShowCardInformation(classDeckArray, roundCards.get(winningNumber)));

		playerDecks[winningNumber].addAll(roundCards);
		roundCards.clear();
		// winning number depends on a set list of players (user + AI1-4)
		AssignRoundWin(winningNumber);
		currentPlayerPosition = winningNumber;

	}

	public void AssignRoundWin(int playerNumber) {
		playerRoundWins[playerNumber]++;

	}

	public void checkLoser() { // to be run after every round

		if (playerDecks.length == 1) {
			playerWins = true;
			determineWinner(activePlayerPositions.get(0));

			if (testLog) {
				f.WriteWinnerToLog(userEliminated);
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

	public void newPlayerDecks(int playerNumber) { // remakes the player decks after a player is eliminated

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
		numberOfPlayers = numberOfPlayers - 1;
	}

	public void playerEliminated(int playerNumber) { // complete this when you know what you are doing

		newPlayerDecks(playerNumber);
		if (activePlayerPositions.get(playerNumber) == 0) {
			System.out.println("THE USER WAS ELIMINATED.");
		} else {
			System.out.println("AIplayer" + activePlayerPositions.get(playerNumber) + " WAS ELIMINATED. "); // playerlist
		}

		activePlayerPositions.remove(playerNumber);
		if (testLog) {
			f.setActivePlayerPositions(activePlayerPositions); // updates testlog with updates player positions

		}

	}

	public int randomizeStartingPosition() { // self explanatory
		int firstRound;
		firstRound = (int) (Math.random() * numberOfPlayers);
		currentPlayerPosition = firstRound;
		return currentPlayerPosition;

	}

	public int ChooseAttribute(int currentPlayerPosition) { // determines whether an AI player or a user
		// is playing

		int choice = -1;
		if (currentPlayerPosition == 0 && !userEliminated) {

			choice = g.PlayerChooseAttribute();
			System.out.println("\nUser selected attribute: " + classDeckArray[0][choice + 1]);
			if (testLog) {
				f.FileWriter("\nUser selected attribute: " + classDeckArray[0][choice + 1]);
			}
		} else {
			choice = (int) (Math.random() * 5 + 1);
			System.out.println("\nAIplayer" + activePlayerPositions.get(currentPlayerPosition) + " selected attribute: "
					+ classDeckArray[0][choice + 1] + "\n");
			if (testLog) {
				f.FileWriter("\nAIplayer" + activePlayerPositions.get(currentPlayerPosition) + " selected attribute: "
						+ classDeckArray[0][choice + 1] + "\n");
			}

		}
		return choice;
	}

	public ArrayList<Integer> setPlayerID(int numberOfPlayers) { // makes player ID's,
// number of AI

		for (int i = 0; i < numberOfPlayers; i++) {
			activePlayerPositions.add(i); // maybe + 1 so it's 1-5 instead of 0-4?
		}

		return activePlayerPositions;
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

	public void determineWinner(int playerNumber) {
		System.out.println("GAME IS OVER");

		if (!userEliminated) {
			winner = "User";
			System.out.println("The winner of the game was the user.");
		} else {
			winner = "AIPlayer" + playerNumber;
			System.out.println("The winner of the game was AIplayer: " + activePlayerPositions.get(0));
		}
	}

	public void setClassDeckArray(String[][] classDeckArray) {
		this.classDeckArray = classDeckArray;
	}

}
