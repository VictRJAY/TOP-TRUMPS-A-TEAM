import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		GameCalc c = new GameCalc();
		
		c.DeckCards();
		c.ShuffleDeck();
		c.getNumberOfPlayers();
		
		ArrayList<String> p = c.ListOfPlayers(c.numberOfPlayers);
		System.out.println(p);
		
		ArrayList<Integer> temp[] = c.DistributePlayerDecks(c.numberOfPlayers, c.shuffledDeck);
		String info = c.ShowCardInformation(c.cards, temp[0].get(0));
		String info1 = c.ShowCardInformation(c.cards, temp[1].get(0));
		String info2 = c.ShowCardInformation(c.cards, temp[2].get(0));
		String info3 = c.ShowCardInformation(c.cards, temp[3].get(0));
		String info4 = c.ShowCardInformation(c.cards, temp[4].get(0));
		System.out.println(info);
		System.out.println(info1);
		System.out.println(info2);
		System.out.println(info3);
		System.out.println(info4);
		
		ArrayList<Integer> temp2 = c.takeTopCards();
		
		int tempINT = c.Compare(1, temp2, c.cards);
		System.out.println(tempINT);
		
	}
}
