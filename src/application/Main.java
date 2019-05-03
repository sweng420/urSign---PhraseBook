package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import application.phraseHashMap;

public class Main extends Application {
	
	ImageView iv;
	Image image;
	BorderPane root;
	Boolean textDone = false;
	
	Button submit;
	TextField textField;
	phraseHashMap map = new phraseHashMap();
	
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
			
			hb.getChildren().addAll(textField, submit);
			hb.setSpacing(10);

			root.setTop(hb);
			
			Scene scene = new Scene(root,800,600);
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
			getPhraseID(textField.getText());
			textDone = true;
		});
		
		textField.setOnMouseClicked(e->{
			if (textDone == true){
				textField.clear();
				textDone =  false;
			}
		});
	}
	
	public void getPhraseID(String text){
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
			image = new Image("file:phrases/"+3+".gif");
		}
		
		iv = new ImageView(image);
		iv.setPreserveRatio(true);
		iv.setFitWidth(300);
		root.setCenter(iv);
		System.out.println("loaded image");
	}


}