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
		// loadPage("TestView", primaryStage);
		loadPage("SignInView", primaryStage);
	}
	
	public static void loadPage(String filename, Stage primaryStage) throws IOException, Exception {
		String resource = String.format("/application/views/%s.fxml", filename);

		FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
		Parent root = loader.load();
		Controller controller = loader.getController();
		
		Scene scene = new Scene(root);
		controller.setStage(primaryStage);

		primaryStage.setScene(scene);

		primaryStage.setWidth(controller.preferredWindowWidth);
		primaryStage.setHeight(controller.preferredWindowHeight);

		primaryStage.setMinWidth(controller.windowMinWidth);
		primaryStage.setMinHeight(controller.windowMinHeight);

		primaryStage.setMaxWidth(controller.windowMaxWidth);
		primaryStage.setMaxHeight(controller.windowMaxHeight);

		primaryStage.setResizable(controller.resizeable);

		primaryStage.centerOnScreen();
		primaryStage.setTitle(controller.getTitle());
		primaryStage.show();

		System.out.printf("role: %s\n", Database.role);
		System.out.printf("userID: %d\n", Database.userID);
	}

	public static void loadPopup(String filename) throws Exception {
		Stage popupStage = new Stage();
		String resource = String.format("/application/views/%s.fxml", filename);

		FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
		Parent root = loader.load();
		Controller controller = loader.getController();
		
		Scene scene = new Scene(root);
		controller.setStage(popupStage);

		popupStage.setScene(scene);

		popupStage.setWidth(controller.preferredWindowWidth);
		popupStage.setHeight(controller.preferredWindowHeight);

		popupStage.setMinWidth(controller.windowMinWidth);
		popupStage.setMinHeight(controller.windowMinHeight);

		popupStage.setMaxWidth(controller.windowMaxWidth);
		popupStage.setMaxHeight(controller.windowMaxHeight);

		popupStage.setResizable(controller.resizeable);

		popupStage.centerOnScreen();
		popupStage.setTitle(controller.getTitle());
		popupStage.show();
	}


	public static void quit() throws SQLException, UnknownHostException, Exception {
		Database.disconnect();
		System.out.println("Goodbye!");
		System.exit(0);
}
	
	public static void main(String[] args) {
		try {
			launch(args);
			quit();
		} catch (Exception e) {
			System.out.println("An error occurred");
			e.printStackTrace();
		}
	}
}

// TODO: Create a class diagraam
// TODO: Create separate classes instead of nesting them
// TODO: Write functional tests
// TODO: Create CRC cards
// TODO: Create a use case diagram
// TODO: Add buttons to the bottom of the Visits View: Change Date, Add Visit, Cancel Visit
// TODO: Change from prescriptionToolsPane to prescriptionToolPane
// TODO: Use a builder interface for creating new users 
// TODO: Add to database: , emergencyContactName, emergencyContactPhone, emergencyContactRelation) ON patients TO 'doctor';
// TODO: Give vaccineRecords table a fixed number of doses (columns)

