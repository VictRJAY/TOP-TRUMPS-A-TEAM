package online;

import java.util.ArrayList;

public class Card {
	
	ArrayList<String> attributeValues;
	public static ArrayList<String> attributeHeaders; //in case the deck is different from star citizen
	 
	public Card(ArrayList<String> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	public String toS() {
		String retValue = ""+attributeValues.get(0);
		for(int i =0;i<attributeHeaders.size();i++) {
			retValue += String.format(" %s: %s ", attributeHeaders.get(i),attributeValues.get(i+1));
		}
		return retValue;
	}

	public ArrayList<String> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(ArrayList<String> attributeValues) {
		this.attributeValues = attributeValues;
	}

}
