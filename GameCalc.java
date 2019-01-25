import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameCalc extends Deck {

	ArrayList<Integer> shuffledDeck = new ArrayList<Integer>();
	ArrayList<Integer> drawPile = new ArrayList<Integer>();
	ArrayList<Integer> playerDecks[];
	int numberOfPlayers;
	int roundCounter;
	Scanner s = new Scanner(System.in);

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

	public int ChooseAttribute() { // chooses an attribute :)

		int number;
		while (true) {
			System.out.println("Choose an attribute:");
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

	public void ShuffleDeck() { // shuffles deck positions

		// you are using an arraylist here despite set positions because it's easier to
		// shuffle
		ArrayList<Integer> cardPosition = new ArrayList<Integer>();

		for (int i = 0; i < 50; i++) {
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
		ArrayList<Integer> roundCards = new ArrayList<Integer>();
		for (int i = 0; i < numberOfPlayers; i++) {
			roundCards.add(playerDecks[i].get(0));
			playerDecks[i].remove(0);
		}
		roundCounter++; // the method also increments the roundCounter since it's called at the
						// beginning of a round
		return roundCards;
	}

	public int Compare(int attributeNumber, ArrayList<Integer> round, String[][] card) { // this method is passed an
																							// attributeNumber and then
																							// checks that attribute in
																							// an array of cards

		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < round.size(); i++) {
			int tempCardNo = round.get(i);
			for (int j = 1; j < card.length; j++) {
				if (tempCardNo == Integer.valueOf(card[j][0])) {
					temp.add(Integer.valueOf(card[j][attributeNumber + 1]));
				}
			}
		}
		int maxNumber = 0;
		int number = 0;
		for (int i = 0; i < temp.size(); i++) {
			if (maxNumber < temp.get(i)) {
				maxNumber = temp.get(i);
				number = i;
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
			number = 666; // this is the number you've assigned to a drawn round, if this method returns
			IfDraw(round); // adds cards to drawPile
		}
//		return maxNumber;
		return number;
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

	}

	public void IfDraw(ArrayList<Integer> round) {
			drawPile.addAll(round);
	}

	public void playerEliminated(int playerNumber) { // complete this when you know what you are doing
		numberOfPlayers = numberOfPlayers - 1;
		// delete playerDecks[playerNumber] from playerDecks[]
		// delete player from playerList since we dont need them if they lose
	}
}
