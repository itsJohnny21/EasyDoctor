package tests;


import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.controllers.ForgotUsernamePasswordController;
import edu.asu.easydoctor.controllers.ResetPasswordController;
import edu.asu.easydoctor.controllers.SignInController;
import edu.asu.easydoctor.controllers.SignUpController;
import edu.asu.easydoctor.controllers.WelcomeController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Test {

	public static void signInTest() throws Exception {
		PreparedStatement statement;

		statement = Database.connection.prepareStatement("SELECT ID FROM users WHERE username = 'signInTest';");
		ResultSet resultSet = statement.executeQuery();

		if (!resultSet.next()) {
			statement = Database.connection.prepareStatement("INSERT INTO users (username, password, role) VALUES ('signInTest', SHA2('passworD1!', 256), 'PATIENT');");
			statement.executeUpdate();
			statement.close();
			statement = Database.connection.prepareStatement("SET @userID = LAST_INSERT_ID();");
			statement.execute();
			statement = Database.connection.prepareStatement("INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity) VALUES (@userID, 'signInTest', 'signInTest',  'OTHER', '2000-01-01', 'signInTest@gmail.com', '1234567890', 'signInTest', 'WHITE', 'HISPANIC');");
			statement.executeUpdate();
			statement.close();
			signInTest();
			return;
		}

		int userID = resultSet.getInt("ID");

		statement.close();
		statement = Database.connection.prepareStatement("UPDATE users SET password = SHA2('passworD1!', 256) WHERE username = 'signInTest';");
		statement.executeUpdate();
		statement.close();

		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signInButton.fire();
		SignInController signInController = SignInController.getInstance();
		signInController.usernameTextField.setText("signInTest");
		signInController.passwordField.setText("passworD1!");
		signInController.signInButton.fire();

		if (Database.userID == userID) {
			System.out.println("Test passed");
		} else {
			System.out.println("Test failed");
		}
		System.exit(0);
	}

	public static void signUpTest() throws SQLException, UnknownHostException, Exception {
		PreparedStatement statement;

		statement = Database.connection.prepareStatement("SELECT ID FROM users WHERE username = 'signUpTest';");
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			statement = Database.connection.prepareStatement("DELETE FROM users WHERE username = 'signUpTest';");
			statement.executeUpdate();
			statement.close();
			signUpTest();
			return;
		}

		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signUpButton.fire();
		SignUpController signUpController = SignUpController.getInstance();
		signUpController.usernameTextField.setText("signUpTest");
		signUpController.roleChoiceBox.setValue(Role.PATIENT.toString());
		signUpController.passwordField.setText("passworD1!");
		signUpController.confirmPasswordField.setText("passworD1!");
		signUpController.nextButton.fire();

		signUpController.firstNameTextField.setText("signUpTest");
		signUpController.lastNameTextField.setText("signUpTest");
		signUpController.emailTextField.setText("signUpTest@gmail.com");
		signUpController.phoneTextField.setText("1234567890");
		signUpController.addressTextField.setText("1234 Test St");
		signUpController.birthDateTextField.setText("2000-01-01");
		signUpController.raceChoiceBox.setValue(Race.ASIAN.toString());
		signUpController.sexChoiceBox.setValue(Sex.MALE.toString());
		signUpController.ethnicityChoiceBox.setValue(Ethnicity.HISPANIC.toString());
		closeShowAndWait();
		signUpController.signUpButton.fire();


		statement.close();
		statement = Database.connection.prepareStatement("SELECT username, role FROM users WHERE username = 'signUpTest';");
		resultSet = statement.executeQuery();

		if (resultSet.next() && resultSet.getString("username").equals("signUpTest") && resultSet.getString("role").equals(Role.PATIENT.toString())) {
			System.out.println("Test passed");
		} else {
			System.out.println("Test failed");
		}
		System.exit(0);
	}

	public static void resetPasswordTest() throws SQLException, UnknownHostException, Exception {
		PreparedStatement statement;

		statement = Database.connection.prepareStatement("SELECT ID FROM users WHERE username = 'resetPasswordTest';");
		ResultSet resultSet = statement.executeQuery();

		if (!resultSet.next()) {
			statement = Database.connection.prepareStatement("INSERT INTO users (username, password, role) VALUES ('resetPasswordTest', 'passworD1!', 'PATIENT');");
			statement.executeUpdate();
			statement.close();
			statement = Database.connection.prepareStatement("SET @userID = LAST_INSERT_ID();");
			statement = Database.connection.prepareStatement("INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity) VALUES (@userID, 'resetPasswordTest', 'resetPasswordTest',  'OTHER', '2000-01-01', 'resetPasswordTest@gmail.com', '1234567890', '123 Test St', 'WHITE', 'HISPANIC');");
			statement.executeUpdate();
			statement.close();
			resetPasswordTest();
			return;
		}

		statement.close();
		statement = Database.connection.prepareStatement("UPDATE users SET password = SHA2('passworD1!', 256) WHERE username = 'resetPasswordTest';");
		statement.executeUpdate();

		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signInButton.fire();
		SignInController signInController = SignInController.getInstance();
		signInController.forgotUsernamePasswordHyperLink.fire();
		ForgotUsernamePasswordController forgotUsernamePasswordController = ForgotUsernamePasswordController.getInstance();
		forgotUsernamePasswordController.roleChoiceBox.setValue(Role.PATIENT.toString());
		forgotUsernamePasswordController.emailTextField.setText("jsalazar6421@gmail.com");
		forgotUsernamePasswordController.sendEmailButton.fire();

		statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens ORDER BY creationTime DESC LIMIT 1");
		resultSet = statement.executeQuery();
		resultSet.next();
		int token = resultSet.getInt("token");

		ResetPasswordController resetPasswordController = ResetPasswordController.getInstance();
		resetPasswordController.resetPasswordTokenTextField.setText(String.valueOf(token));
		resetPasswordController.newPasswordField.setText("passworD2!");
		resetPasswordController.confirmNewPasswordField.setText("passworD2!");
		closeShowAndWait();
		resetPasswordController.resetButton.fire();

		statement = Database.connection.prepareStatement("SELECT password FROM users WHERE password = SHA2('passworD2!', 256);");
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			System.out.println("Test passed");
		} else {
			System.out.println("Test failed");
		}
		System.exit(0);
	}

	public static void closeShowAndWait() {
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Platform.runLater(() -> {
				for (Window window : Window.getWindows()) {
					if (window instanceof Stage) {
						Stage stage = (Stage) window;
						if (stage.getScene().getRoot() instanceof DialogPane) {
							DialogPane dialogPane = (DialogPane) stage.getScene().getRoot();

							ButtonType okButton = dialogPane.getButtonTypes().stream().filter(buttonType -> buttonType.getText().equals("OK")).findFirst().orElse(null);
							if (okButton != null) {
								Node okButtonNode = dialogPane.lookupButton(okButton);
								if (okButtonNode instanceof Button) {
									((Button) okButtonNode).fire();
									return;
								}
							}
						}
					}
				}
			});
		}).start();
	}
}