package topTrumps;

import java.util.ArrayList;

public class classDisplay extends GameCalc{
	
	ArrayList<Integer> shuffledDeck1 = new ArrayList<Integer>();
	ArrayList<Integer> drawPile1 = new ArrayList<Integer>();
	ArrayList<Integer> playerDecks1[];
	ArrayList<String>  roundData1 =new ArrayList<>();
	
	public classDisplay() {
		
		
		 GameCalc gc= new GameCalc();
		 
		 this.drawPile1 = gc.drawPile;
		 this.shuffledDeck1 =gc.shuffledDeck;
		 this.drawPile1=gc.drawPile;
		 this.playerDecks1 =gc.playerDecks;
		// this.roundData1 = gc.roundData;
		
		}
	
	public void saveToDB() {
		
		
	}
	
	public void readFromDB() {
		
		
	}
	
	
	public String display() {
		
		
		return null;
	}
	

	public String displayRound(ArrayList<String> roundData) {
		
		return null;
	}
	
}
