package commandline;


public class Deck {

	static String[][] cards = new String[51][7]; // stores initial deck, never changes

	public static String[][] DeckCards() { // Fills deck with data

		cards[0][0] = "Card No.";
		cards[0][1] = "Card Name";
		for (int i = 2; i < cards[0].length; i++) {
			String name = "Card Attribute No." + (i - 1);
			cards[0][i] = name;
		}
		for (int i = 1; i < cards.length; i++) {
			cards[i][0] = String.valueOf(i);
			String name = "Card Name No." + i;
			cards[i][1] = name;
		}
		for (int i = 1; i < cards.length; i++) {
			for (int j = 2; j < cards[i].length; j++) {
				cards[i][j] = String.valueOf((int) (Math.random() * 10 + 1));
			}
		}
		return cards;
	}
}
