package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import application.phraseHashMap;

public class Main extends Application {
	
	Scene scene;
	ImageView iv;
	Image image;
	BorderPane root;
	
	
	Button submit;
	Label search;
	phraseHashMap map = new phraseHashMap();
	
	ComboBox<String> searchHistory;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			root = new BorderPane();
			HBox hb = new HBox();
			
			submit = new Button();
			submit.setText("Submit");
			submit.setMinWidth(100);
			submit.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			Label history = new Label("Search history:");
			history.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			searchHistory = new ComboBox<String>();
			searchHistory.setPromptText("Search....");
			searchHistory.setEditable(true);
			searchHistory.setVisibleRowCount(4);
			searchHistory.prefWidthProperty().bind(hb.widthProperty());
			searchHistory.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			search = new Label("Auto Corrected!");
			search.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			search.setVisible(false);
			
			hb.getChildren().addAll(searchHistory, submit);
			hb.setSpacing(10);
			
			VBox vb = new VBox();
			vb.getChildren().addAll(hb, search);
			
			root.setTop(vb);

			scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// TEST
			
			readPhraseBook();
			
			// /TEST
			runProgram();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void runProgram(){
		
		submit.setOnAction(e->{
			search.setVisible(false);
			int check = getPhraseID(searchHistory.getValue().toString());
			
			if (check != -1){
				updateHistory(searchHistory.getValue().toString());
			}
			else{
				searchHistory.setValue("Phrase not available");
			}
			
			
		});
		
		scene.setOnKeyPressed(e->{
			if (e.getCode() == KeyCode.ENTER){
				search.setVisible(false);
				int check = getPhraseID(searchHistory.getValue().toString());
				
				if (check != -1){
					updateHistory(searchHistory.getValue().toString());
				}
				else{
					searchHistory.setValue("Phrase not available");
				}
			
			}
		});
		
		
	}
	
	public int getPhraseID(String text){
		int imageId = -1;
		int index = 0;
		text = text.toUpperCase();
		//System.out.println(text.length());
		if (text.length() == 1){
			char c = text.charAt(0);
			//System.out.println(c);
			imageId = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c);
			index = 0;
		}
		else{
			imageId = evaluateString(text);
			index = 1;
		}
		loadImage(index, imageId);
		return imageId;
	}
	
	public int evaluateString(String text){
		
		CustomContainer indexAndSimilarity = map.searchHashMap(text);
		//int imageID = -1;
		int similarity = indexAndSimilarity.getSimilarity();
		int index = indexAndSimilarity.getIndex();
		int position = indexAndSimilarity.getPosition();
		
		if (similarity == 100)
		{
			return index;
		} else if (similarity > 80)
		{
			// Auto-complete
			searchHistory.setValue(map.getMapString(index, position).toLowerCase());
			search.setVisible(true);
			return index;
			
		} else if (similarity > 70)
		{
			// Display 'did you mean...?'
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Phrase Not Found!");
			alert.setHeaderText("Did you mean: " +map.getMapString(index, position).toLowerCase()+"?");
			
			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No");
			
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(yes,no);
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yes){
			    // ... user chose OK
				searchHistory.setValue(map.getMapString(index, position).toLowerCase());
				return index;
			} else {
			    // ... user chose CANCEL or closed the dialog
				return -1;
			}
		} else 
		{
			// Display error message
			System.out.println("no phrase");
			return -1;
		}
		
		//return 0;
		
//		int imageId = -1;
//		imageId = map.searchHashMap(text);
//		return imageId;
	}
	
	public void loadImage(int index, int id ){
		
		//Alphabet
		if(index == 0){
			image = new Image("file:images/"+id+".jpg");
		}
		//Phrases
		else if (index == 1){
			image = new Image("file:phrases/"+id+".gif");
		}
		
		iv = new ImageView(image);
		iv.setPreserveRatio(true);
		iv.setFitWidth(300);
		root.setCenter(iv);
		System.out.println("loaded image");
	}

	public void updateHistory(String text){
		
		for (int i = 0; i < searchHistory.getItems().size() ; i++){
			if (text == searchHistory.getItems().get(i)){
				searchHistory.getItems().remove(i);
				break;
			}
		}
		
		searchHistory.getItems().add(0, text);
		searchHistory.setValue(text);
	}
	
	public void readPhraseBook()
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
			
			final Character[] charactersArray = 
					charArrayList.toArray(new Character[charArrayList.size()]);
			
			for (Character c : charactersArray)
			{
				fullPhraseBook += c.toString();
			}
			
			String[] delimitedPhraseSets = fullPhraseBook.split("-");
			ArrayList<String> delimitedPhrases;
			
			for (int j = 0; j < delimitedPhraseSets.length; j++)
			{
				String[] tempPhrases = delimitedPhraseSets[j].split("_");
				delimitedPhrases = new ArrayList<>(Arrays.asList(tempPhrases));
			}
			
			
			System.out.println(Arrays.toString(delimitedPhrases));
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}