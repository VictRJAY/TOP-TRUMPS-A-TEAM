package online;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GameCalcO{
	
	DeckReader deckReader;

	ArrayList<Card> shuffledDeck = new ArrayList<Card>();
	public ArrayList<Player> Players = new ArrayList<Player>();
	ArrayList<Card> drawPile = new ArrayList<Card>(); // where we store drawn cards
	public int[] playersRoundWins = {0,0,0,0,0}; //since there is a maximum of five players
	public int activeID = -1;
	public int roundChoice = 0;
	public int winnerID = -1;
	
	public int numberOfPlayers = 0;
	public int roundCounter = 1;
	public int drawCounter = 0;
																

	public void StartOfGame(int playerNumbers) { // this runs once at the start of every game
		getNumberOfPlayers(playerNumbers);
		deckReader = new DeckReader(new File("StarCitizenDeck.txt"));
		ShuffleDeck();
		DistributePlayerDecks();
		activeID = (int) (Math.random() * numberOfPlayers);

	}
	
	public void getNumberOfPlayers(int number) { // asks for numbers of AI players and then sets numberOfPlayers variable
		if (number >= 1 && number <= 5) {
			numberOfPlayers = number;
		}
	}
	
	public void ShuffleDeck() { // shuffles deck positions
		shuffledDeck = deckReader.getDeck();
		Collections.shuffle(shuffledDeck);
	}

	public void DistributePlayerDecks() {

		// initialise player list where id 0 is for user
		for(int i=0;i<numberOfPlayers;i++) {
			if(i==0)
				Players.add(new Player(i,"User",new ArrayList<Card>()));
			else
				Players.add(new Player(i,"AI Player "+i,new ArrayList<Card>()));
		}
		int index = 0;
		for(Card c:shuffledDeck) { // For every card in shuffledDeck let "c" represent the card one by one
			if(index == numberOfPlayers) {
				index = 0;
			}
			Players.get(index).getCards().add(c);
			index++;
		}
	}
	
	public void OneRound() { // this runs every round until all but one players are eliminated
		ArrayList<Card> winnerCards = Compare();
		winnerID = getWinnerID(winnerCards); //if its a draw it will return -1
		if(winnerID != -1) {
			playersRoundWins[winnerID] += 1; //increment the winner round wins by one
			activeID = winnerID;
		}
		if(winnerCards.size()>1) {
			addToDrawPile();
			drawCounter++;
		}else {
			addToWinnerDeck();
		}

		removeLoser();
	}
	
	public boolean isGameOver() {
		return Players.size() == 1;
	}
	
	public void setRoundChoice() {
		if(!isPlayerTurn()) {
			roundChoice = (int) (Math.random() * 5 + 1);
		}
	}
	
	private int getWinnerID(ArrayList<Card> winnerCards) {
		int id = -1;
		//making sure its not a draw
		if(winnerCards.size() == 1) {
			for(Player p:Players) {
				if(p.getCards().get(0).equals(winnerCards.get(0))) {
					id = p.getId();
				}
			}
		}
		return id;
	}
	
	public void addToWinnerDeck() {
		for(Player p: Players) {
			if(p.getId() == winnerID) {
				p.getCards().addAll(getTopCards());
				p.getCards().addAll(drawPile);
			}
		}
		for(Player p: Players) {
			p.getCards().remove(0);
		}
		drawPile = new ArrayList<Card>();
	}

	public void addToDrawPile() {
		drawPile.addAll(getTopCards());
		for(Player p: Players) {
			p.getCards().remove(0);
		}
	}

	public void removeLoser() { // to be run after every round
		ArrayList<Player> losers = new ArrayList<Player>();
		for(Player p: Players) {
			if(p.getCards().size() == 0) {
				losers.add(p);
			}
		}
		Players.removeAll(losers);

	}
	
	public ArrayList<Card> Compare() {
		
		Card winningCard = getTopCards().get(0);
		
		//find highest card of selected attribute
		for(Card c:getTopCards()) {
			int currentAttVal = Integer.parseInt(c.getAttributeValues().get(roundChoice));
			int WinCardAttVal = Integer.parseInt(winningCard.getAttributeValues().get(roundChoice));
			if(currentAttVal >= WinCardAttVal) {
				winningCard = c;
			}	
		} 
		
		//in case more than one card won this round
		ArrayList<Card> winnerCards = new ArrayList<Card>();
		for(Card c:getTopCards()) {
			int currentAttVal = Integer.parseInt(c.getAttributeValues().get(roundChoice));
			int WinCardAttVal = Integer.parseInt(winningCard.getAttributeValues().get(roundChoice));
			if(currentAttVal == WinCardAttVal) {
				winnerCards.add(c);
			}	
		}

		return winnerCards;
	}

	
	//API FUNCTIONS
	public boolean isPlayerTurn() {
		return activeID == 0;
	}

	public Player getActivePlayer() {
		for(Player p:Players) {
			if(p.getId() == activeID) {
				return p;
			}
		}
		return new Player();
	}
	
	public Player getPlayer(int id) {
		for(Player p:Players) {
			if(p.getId() == id) {
				return p;
			}
		}
		return new Player();
	}
	
	public ArrayList<Card> getTopCards(){
		ArrayList<Card> topCards = new ArrayList<Card>();
		for(Player p:Players) {
			topCards.add(p.getCards().get(0));
		}
		return topCards;
	}
	
	public ArrayList<Player> getPlayersRoundInfo(){
		ArrayList<Player> playerRInfo = new ArrayList<Player>();
		for(Player p:Players) {
			ArrayList<Card> topCards = new ArrayList<Card>();
			topCards.add(p.getCards().get(0));
			playerRInfo.add( new Player(p.getId(),p.getName(),topCards));
		}
		
		return playerRInfo;
	}
 
	public String beginningOfRoundInfo() {
		return "Round "+roundCounter+": "+getActivePlayer().getName()+" turn";
	}

	public String endOfRoundInfo() {
		return "Round "+roundCounter+": "+getActivePlayer().getName()+" selected "+Card.attributeHeaders.get(roundChoice-1);
	}
	
	public String finish() {
		if(isGameOver()) {
			
			return "Round "+roundCounter+": "+getPlayer(activeID).getName()+" won this round. GAME OVER!!!";
		}
		
		if(winnerID == -1) {
			return "Round "+roundCounter+": was a draw. Draw Pile: "+drawPile.size();
		}else {
			return "Round "+roundCounter+": "+getPlayer(winnerID).getName()+" won this round";
		}
		
	}

}