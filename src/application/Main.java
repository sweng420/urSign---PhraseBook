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
		
		//Set defaults for error msg 
		int imageId = -1;
		int index = 0;
		
		//Convert entered string to all upper case for string comparisons 
		text = text.toUpperCase();
		
		//If single character entered...
		if (text.length() == 1){
			char c = text.charAt(0);
			if(Character.isLetter(c) == true){
				//ImageId for alphabet character 0-25
				imageId = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c);
				index = 0;
			}
			else if(Character.isDigit(c) == true){
				//ImageId for numbers from 26 onwards...
				
				imageId = Character.getNumericValue(c) + 26;
				System.out.println(imageId);
				index = 0;
			}
		}
		else{
			imageId = evaluateString(text);
			index = 1;
		}
		loadImage(index, imageId);
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
	
}