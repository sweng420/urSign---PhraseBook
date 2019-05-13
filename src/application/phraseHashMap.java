package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class phraseHashMap {
	
	HashMap<Integer, String[]> phrases;
	int mapLength;
	public phraseHashMap(){

		//Reads phrases list from a text file
		ArrayList<String[]> phraseSets = readPhraseBook();
		
		// Create a HashMap
	    phrases = new HashMap<Integer, String[]>();
		
		for (int i = 0; i < phraseSets.size(); i++){
			String[] temp = phraseSets.get(i);
			phrases.put(i, temp);
		}
		
//		//Phrase Strings
//		String[] hello = new String[]{"HELLO","HEY","HI ","HOWDY"};
//		String[] goodbye = new String[]{"BYE" ,"GOOD BYE" ,"LATER ALLIGATOR"};
//		String[] howAreYou = new String[]{"HOW ARE YOU?","HOW ARE YOU", "HOW R U"};
//		String[] yes = new String[]{"YES","YEAH","YE"};
//		String[] no = new String[]{"NO","NAH","NOPE"};
//		String[] please = new String[]{"PLEASE","PLS"};
//		String[] thankYou = new String[]{"THANK YOU","THANKS","CHEERS","THX"};
//		String[] sorry = new String[]{"SORRY","MY BAD","SOZ"};

//	    // Add keys and values
//	    phrases.put(0, hello);
//	    phrases.put(1, goodbye);
//	    phrases.put(2, howAreYou);
//	    phrases.put(3, yes);
//	    phrases.put(4, no);
//	    phrases.put(5, please);
//	    phrases.put(6, thankYou);
//	    phrases.put(7, sorry);
	   
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
	
	public ArrayList<String[]> readPhraseBook()
	{
		// Pass file as parameter and instantiate FileReader 
		
		FileReader reader = null;
		try {
			reader = new FileReader("Phrases.txt");
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist in this location");
			e.printStackTrace();
		} 
		  
		int i; 
		
		String fullPhraseBook = "";
		ArrayList<Character> charArrayList = new ArrayList<Character>();
		
		try {
			i = reader.read();
			
			// As long as there is something to read...
			
			while (i != -1)
			{
				// ...Add character to an ArrayList 
				
				charArrayList.add((char)i);
				
				// Read next character of phrasebook
				
				i = reader.read();
			}
			
			// Convert ArrayList to Array
			
			final Character[] charactersArray = 
					charArrayList.toArray(new Character[charArrayList.size()]);
			
			// Convert every element in array to string and add it to phrase book
			
			for (Character c : charactersArray)
			{
				fullPhraseBook += c.toString();
			}
			
			// Split into phrase sets
			
			String[] delimitedPhraseSets = fullPhraseBook.split("-");
			
			ArrayList<String[]> delimitedPhrases;
			
			delimitedPhrases = new ArrayList<>();
			
			for (int j = 0; j < delimitedPhraseSets.length; j++)
			{
				String[] tempPhrases = delimitedPhraseSets[j].split("_");
				
				delimitedPhrases.add(tempPhrases);
			}
			
			return delimitedPhrases;
//			for (int j = 0; j < delimitedPhrases.size(); j++)
//			{
//				System.out.println(Arrays.toString(delimitedPhrases.get(j)));
//			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; 
		
	}
	
}

