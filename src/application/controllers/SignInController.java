package application.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import application.App;
import application.Database;
import application.Database.Role;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class SignInController extends Controller {
	
	private static Preferences preferences = Preferences.userNodeForPackage(SignInController.class);

	@FXML public TextField usernameTextField;
	@FXML public PasswordField passwordTextField;
	@FXML public Button signInButton;
	@FXML public Hyperlink forgotUsernamePasswordHyperLink;
	@FXML public CheckBox rememberMeCheckbox;
	@FXML public BorderPane mainPane;

	public void initialize() throws Exception {
		title = "Sign In";
		windowMaxWidth = mainPane.getPrefWidth();
		windowMaxHeight = mainPane.getPrefHeight();

		windowMinWidth = windowMaxWidth;
		windowMinHeight = windowMaxHeight;

		preferredWindowWidth = windowMaxWidth;
		preferredWindowHeight = windowMaxHeight;

		resizeable = false;

		usernameTextField.setText(preferences.get("username", ""));
		passwordTextField.setText(preferences.get("password", ""));
		rememberMeCheckbox.setSelected(preferences.getBoolean("rememberMeChecked", false));

		mainPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				signInButton.fire();
			}
		});

		PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
		pause.setOnFinished(event -> {
			signInButton.fire();
		});
		pause.play();
	}
	
	@FXML public void handleSignInButtonAction(ActionEvent event) throws SQLException, UnknownHostException, IOException, Exception {

		// Check if remember me is checked
		rememberMe(rememberMeCheckbox.isSelected());

		// Check if username is empty
        if (usernameTextField.getText().isBlank()) {
            usernameTextField.requestFocus();
            usernameTextField.setStyle("-fx-border-color: red;");
            return;
        }
        
        // Check if password is empty
        if (passwordTextField.getText().isEmpty()) {
            passwordTextField.requestFocus();
            passwordTextField.setStyle("-fx-border-color: red;");
            return;
        }

		// Attempt to sign in and load the appropriate view. If sign in failed, highlight fields in red
		boolean successful = Database.signIn(usernameTextField.getText(), passwordTextField.getText());


		if (successful) {
			if (Database.role == Role.DOCTOR || Database.role == Role.NURSE) {
				App.loadPage("WorkPortalView", stage);
			} else if (Database.role == Role.PATIENT) {
				App.loadPage("PatientPortalView", stage);
			}

		} else {
			usernameTextField.requestFocus();
			usernameTextField.setStyle("-fx-border-color: red;");
			passwordTextField.setStyle("-fx-border-color: red;");
		}
	}
	
	@FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) throws IOException, Exception {
		App.loadPage("ForgotUsernamePasswordView", stage);
	}
	
	public String getTitle() {
		return this.title;
	}

    @FXML public void handleKeyTyped(KeyEvent event) {
        passwordTextField.setStyle("-fx-border-color: none;");
        usernameTextField.setStyle("-fx-border-color: none;");
    }
    
    @FXML public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signInButton.setStyle("-fx-background-color: #d3d3d3;");
        }
    }
    
    @FXML public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
			signInButton.fire();
        }
    }

	public void rememberMe(boolean rememberMe) {
		if (rememberMe) {
			preferences.put("username", usernameTextField.getText());
			preferences.put("password", passwordTextField.getText());
			preferences.putBoolean("rememberMeChecked", rememberMeCheckbox.isSelected());
		} else {
			preferences.remove("username");
			preferences.remove("password");
			preferences.remove("rememberMeChecked");
			preferences.remove("doctorNurseChecked");
		}
	}
}
