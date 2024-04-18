package edu.asu.easydoctor;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

import edu.asu.easydoctor.controllers.WelcomeController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	public static Properties properties;
	public static Stage primaryStage;

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
	public void start(Stage primaryStage) throws IOException, SQLException, UnknownHostException, Exception {
		App.primaryStage = primaryStage;

		primaryStage.setOnCloseRequest(event -> {
            try {
				quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
        });


		WelcomeController.getInstance().load();
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
			e.printStackTrace();
		}
	}
}

//TODO: Custom dialogs need some major bug fixes and redesign
//TODO: Fix visits table so that it has a date field and time field separately and make sure rows are unique for date and userID
//TODO: Add pharmacy to patients table
//TODO: Fix bug when trying to sign in and the show toggle button is toggled on. Update ShowPasswordGroup to handle this (pass it a button)
//TODO: Delete branches: PrimaryStage, DialogController, Polish-MyVists, PatientPortal-Base
//TODO: Fix this warning: WARNING: Loading FXML document with JavaFX API of version 21 by JavaFX runtime of version 18-ea
//TODO: The visits table must not have a default for time and date, or should it?...
//TODO: Read field from conversations table is not really needed. Maybe delete?