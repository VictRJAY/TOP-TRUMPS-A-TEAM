package online;

import java.util.ArrayList;

public class Player {
	
	int id;
	String name;
	ArrayList<Card> cards;
	
	public Player() {}
	
	public Player(int id, String name, ArrayList<Card> cards) {
		super();
		this.id = id;
		this.name = name;
		this.cards = cards;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	
}
