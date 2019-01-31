package commandline;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameCalc extends Deck {

	ArrayList<Integer> shuffledDeck = new ArrayList<Integer>();
	ArrayList<Integer> drawPile = new ArrayList<Integer>(); // where we store drawn cards
	ArrayList<Integer> roundCards = new ArrayList<Integer>(); // cards played in a given round

	ArrayList<Integer> playerDecks[]; // players decks are stored here syso('you have: ' + playerDecks[0] + ' cards
										// left';

	int numberOfPlayers; // needed for calculations
	int roundCounter; // next two variables are needed for specs
	int drawCounter;
	int currentPlayerPosition;// needed for calc

	boolean playerWins = false; // while (playerWins = false) run game
	boolean userEliminated = false; // needed so scanner doesnt run if user isn't in game
	boolean draw = false; // needed to not run AssignCard methods in OneRound() (currently line 38)

	int[][] playerRoundWins = new int[5][2]; // Here we store original player ID's and how many rounds they've won
	ArrayList<Integer> currentPlayerPositions = new ArrayList<Integer>(); // here we store the positions of the players
																			// left in the game

	// ArrayList<Integer> currentPlayerPositions = new ArrayList<>();

	Scanner s = new Scanner(System.in);

	public void StartOfGame() { // this runs once at the start of every game
		getNumberOfPlayers();
		FileReader();
		ShuffleDeck();
		DistributePlayerDecks(numberOfPlayers, shuffledDeck);
		randomizeStartingPosition();
		PlayerID(numberOfPlayers);
	}

	public void OneRound() { // this runs every round until all but one players are eliminated
		draw = false;

		int choice = ChooseAttributeForAIPlayerRound(currentPlayerPosition);
		takeTopCards();
		int winner = Compare(choice, roundCards, classDeckArray);
		// prompt user to see winner of round
		
		wouldYouLikeToContinue(); // This method breaks up every round so the game is actually usable

		if (draw == false) {
			AwardAllCards(winner, roundCards);
			checkLoser(playerDecks);
		} else {
			System.out.println("THIS ROUND WAS A DRAW"); // testing
		}

		System.out.println("ROUND: " + roundCounter); // everything here is just testing

		for (int i = 0; i < playerRoundWins.length; i++) {
			System.out.println("Player: " + playerRoundWins[i][0]);
			System.out.println("Wins: " + playerRoundWins[i][1]);
		}

	}

	public void getNumberOfPlayers() { // asks for numbers of AI players and then sets numberOfPlayers variable

		int number;
		while (true) {
			System.out.println("Enter numbers of AI Players:");
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

	public int ChooseAttribute() { // chooses an attribute :)

		int number;
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
			currentPlayerPositions.add(i); // maybe + 1 so it's 1-5 instead of 0-4?
			playerRoundWins[i][0] = i;
		}

		return currentPlayerPositions;
	}

	public void ShuffleDeck() { // shuffles deck positions

		// you are using an arraylist here despite set positions because it's easier to
		// shuffle
		ArrayList<Integer> cardPosition = new ArrayList<Integer>();

		for (int i = 0; i < 40; i++) {
			cardPosition.add(i + 1);
		}
		Collections.shuffle(cardPosition);

		shuffledDeck = cardPosition;
	}

	public String ShowCardInformation(String[][] cards, int cardID) { // takes the ID of a card, compares it to the deck
																		// and returns all attributes

		String information = "";

		for (int j = 1; j < cards.length; j++) {
			if (cardID == Integer.valueOf(cards[j][0])) {
				information = "Card Name: \n" + cards[j][1] + "\nAttribute 1: " + cards[j][2] + "\nAttribute 2: "
						+ cards[j][3] + "\nAttribute 3: " + cards[j][4] + "\nAttribute 4: " + cards[j][5]
						+ "\nAttribute 5: " + cards[j][6];
			}
		}

		return information;
	}

	public ArrayList<Integer>[] DistributePlayerDecks(int numOfPlayers, ArrayList<Integer> shuffledDeck) {

		// distributes deck between x (x<6) players

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

	public ArrayList<Integer> takeTopCards() { // this method is meant to be called at the start of every round

		// the method takes all the top cards and stores them in an ArrayList called
		// roundCards
		ArrayList<Integer> tempRoundCards = new ArrayList<Integer>();
		for (int i = 0; i < numberOfPlayers; i++) {
			tempRoundCards.add(playerDecks[i].get(0));
			playerDecks[i].remove(0);
		}
		roundCounter++; // the method also increments the roundCounter since it's called at the
						// beginning of a round
		roundCards = tempRoundCards;
		return roundCards;
	}

	public int Compare(int attributeNumber, ArrayList<Integer> roundCards, String[][] card) { // this method is passed
																								// an
																								// attributeNumber and
																								// then
																								// checks that attribute
																								// in
																								// an array of cards

		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < roundCards.size(); i++) {
			int tempCardNo = roundCards.get(i);
			for (int j = 1; j < card.length; j++) {
				if (tempCardNo == Integer.valueOf(card[j][0])) {
					temp.add(Integer.valueOf(card[j][attributeNumber + 1]));
				}
			}
		}
		int maxNumber = 0;
		int winningCardNumber = 0;
		for (int i = 0; i < temp.size(); i++) {
			if (maxNumber < temp.get(i)) {
				maxNumber = temp.get(i);
				winningCardNumber = i; // position of winning card in order
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
			winningCardNumber = 1337; // this is the number you've assigned to a drawn round, if this method returns
			IfDraw(roundCards); // adds cards to drawPile
			draw = true;
		}
//		return maxNumber;
		return winningCardNumber;
	}

	public void AwardAllCards(int winningNumber, ArrayList<Integer> round) { // checks for a draw and assigns cards to
																				// winner
		if (drawPile.size() == 0) {
			// does nothing
		} else {
			drawPile.addAll(round);
			drawPile.clear();
		}

		playerDecks[winningNumber].addAll(round);
		roundCards.clear();
		// winning number depends on a set list of players (user + AIx sorted)
		AssignRoundWin(winningNumber);
		currentPlayerPosition = winningNumber;

	}

	public void IfDraw(ArrayList<Integer> round) {
		drawPile.addAll(round);
		drawCounter++;
		checkLoser(playerDecks);

		// AFTER A DRAW THE SAME PLAYER SELECTS AN ATTRIBUTE AGAIN, CODE THIS HERE
	}

	public void AssignRoundWin(int playerNumber) {
		for (int i = 0; i < playerRoundWins.length; i++) {
			if (playerRoundWins[i][0] == currentPlayerPositions.get(playerNumber)) {
				playerRoundWins[i][1]++;
			}

		}

		// made redundant by the code in AwardAllCards()
	}

	public void playerEliminated(int playerNumber) { // complete this when you know what you are doing
		numberOfPlayers = numberOfPlayers - 1;
		currentPlayerPositions.remove(playerNumber);
//		playerDecks[playerNumber].clear(); // this is just to test, remove this for game version
		newPlayerDecks(playerDecks);
		System.out.println("TESTING: PLAYER WAS ELIMINATED: " + playerNumber);
		// delete playerDecks[playerNumber] from playerDecks[]
		// delete player from playerList since we dont need them if they lose
	}

	public ArrayList<Integer>[] newPlayerDecks(ArrayList<Integer>[] playerDecks) {
		ArrayList<Integer>[] temp = new ArrayList[playerDecks.length - 1];
		int j = 0;
		for (int i = 0; i < playerDecks.length; i++) {
			if (!playerDecks[i].isEmpty()) {
				temp[i - j] = playerDecks[i];
			} else {
				j++; // this is only good if 2 players are eliminated in the same round
			}

		}
		setPlayerDecks(temp);
		return temp;
	}

	public void setPlayerDecks(ArrayList<Integer>[] newDecks) { // called in newPlayerDecks to set the new decks when a
																// player is eliminated
		playerDecks = newDecks;
	}

	public void checkLoser(ArrayList<Integer>[] playerDecks) { // to be run after every round
		for (int i = 0; i < playerDecks.length; i++) {
			if (playerDecks[i].isEmpty()) {
				playerEliminated(i);
				if (i == 0) {
					userEliminated = true;
				}
			}
			if (playerDecks.length == 1) {
				playerWins = true;
				// playerDecks[0] (compare current player positons) compare to
				// playerRoundWins(positions), the corresponding playerID is the winner
			}
		}
	}

	public int randomizeStartingPosition() { // self explanatory
		int firstRound;
		firstRound = (int) (Math.random() * numberOfPlayers);
		currentPlayerPosition = firstRound;
		return currentPlayerPosition;

	}

	public int ChooseAttributeForAIPlayerRound(int currentPlayerPosition) { // determines whether an AI player or a user
																			// is playing

		int choice = -1;
		if (currentPlayerPosition == 0 && !userEliminated) {
			ShowCardInformation(classDeckArray, playerDecks[0].get(0)); // shows top card of users deck
			choice = ChooseAttribute();
		} else {
			choice = 1;
		}
		return choice;
	}

}
