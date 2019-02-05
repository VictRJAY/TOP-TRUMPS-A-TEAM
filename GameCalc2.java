package topTrumps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	boolean playerEliminated = false;
	boolean draw = false; // needed to not run AssignCard methods in OneRound() (currently line 38)

	int[][] playerRoundWins = new int[5][2]; // Here we store original player ID's and how many rounds they've won
	ArrayList<Integer> currentPlayerPositions = new ArrayList<Integer>(); // here we store the positions of the players
																			// left in the game
	// testing t = new testing();
	int WiningCard;
	
	

	Scanner s = new Scanner(System.in);

	public void StartOfGame() { // this runs once at the start of every game
		
		getNumberOfPlayers();
		
		
		
		ListOfPlayers(numberOfPlayers);
		
		FileReader();
		
		
		
		ShuffleDeck();
	
		DistributePlayerDecks(numberOfPlayers, shuffledDeck);
		
		randomizeStartingPosition();
		
		//PlayerID(numberOfPlayers);
		

		//FileWriter("This is the original loaded deck:\n\n ");
		//Writing2D(classDeckArray);
		//FileWriter("This is the shuffled deck:\n\n ");

		//WriteCardInformation(shuffledDeck);
		//FileWriter("These are the player decks:\n\n ");

		//WriteCardIDs(playerDecks);
		
	}

	public void OneRound() { // this runs every round until all but one players are eliminated
		draw = false;

		int choice = ChooseAttributeForAIPlayerRound(currentPlayerPosition);
		takeTopCards();
		int winner = Compare(choice, roundCards, classDeckArray);
	
		// prompt user to see winner of round

	//	wouldYouLikeToContinue();
		// This method breaks up every round so the game is
		// actually usable

		if (draw == false) {
			AwardAllCards(winner, roundCards); // change 1 to winner
		} else {
			System.out.println("THIS ROUND WAS A DRAW: " + roundCounter); // testing
			FileWriter("This is the drawPile:\n\n ");

			WriteCardInformation(drawPile);
		}
		FileWriter("These are the contents of players' decks after a round:\n\n ");

		WriteCardIDs(playerDecks);
		checkLoser(playerDecks);
		while (playerEliminated) { // this only runs if there are more than one player eliminated in one round
			checkLoser(playerDecks);
		}

//		System.out.println("ROUND: " + roundCounter); // everything here is just testing
//
//		for (int i = 0; i < playerRoundWins.length; i++) {
//			System.out.println("Player: " + playerRoundWins[i][0]);
//			System.out.println("Wins: " + playerRoundWins[i][1]);
//		}

	}

	public void getNumberOfPlayers() { // asks for numbers of AI players and then sets numberOfPlayers variable

	    	int number;
	    	
		   while (true) {
			 System.out.println("Enter numbers of AI Players between 1 & 4:");
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
		String showInfo = ShowCardInformation(classDeckArray, playerDecks[0].get(0)); // shows top card of users deck
		System.out.println(showInfo);

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
		System.out.println(playerList);
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
		System.out.println(cardPosition);

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
		System.out.println(playerDecks);
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
		FileWriter("These are the cards played this round:\n\n ");

		WriteCardInformation(roundCards);
		WiningCard=winningCardNumber;
		return winningCardNumber;
	}

	public void AwardAllCards(int winningNumber, ArrayList<Integer> round) { // checks for a draw and assigns cards to
																				// winner
		if (!drawPile.isEmpty()) { // change this to if it isnt empty do this

			roundCards.addAll(drawPile);
			drawPile.clear();
			FileWriter("This is the drawPile:\n\n ");

			WriteCardInformation(drawPile);
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

//		playerDecks[playerNumber].clear(); // this is just to test, remove this for game version
		playerEliminated = true;
		newPlayerDecks(playerDecks, playerNumber);
		currentPlayerPositions.remove(playerNumber);
		System.out.println("TESTING: PLAYER WAS ELIMINATED: " + playerNumber);
		// delete playerDecks[playerNumber] from playerDecks[]
		// delete player from playerList since we dont need them if they lose
	}

	public ArrayList<Integer>[] newPlayerDecks(ArrayList<Integer>[] playerDecks, int playerNumber) {

		int j = 0;
		ArrayList<Integer>[] temp = new ArrayList[playerDecks.length - 1];
		for (int i = 0; i < playerDecks.length; i++) {
			if (!(i == playerNumber)) {
				temp[i - j] = playerDecks[i];
			} else {
				j++;
			}

		}
		setPlayerDecks(temp);
		numberOfPlayers = numberOfPlayers - 1;

		return temp;
	}

	public void setPlayerDecks(ArrayList<Integer>[] newDecks) { // called in newPlayerDecks to set the new decks when a
																// player is eliminated
		playerDecks = newDecks;
	}

	public void checkLoser(ArrayList<Integer>[] playerDecks) { // to be run after every round

		if (playerDecks.length == 1) {
			playerWins = true;
		}

		for (int i = 0; i < playerDecks.length; i++) {
			if (playerDecks[i].isEmpty()) {
				if (i == 0) {
					userEliminated = true;
				}
				playerEliminated(i); // if two players are eliminated in same round this is bad
				break;
			}
			playerEliminated = false;
		}
		// add newplayerdecks here
	}

	public int randomizeStartingPosition() { // self explanatory
		int firstRound;
		firstRound = (int) (Math.random() * numberOfPlayers);
		currentPlayerPosition = firstRound;
		
		System.out.println(currentPlayerPosition);
		return currentPlayerPosition;

	}

	public int ChooseAttributeForAIPlayerRound(int currentPlayerPosition) { // determines whether an AI player or a user
																			// is playing

		int choice = -1;
		if (currentPlayerPosition == 0 && !userEliminated) {
			choice = (int) (Math.random() * 5 + 1); // ChooseAttribute();
		} else {
			choice = (int) (Math.random() * 5 + 1);
		}
		return choice;
	}

	public void FileWriter(String content) {
		File f = new File("TestLog.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter("TestLog.txt", true);
			fw.write(content + "\r\n");
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
		String temp = "ROUND: " + roundCounter + "\r\n";
		String s = "------------------------";
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

	public void WriteCardIDs(ArrayList<Integer>[] al) {
		String temp = "";
		String s = "\r\n------------------------";
		for (int i = 0; i < al.length; i++) {
			temp += "Player" + (i + 1);
			temp += "\r\n";
			for (int j = 0; j < al[i].size(); j++) {
				temp += "CardID: " + al[i].get(j);
				temp += "\r\n";

			}
		}
		FileWriter(temp + s);
	}

	// This method takes a String[][] and reformats it to a string that can be
	// written to the testlog

	public void Writing2D(String[][] string2d) {
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
	
	public void	RoundReport(){
		while(roundCounter>0) {
			
			System.out.print("Round "+roundCounter+ " Had "+numberOfPlayers +"players");
			System.out.println("The Round cards were ......");
			 
			for(int i =0; i<roundCards.size();i++) {
				
				String data ="";
				data= roundCards.get(i).toString();
				System.out.println(data);
				
				}
			if(draw==true) {
				System.out.println("This round was a draw");
			}
			else {
				
				System.out.println("This round was won  by "+currentPlayerPosition);
				System.out.println("The winning card was "+ WiningCard);
			}	
			}
		}
		
	
	public void GameReport() {
		while(roundCounter>0) {
			
		}
		
	}
	}

