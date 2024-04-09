package edu.asu.easydoctor;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

import edu.asu.easydoctor.controllers.SignInController;
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
		WelcomeController.load(App.primaryStage);
		signInTest();
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

	public static void signInTest() {
		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signInButton.fire();
		SignInController signInController = SignInController.getInstance();
		signInController.usernameTextField.setText("itsJohnny21");
		signInController.passwordTextField.setText("meatCuh21!");
		signInController.signInButton.fire();

		if (Database.role == Database.Role.PATIENT && Database.userID == 109) {
			System.out.println("Sign in test passed");
		} else {
			System.out.println("Sign in test failed");
		}
	}
}