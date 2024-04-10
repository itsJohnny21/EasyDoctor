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
		WelcomeController.getInstance().load(primaryStage);
	}

	public static void quit() throws SQLException, UnknownHostException, Exception {
		Database.disconnect();
		System.out.println("Goodbye!");
		System.exit(0);
	}
	public static void main(String[] args) {
		System.out.println("This is for my commit for Phase III documentation");
		System.out.println("Starting application...");
		try {
			launch(args);
			quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
