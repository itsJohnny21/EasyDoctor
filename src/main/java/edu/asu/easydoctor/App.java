package edu.asu.easydoctor;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

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
		WelcomeController.getInstance().load();
		Bypass.toWorkPortalScheduleVisit("barb123");
		// Bypass.toWorkPortal();
		// SignUpController.getInstance().load();
		// Bypass.toWorkPortal();
	}

	public static void quit() throws SQLException, UnknownHostException {
		Database.disconnect();
		System.out.println("Goodbye!");
		System.exit(0);
	}
	public static void main(String[] args) {
		System.out.println("Starting application...");
		try {
			launch(args);
			quit();
		} catch (CommunicationsException e) {
			System.err.println("Lost connection to the database. Please check your network connection and try again.");
		} catch (SQLException e) {
			System.err.println("A database error occurred. Please try again.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//TODO: Add pharmacy to patients table
//TODO: Delete branches: PrimaryStage, DialogController, Polish-MyVists, PatientPortal-Base
//TODO: Combine common functionality between PatientPortal and WorkPortal into a single class called Portal. For example, loading the inbox, sending messages, loading the visits, signing out, and much more!
//TODO: Testing getVisitStatusWorkPortal from Utilities class (edge cases)
//TODO: Add manager as a role
//TODO: WorkPortal.Visits tab should allow the user to filter visits by status, patient, doctor, and date
//TODO: For WorkPortalVisit controller, add a Patient Info button and a Cancel button to cancel the visit
