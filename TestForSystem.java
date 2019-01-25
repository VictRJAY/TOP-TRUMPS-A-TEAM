import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestForSystem {

	public static void PrintOutAtTheBeginningOfProgram() {
		System.out.println("--------------------");
		System.out.println("--- Top Trumps   ---");
		System.out.println("--------------------");
	}
	
	public static void PrintOutAtTheBeginningOfGame() {
		System.out.println("Do you want to see past results or play a game?");
		System.out.println("   1: Print Game Statistics");
		System.out.println("   2: Play game");
		Scanner s = new Scanner(System.in);
		while (true) {
			System.out.println("Enter the number for your selection: ");
			int selection = s.nextInt();
			s.nextLine();
			if (selection == 1) {
				System.out.println("Print Game Statistics");
				break;
			} else if (selection == 2) {
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println("Game Start");
				break;
			}
		}
	}
	
	public static void PrintOutAtTheBeginningOfRound(int roundCounter, ArrayList<Integer> roundDeck, int shuffledPositionOfPlayer, ArrayList<Integer> playerDeck[]) {
		System.out.println("Round " + roundCounter);
		System.out.println("Round " + roundCounter + ": Players have drawn their cards");
		System.out.println("You drew '" + roundDeck.get(( shuffledPositionOfPlayer - 1 )) + "':"); // only show the card no. now, use show information method to get the name of that card
		System.out.println("   > Attribute1: " + roundDeck.get(( shuffledPositionOfPlayer - 1 ))); // only show the card no. now, use show information method to get the attribute1 of that card
		System.out.println("   > Attribute2: " + roundDeck.get(( shuffledPositionOfPlayer - 1 ))); // only show the card no. now, use show information method to get the attribute2 of that card
		System.out.println("   > Attribute3: " + roundDeck.get(( shuffledPositionOfPlayer - 1 ))); // only show the card no. now, use show information method to get the attribute3 of that card
		System.out.println("   > Attribute4: " + roundDeck.get(( shuffledPositionOfPlayer - 1 ))); // only show the card no. now, use show information method to get the attribute4 of that card
		System.out.println("   > Attribute5: " + roundDeck.get(( shuffledPositionOfPlayer - 1 ))); // only show the card no. now, use show information method to get the attribute5 of that card
		System.out.println("There are " + playerDeck[shuffledPositionOfPlayer - 1].size() + " cards in your deck");
	}
	
	public static void PrintOutAttributeSelectionWhenPlayerRound(int numberOfAttributeSelection) {
		System.out.println("It is your turn to select a category, the categories are:");
		System.out.println("   1: Attribute1");
		System.out.println("   2: Attribute2");
		System.out.println("   3: Attribute3");
		System.out.println("   4: Attribute4");
		System.out.println("   5: Attribute5");
		System.out.println("Enter the number for your attribute: ");
	}
	
	public static void PrintOutAttributeSelectionWhenAIPlayerRound(int AIPlayerNumber) {
		System.out.println("AI Player " + AIPlayerNumber + " choose attribute1");
	}
	
	public static void PrintOutResultWithoutDraw() {
		System.out.println("Round " + "(roundNumber)" + ": " + "(playerList.get(number/position))" + " You won this round");
		System.out.println("The winning card was '" + "(cardNo.->cardName)" + "':");
		System.out.println("   > Attribute1: " + "(value of attribute1)");
		System.out.println("   > Attribute2: " + "(value of attribute2)");
		System.out.println("   > Attribute3: " + "(value of attribute3)");
		System.out.println("   > Attribute4: " + "(value of attribute4)");
		System.out.println("   > Attribute5: " + "(value of attribute5)");
//		print out ' <--' after the chosen attribute
		System.out.println();
		System.out.println();
	}
	
	public static void PrintOutResultWithDraw() {
		System.out.println("Round " + "(roundNumber)" + ": This round was a Draw, common pile now has " + "(roundCard.size())" + " cards");
		System.out.println();
		System.out.println();
	}
	
	public static void GameOver() {
		System.out.println("Game End");
		System.out.println();
		System.out.println("The overall winnder was " + "(playerRemainingList.get(0))");
		System.out.println("Scores:");
		System.out.println("   " + "shuffledPlayerList.get(i)" + ": " + "(int[]/ArrayList<Integer> for score)"); // print out by using for loop
	}

	public static boolean CheckWinner(ArrayList<String> playerList) {
		
		boolean win = false;
		if (playerList.size() == 1) {
			win = true;
		}
		return win;
	}
	
	public static boolean CheckLoser(ArrayList<Integer> playerDeck[], int numberOfPlayerRemaining) {
		
		boolean lose = false;
		for (int i = 0; i < numberOfPlayerRemaining; i++) {
			if (playerDeck[numberOfPlayerRemaining].size() == 0) {
				lose = true;
				break;
			}
		}
		return lose;
	}
	
	public static int ChooseAttributeForAIPlayerRound (ArrayList<String> shuffledPlayerList, int numberOfShuffledPlayer) {
		
		int chooseAttribute;
		if (shuffledPlayerList.get(numberOfShuffledPlayer).equals("Player")) {
			chooseAttribute = 0;
		} else {
			chooseAttribute = 1;
		}
		return chooseAttribute;
	} // return 0 to ask player to choose an attribute, return 1 for all AI players to choose the first attribute only
	
	public static void FileReader(String fileName) {
		
	}
	
	public static void FileWriter(String content) {
		File f = new File("Statistics.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter("Statistics.txt", true);
			fw.write(content + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void FileWriter(String content, String fileName) {
		File f = new File(fileName + ".txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(fileName + ".txt", true);
			fw.write(content + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
