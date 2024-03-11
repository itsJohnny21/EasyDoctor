package application.controllers;

import java.util.prefs.Preferences;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController extends Controller {
	
	private static Preferences preferences = Preferences.userNodeForPackage(SignInController.class);

	@FXML public TextField usernameTextField;
	@FXML public PasswordField passwordTextField;
	@FXML public Button SignInButton;
	@FXML public Hyperlink forgotUsernamePasswordHyperlink;
	@FXML public CheckBox rememberMeCheckbox;
	@FXML public CheckBox doctorNurseCheckbox;
	
	public void initialize() {
		title = "Sign In";
		usernameTextField.setText(preferences.get("username", ""));
		passwordTextField.setText(preferences.get("password", ""));
		rememberMeCheckbox.setSelected(preferences.getBoolean("rememberMeChecked", false));
		doctorNurseCheckbox.setSelected(preferences.getBoolean("doctorNurseChecked", false));
	}
	
	
	@FXML public void handleSignInButtonAction(ActionEvent event) {
		if (rememberMeCheckbox.isSelected()) {
			rememberMe(true);
		} else {
			rememberMe(false);
		}

        if (doctorNurseCheckbox.isSelected()) {
			App.loadPage("WorkPortalView", stage);
        } else {
        	App.loadPage("PatientPortalView", stage);
       	}
	}
	
	@FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) {
		System.out.println("forgot username or password!");
		App.loadPage(title, stage); //!Fix me

	}
	
	public String getTitle() {
		return this.title;
	}

	public void rememberMe(boolean rememberMe) {
		if (rememberMe) {
			preferences.put("username", usernameTextField.getText());
			preferences.put("password", passwordTextField.getText());
			preferences.putBoolean("rememberMeChecked", rememberMeCheckbox.isSelected());
			preferences.putBoolean("doctorNurseChecked", doctorNurseCheckbox.isSelected());
		} else {
			preferences.remove("username");
			preferences.remove("password");
			preferences.remove("rememberMeChecked");
			preferences.remove("doctorNurseChecked");
		}
	}
	
}
