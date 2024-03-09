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

public class LoginController extends Controller {
	
	private static Preferences preferences = Preferences.userNodeForPackage(LoginController.class);

	@FXML public TextField usernameTextField;
	@FXML public PasswordField passwordTextField;
	@FXML public Button loginButton;
	@FXML public Hyperlink forgotUsernamePasswordHyperlink;
	@FXML public CheckBox rememberMeCheckbox;
	@FXML public CheckBox doctorNurseCheckbox;
	
	public void initialize() {
		title = "Login";
		usernameTextField.setText(preferences.get("username", ""));
		passwordTextField.setText(preferences.get("password", ""));
		rememberMeCheckbox.setSelected(preferences.getBoolean("rememberMeChecked", false));
		doctorNurseCheckbox.setSelected(preferences.getBoolean("doctorNurseChecked", false));
	}
	
	
	@FXML public void handleLoginButtonAction(ActionEvent event) {
		if (rememberMeCheckbox.isSelected()) {
			rememberMe();
		} else {
			forgetMe();
		}

		System.out.println(stage.toString());

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

	public void rememberMe() {
		preferences.put("username", usernameTextField.getText());
		preferences.put("password", passwordTextField.getText());
		preferences.putBoolean("rememberMeChecked", rememberMeCheckbox.isSelected());
		preferences.putBoolean("doctorNurseChecked", doctorNurseCheckbox.isSelected());
	}

	public void forgetMe() {
		preferences.remove("username");
		preferences.remove("password");
		preferences.remove("rememberMeChecked");
		preferences.remove("doctorNurseChecked");
	}
	
}
