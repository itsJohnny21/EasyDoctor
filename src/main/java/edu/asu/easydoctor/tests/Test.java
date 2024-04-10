package edu.asu.easydoctor.tests;


import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.controllers.ForgotUsernamePasswordController;
import edu.asu.easydoctor.controllers.ResetPasswordController;
import edu.asu.easydoctor.controllers.SignInController;
import edu.asu.easydoctor.controllers.WelcomeController;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Test {

	public static void signInTest(String username, String password) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.startup(() -> {
            try {
                new App().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();

			try {
				PreparedStatement statement = Database.connection.prepareStatement("SELECT ID FROM users WHERE username = ?");
				statement.setString(1, username);
				ResultSet resultSet = statement.executeQuery();
				resultSet.next();
				int userID = resultSet.getInt("ID");

				PreparedStatement statement2 = Database.connection.prepareStatement("UPDATE users SET password = SHA2(?, 256) WHERE ID = ?");
				statement2.setString(1, password);
				statement2.setInt(2, userID);
				statement2.executeUpdate();
				
				WelcomeController welcomeController = WelcomeController.getInstance();
				welcomeController.signInButton.fire();
				SignInController signInController = SignInController.getInstance();
				signInController.usernameTextField.setText(username);
				signInController.passwordTextField.setText(password);
				signInController.signInButton.fire();
	
				if (Database.userID == userID) {
					System.out.println("Test passed");
				} else {
					System.out.println("Test failed");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		});

        latch.await();
	}

	public static void resetPasswordTest() throws SQLException, UnknownHostException, Exception {
		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signInButton.fire();
		SignInController signInController = SignInController.getInstance();
		signInController.forgotUsernamePasswordHyperLink.fire();
		ForgotUsernamePasswordController forgotUsernamePasswordController = ForgotUsernamePasswordController.getInstance();
		forgotUsernamePasswordController.roleChoiceBox.setValue(Role.PATIENT.toString());
		forgotUsernamePasswordController.emailTextField.setText("jsalazar6421@gmail.com");
		forgotUsernamePasswordController.resetPasswordButton.fire();
		PreparedStatement statement = Database.connection.prepareStatement("SELECT userID, token FROM resetPasswordTokens ORDER BY creationTime DESC LIMIT 1");
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		int userID = resultSet.getInt("userID");
		int token = resultSet.getInt("token");
		System.out.println("Token: " + token);
		Database.role = null;;
		PreparedStatement statement2 = Database.connection.prepareStatement("SELECT password FROM users WHERE ID = ?");
		statement2.setInt(1, userID);
		ResultSet resultSet2 = statement2.executeQuery();
		resultSet2.next();
		String password = resultSet2.getString("password");
		password = password.substring(0, 15) + "123A!";
		System.out.printf("Password: %s\n", password);
		ResetPasswordController resetPasswordController = ResetPasswordController.getInstance();
		resetPasswordController.resetPasswordTokenTextField.setText(String.valueOf(token));
		resetPasswordController.newPasswordField.setText(password);
		resetPasswordController.confirmNewPasswordField.setText(password);
		resetPasswordController.resetButton.fire();
	}

	public static void main(String[] args) {
		try {
			signInTest("itsJohnny21", "meatCuh21!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
