package application;

import java.util.HashMap;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class phraseHashMap {
	
	HashMap<Integer, String[]> phrases;
	int mapLength;
	public phraseHashMap(){
		//Phrase Strings
		String[] hello = new String[]{"HELLO","HEY","HI ","HOWDY"};
		String[] goodbye = new String[]{"BYE" ,"GOOD BYE" ,"LATER ALLIGATOR"};
		String[] howAreYou = new String[]{"HOW ARE YOU?","HOW ARE YOU", "HOW R U"};
		String[] yes = new String[]{"YES","YEAH","YE"};
		String[] no = new String[]{"NO","NAH","NOPE"};
		String[] please = new String[]{"PLEASE","PLS"};
		String[] thankYou = new String[]{"THANK YOU","THANKS","CHEERS","THX"};
		String[] sorry = new String[]{"SORRY","MY BAD","SOZ"};
		
		// Create a HashMap
	    phrases = new HashMap<Integer, String[]>();
	    // Add keys and values
	    phrases.put(0, hello);
	    phrases.put(1, goodbye);
	    phrases.put(2, howAreYou);
	    phrases.put(3, yes);
	    phrases.put(4, no);
	    phrases.put(5, please);
	    phrases.put(6, thankYou);
	    phrases.put(7, sorry);
	   
	    mapLength = phrases.size();
	}
	
	public String getMapString(int index, int position){
		String phrase[] = phrases.get(index);
		return phrase[position];
	}

	public CustomContainer searchHashMap(String text){
		String tempString[];
		CustomContainer indexAndSimilarity = new CustomContainer(-1, 0, 0);
		
		for (int i = 0; i < this.mapLength; i++){
			tempString =  phrases.get(i);
			for(int j = 0; j < tempString.length ; j++ ){
				
				int similarity = FuzzySearch.ratio(text, tempString[j]);
				
				if (similarity > indexAndSimilarity.getSimilarity())
				{
					indexAndSimilarity.setSimilarity(similarity);
					indexAndSimilarity.setIndex(i);
					indexAndSimilarity.setPosition(j);
				}
			}
		}
		return indexAndSimilarity;
		
	}
}
