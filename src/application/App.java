package application;
	
import java.io.IOException;

import application.controllers.Controller;
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
		loadPage("/application/views/LoginView.fxml");
	}
	
	public static void loadPage(String resource) {
        try {
        	
            FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            App.stage.setScene(scene);
            
            Controller controller = loader.getController();
            App.stage.setTitle(controller.getTitle());
            App.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void logout() {
		loadPage("/application/views/LoginView.fxml");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
