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
			
			//Root borderPane for all nodes
			root = new BorderPane();
			
			//HBox for search controls  
			HBox searchBar = new HBox();
			
			//Button for completing search actions 
			submit = new Button();
			submit.setText("Submit");
			submit.setMinWidth(100);
			submit.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			//Create editable ComboBox for queries and history 
			searchHistory = new ComboBox<String>();
			searchHistory.setPromptText("Search....");
			searchHistory.setEditable(true);
			searchHistory.setVisibleRowCount(4);
			searchHistory.prefWidthProperty().bind(searchBar.widthProperty());
			searchHistory.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			//Label for informing user when a query has be auto corrected
			search = new Label();
			search.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			search.setVisible(false);
			
			//Add nodes to HBox
			searchBar.getChildren().addAll(searchHistory, submit);
			searchBar.setSpacing(10);
			
			//VBox for search controls and user info label
			VBox vb = new VBox();
			vb.getChildren().addAll(searchBar, search);
			
			//Add VBox to top of root borderPane
			root.setTop(vb);

			//Add root pane to scene, and show stage
			scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//Launch main program
			runProgram();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void runProgram(){
		
		//When the submit button is clicked
		submit.setOnAction(e->{
			//Set information label to invisible 
			search.setVisible(false);
			//Get the ID of the user entered phrase
			int check = getPhraseID(searchHistory.getValue().toString());
			
			//Phrase ID -1 when phrase not recognised
			//When phrase is recognised update the search history
			if (check != -1){
				updateHistory(searchHistory.getValue().toString());
			}
			//Otherwise inform user that the entered phrase is not recognised
			else{
				searchHistory.setValue("Phrase not available");
			}
			
			
		});
		
		//Set same functionality for pressing the ENTER key as the submit button
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
		
		//Set defaults for error message 
		int imageId = -1;
		int index = 0;
		
		//Trim leading and trailing whitespace from enter string
		text = text.trim();
		//Convert entered string to all upper case for string comparisons 
		text = text.toUpperCase();
		
		
		
		//If single character entered...
		if (text.length() == 1){
			char c = text.charAt(0);
			
			//Check if entered character is a letter...
			if(Character.isLetter(c) == true){
				//ImageId for alphabet character 0-25
				imageId = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c);
				index = 0;
			}
			//Check if entered character is a number
			else if(Character.isDigit(c) == true){
				//ImageId for numbers from 26 onwards...
				imageId = Character.getNumericValue(c) + 26;
				index = 0;
			}
		}
		//If entered string is longer then a single character, search HashMap of known phrases
		else{
			imageId = evaluateString(text);
			index = 1;
		}
		
		//Load the image corrospondiong to the enter string
		loadImage(index, imageId);
		
		//Return image ID value
		return imageId;
	}
	
	public int evaluateString(String text){
		
		//Store the hash map key value, similarity and position of string closest to user input string
		CustomContainer indexAndSimilarity = map.searchHashMap(text);
		//int imageID = -1;
		int similarity = indexAndSimilarity.getSimilarity();
		int index = indexAndSimilarity.getIndex();
		int position = indexAndSimilarity.getPosition();
		
		//If there is a 100% match between user input and mapped string
		if (similarity == 100)
		{
			//Returns the id for the image for corresponding string set
			return index;
		} 
		//If there is an larger than 80% match between user input and mapped string
		else if (similarity > 80)
		{
			// Auto-complete the users input value
			searchHistory.setValue(map.getMapString(index, position).toLowerCase());
			//Indicate that an auto-complete has occurred
			search.setText("Auto corrected '" + text.toLowerCase() + "' to " + map.getMapString(index, position).toLowerCase());
			search.setVisible(true);
			return index;
			
		} 
		//If there is an larger than  70% match between user input and mapped string
		else if (similarity > 70)
		{
			// Alert user to the string that is close to their input, and prompt and "Did you mean X " alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Phrase Not Found!");
			alert.setHeaderText("Did you mean: " +map.getMapString(index, position).toLowerCase()+"?");
			
			ButtonType yes = new ButtonType("Yes");
			ButtonType no = new ButtonType("No");
			
			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(yes,no);
			
			Optional<ButtonType> result = alert.showAndWait();
			//Set actions for each button type
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
			// Display error message if there is no match
			System.out.println("no phrase");
			return -1;
		}
	}
	
	public void loadImage(int index, int id ){
		
		//Load image from alphabet and number folder
		if(index == 0){
			image = new Image("file:images/"+id+".jpg");
		}
		//Load image from phrases folder
		else if (index == 1){
			image = new Image("file:phrases/"+id+".gif");
		}
		
		//Create and add ImageView for selected image
		iv = new ImageView(image);
		iv.setPreserveRatio(true);
		iv.setFitWidth(300);
		root.setCenter(iv);
	}

	//Update the users search history
	public void updateHistory(String text){
		
		//Loop through current history and remove any duplicates from the history before adding entered
		//  value to the top of the history
		for (int i = 0; i < searchHistory.getItems().size() ; i++){
			if (text == searchHistory.getItems().get(i)){
				searchHistory.getItems().remove(i);
				break;
			}
		}
		
		//Add entered string to top of hsitory
		searchHistory.getItems().add(0, text);
		searchHistory.setValue(text);
	}
	
}