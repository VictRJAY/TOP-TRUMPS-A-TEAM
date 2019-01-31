package commandline;

import java.io.*;
import java.util.ArrayList;

public class testing {
	GameCalc c = new GameCalc();
		
	// The below method is passed a string which it then writes to a file called TextLog.txt

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
		String temp = "";
		String s = "------------------------";
		for (int i = 0; i < al.size(); i++) {
			temp += c.ShowCardInformation(c.classDeckArray, al.get(i));
			temp += "\r\n";
		}
		FileWriter(temp);
	}

	/*
	 * This method takes an ArrayList<Integer>[] playerDeck arrays, which are in
	 * turned filled with card ID's the method then adds these card ID's to a
	 * readable string and writes it to the testlog.
	 * 
	 * Things to be run by this method: playerDecks after distribution
	 * playerDecks after cards are added/removed
	 */

	public void WriteCardIDs(ArrayList<Integer>[] al) {
		String temp = "";
		String s = "\r\n------------------------";
		for (int i = 0; i < al.length; i++) {
			temp += "Player" + (i + 1);
			temp += "\r\n";
			for (int j = 0; j < al[i].size(); j++) {
				temp += "CardID: " + al[i].get(i);
				temp += "\r\n";

			}
		}
		FileWriter(temp + s);
	}
	
	// This method takes a String[][] and reformats it to a string that can be written to the testlog

	public void Writing2D(String[][] string2d) {
		String temp = "";
		for (int i = 0; i < string2d.length; i++) {
			for (int j = 0; j < string2d[i].length; j++) {
				temp += string2d[i][j];
			}
			temp += "\r\n";
		}
		FileWriter(temp);
	}

}
