package commandline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWriting {

	boolean startOfGame = true;
	boolean userEliminated = false;
	protected String[][] classDeckArray = new String[41][7];
	ArrayList<Integer> activePlayerPositions = new ArrayList<Integer>();

	public void FileWriter(String content) {

		File f = new File("TestLog.txt");
		String divider = "\r\n-------------------------------\r\n";
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
			fw.write(content + divider);
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

	public String ShowCardInformation(String[][] cards, int cardID) { // takes the ID of a card, compares it to the deck
		// and returns all attributes

		String information = "";

		for (int j = 1; j < cards.length; j++) {
			if (cardID == Integer.valueOf(cards[j][0])) {
				information = " " + cards[0][1] + ": " + cards[j][1] + "\n(1)" + cards[0][2] + ": " + cards[j][2]
						+ "\n(2)" + cards[0][3] + ": " + cards[j][3] + "\n(3)" + cards[0][4] + ": " + cards[j][4]
						+ "\n(4)" + cards[0][5] + ": " + cards[j][5] + "\n(5) " + cards[0][6] + ": " + cards[j][6]
						+ "\n";
			}
		}

		return information;
	}

	public void setClassDeckArray(String[][] classDeckArray) {
		this.classDeckArray = classDeckArray;
	}

	public void setActivePlayerPositions(ArrayList<Integer> activePlayerPositions) {
		this.activePlayerPositions = activePlayerPositions;
	}

}
