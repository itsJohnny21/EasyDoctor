package application;
	
import java.io.IOException;

import application.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class App extends Application {
		
	@Override
	public void start(Stage primaryStage) {
		loadPage("LoginView", primaryStage);
	}
	
	public static void loadPage(String filename, Stage primaryStage) {
		String resource = String.format("/application/views/%s.fxml", filename);
        try {
        	
            FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
            Parent root = loader.load();
            Controller controller = loader.getController();
            
            Scene scene = new Scene(root);
			controller.setStage(primaryStage);
			primaryStage.setScene(scene);

            
            primaryStage.setTitle(controller.getTitle());
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void logout(Stage primaryStage) {
		loadPage("LoginView", primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
