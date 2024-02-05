package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import application.controllers.LoginController;


public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginView.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.setTitle("Logsfsdfin");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
		

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
