package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import application.phraseHashMap;

public class Main extends Application {
	
	Scene scene;
	ImageView iv;
	Image image;
	BorderPane root;
	Boolean textDone = false;
	
	Button submit;
	TextField textField;
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
			
			textField = new TextField ("Enter phrase here...");
			textField.prefWidthProperty().bind(hb.widthProperty());
			textField.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			Label history = new Label("Search history:");
			history.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			searchHistory = new ComboBox<String>();
			searchHistory.setPromptText("Search....");
			searchHistory.setEditable(true);
			searchHistory.setVisibleRowCount(4);
			searchHistory.prefWidthProperty().bind(hb.widthProperty());
			searchHistory.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			
			hb.getChildren().addAll(searchHistory, submit);
			hb.setSpacing(10);
			
//			StackPane stack = new StackPane();
//			stack.setAlignment(Pos.TOP_CENTER);
//		    stack.getChildren().addAll(history);
			
			root.setTop(hb);
			//root.setRight(sh);
			
			
			scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.show();

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
			//getPhraseID(textField.getText());
			int check = getPhraseID(searchHistory.getValue().toString());
			
			if (check != -1){
				//updateHistory(textField.getText());
				updateHistory(searchHistory.getValue().toString());
			}
			else{
				searchHistory.setValue("Phrase not available");
			}
			
			textDone = true;
		});
		
		scene.setOnKeyPressed(e->{
			if (e.getCode() == KeyCode.ENTER){
				//getPhraseID(textField.getText());
				int check = getPhraseID(searchHistory.getValue().toString());
				
				if (check != -1){
					//updateHistory(textField.getText());
					updateHistory(searchHistory.getValue().toString());
				}
				else{
					searchHistory.setValue("Phrase not available");
				}
				
				textDone = true;
			}
		});
		
		textField.setOnMouseClicked(e->{
			if (textDone == true){
				textField.clear();
				textDone =  false;
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
		int imageId = -1;
		imageId = map.searchHashMap(text);
		return imageId;
	}
	
	public void loadImage(int index, int id ){
		
		//Alphabet
		if(index == 0){
			image = new Image("file:images/"+id+".jpg");
		}
		//Phrases
		else if (index == 1){
			image = new Image("file:phrases/"+id+".jpg");
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