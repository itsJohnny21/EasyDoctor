package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;



public class App extends Application {
	
	 public static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/StartView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            stage.setTitle("Who Am I");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
