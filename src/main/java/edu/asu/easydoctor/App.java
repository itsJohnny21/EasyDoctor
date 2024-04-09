package edu.asu.easydoctor;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

import edu.asu.easydoctor.controllers.Controller;
import edu.asu.easydoctor.controllers.DialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public static Properties properties;

	static {
		properties = new Properties();

		try {
			FileInputStream in = new FileInputStream(".env");
			properties.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("Error reading .env file");
		}
	}

	@Override
	public void start(Stage primaryStage) throws IOException, SQLException, UnknownHostException {
		Database.connect();
		loadPage("ForgotUsernamePasswordView", primaryStage);
	}

	public static void loadDialog(String filename, Stage stage, Controller controller) throws IOException {
		String resource = String.format("views/%s.fxml", filename);
		FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));

		Parent root = loader.load();
		DialogController dialogController = loader.getController();
		dialogController.setParentController(controller);

		Scene scene = new Scene(root);
		Stage secondaryStage = new Stage();
		dialogController.setStage(secondaryStage);
		secondaryStage.setTitle(dialogController.getTitle());
		secondaryStage.setScene(scene);
		secondaryStage.setResizable(dialogController.resizable);
		secondaryStage.centerOnScreen();
		secondaryStage.setWidth(dialogController.width);
		secondaryStage.setHeight(dialogController.height);
		secondaryStage.setMaxWidth(dialogController.width);
		secondaryStage.setMaxHeight(dialogController.height);
		secondaryStage.initOwner(stage);
		secondaryStage.showAndWait();
	}
	
	public static void loadPage(String filename, Stage primaryStage) throws IOException {
		String resource = String.format("views/%s.fxml", filename);

		FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
		Parent root = loader.load();
		Controller controller = loader.getController();

		Scene scene = new Scene(root);
		controller.setStage(primaryStage);
		primaryStage.setTitle(controller.getTitle());
		primaryStage.setScene(scene);
		primaryStage.setResizable(controller.resizable);
		primaryStage.centerOnScreen();
		primaryStage.setWidth(controller.width);
		primaryStage.setHeight(controller.height);
		primaryStage.setMaxWidth(controller.width);
		primaryStage.setMaxHeight(controller.height);
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

// TODO: Avoid using String.format() when querying the database
// TODO: When edit is clicked, a RadioButton should appear next to each row, and the user should be able to select multiple rows to delete
// TODO: Test insertng and deleting visits as a patient and doctor