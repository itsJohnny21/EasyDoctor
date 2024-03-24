package application;
	
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import application.Database.Role;
import application.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException, Exception {
		Database.connectAs(Role.NEUTRAL);
		loadPage("TestView", primaryStage);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(1000);
		// loadPage("SignInView", primaryStage);
	}
	
	public static void loadPage(String filename, Stage primaryStage) throws IOException, Exception {
		String resource = String.format("/application/views/%s.fxml", filename);

		FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
		Parent root = loader.load();
		Controller controller = loader.getController();
		
		Scene scene = new Scene(root);
		controller.setStage(primaryStage);
		primaryStage.setTitle(controller.title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void quit() throws SQLException, UnknownHostException, Exception {
		Database.disconnect();
		System.out.println("Goodbye!");
		System.exit(0);
}
	
	public static void main(String[] args) {
		System.out.println("Starting application...");
		try {
			launch(args);
			quit();
		} catch (Exception e) {
			System.out.println("An error occurred");
			e.printStackTrace();
		}
	}
}

// TODO: