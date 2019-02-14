package online;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DeckReader {
	//stores the deck cards info
	ArrayList<Card> deck = new ArrayList<Card>();
	
	public DeckReader(File deckFile) {

		//to read the file context
        BufferedReader br = null;
        //to hold each line in the file
        String line = "";
        //to splice the line string
        String cvsSplitBy = " ";

        try {
            br = new BufferedReader(new FileReader(deckFile));
            while ((line = br.readLine()) != null) {

                //break the line into substring (string array) with the splicer
                String[] deckLine  = line.split(cvsSplitBy);

                //try to get card info from the string array
                try {
                	ArrayList<String> cardInfo = new ArrayList<String>();
                	//change from string array to string arraylist
                	for(String s:deckLine) {
                		if(!s.equalsIgnoreCase("description"))
                			cardInfo.add(s);
                	}
                	//save it the the static card header arraylist
                	if(Card.attributeHeaders == null)
                		Card.attributeHeaders = cardInfo;
                	else {
                		Card card = new Card(cardInfo);
                		deck.add(card);
                	}
                }catch (Exception e) {
				}
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}

	
}
