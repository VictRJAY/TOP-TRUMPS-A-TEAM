package topTrumps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StartGame {
	
	public static void main(String[] args) {
		
		PrintOutAtTheBeginningOfProgram();
		PrintOutAtTheBeginningOfGame();
		
		
	}
	
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
                classDisplay cD  = new classDisplay()		;		
				cD.display();
				//
				break;
			} else if (selection == 2) {
				System.out.println(" Do you want to play on the CLI or Webservice?");
				System.out.println( "  -c : Command Line Interface");
				System.out.println("   -o : Webservice  ");
				
				Scanner ins = new Scanner(System.in);
				
				String mode= ins.nextLine();
				
				if(mode=="-c") {
					
					PrintOutAtTheBeginningOfRound();
					startCLI();
				}
				else if (mode =="-c") {
					PrintOutAtTheBeginningOfRound();
					startWebService();
				}
				else {
					System.out.println("Invalid Choice ");
					PrintOutAtTheBeginningOfGame();
				}
				
				
				
				
			}
		}
	}
	
	private static void startWebService() {
		// TODO Auto-generated method stub
		
	}

	private static void startCLI() {
		GameCalc gc = new GameCalc();
		gc.StartOfGame();
		
		}

	public static void PrintOutAtTheBeginningOfRound() {
		System.out.println("Round Start");
	}
	
	
	
		
	}

