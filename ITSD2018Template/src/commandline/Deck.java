package commandline;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {

	static String[][] classDeckArray = new String[41][7];

	// The below method takes takes a .txt file filled with deck information and
	// stores it as a String[][] array

	public String[][] FileReader() {
		File file = new File("StarCitizenDeck.txt"); // CHANGE DECK HERE
		BufferedReader br;
		String test = "";
		String[] card = null;
		ArrayList<String> deck = new ArrayList<String>();
		String[][] deckArray = new String[41][7];

		try {
			br = new BufferedReader(new FileReader(file));
			while ((test = br.readLine()) != null) {
				card = test.split(" ");
				for (int i = 0; i < card.length; i++) {
					deck.add(card[i]);
				}
			}
		} catch (IOException e) {
		//	e.printStackTrace();
		}
		for (int i = 0; i < deckArray.length; i++) {
			deckArray[i][0] = String.valueOf(i);
			for (int j = 1; j < deckArray[i].length; j++) {
				deckArray[i][j] = deck.get(i * 6 + j - 1);
			}
		}
		deckArray[0][0] = "CardID";
		classDeckArray = deckArray;
		return deckArray;
	}
}
