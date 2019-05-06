package application;

import java.util.HashMap;

import javafx.util.Pair;
import me.xdrop.fuzzywuzzy.FuzzySearch;

public class phraseHashMap {
	
	HashMap<Integer, String[]> phrases;
	int mapLenght;
	public phraseHashMap(){
		//Phrase Strings
		String[] hello = new String[]{"HELLO","HEY","HI","HOWDY"};
		String[] goodbye = new String[]{"BYE" ,"GOOD BYE" ,"LATER ALLIGATOR"};
		String[] howAreYou = new String[]{"HOW ARE YOU", "HOW R U"};
		
		
		// Create a HashMap
	    phrases = new HashMap<Integer, String[]>();
	    // Add keys and values
	    phrases.put(0, hello);
	    phrases.put(1, goodbye);
	    phrases.put(2, howAreYou);
	   
	    mapLenght = phrases.size();
	    
	}
	
	public String getMapString(int index, int position){
		String phrase[] = phrases.get(index);
		return phrase[position];
	}

	public CustomContainer searchHashMap(String text){
		String temp[];
		CustomContainer indexAndSimilarity = new CustomContainer(-1, 0, 0);
		
		for (int i = 0; i < this.mapLenght; i++){
			temp =  phrases.get(i);
			for(int j = 0; j < temp.length ; j++ ){
				
				int similarity = FuzzySearch.ratio(text, temp[j]);
				
				if (similarity > indexAndSimilarity.getSimilarity())
				{
					indexAndSimilarity.setSimilarity(similarity);
					indexAndSimilarity.setIndex(i);
					indexAndSimilarity.setPosition(j);
				}
				
//				if (temp[j].equals(text)){
//					System.out.println(i);
//					return indexAndSimilarity;
//				}
			}
		}
		
		return indexAndSimilarity;
	}
	

}
