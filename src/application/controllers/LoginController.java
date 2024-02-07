package application.controllers;

import java.io.IOException;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;

public class LoginController extends Controller {
	
	String title = "Login";
	static String username;
	static String password;
	static boolean rememberMeChecked;
	static boolean nurseDoctorChecked;
	
	@FXML TextField usernameTextField;
	@FXML PasswordField passwordTextField;
	@FXML Button loginButton;
	@FXML Hyperlink forgotUsernamePasswordHyperlink;
	@FXML CheckBox rememberMeCheckbox;
	@FXML CheckBox doctorNurseCheckbox;
	
	public void initialize() {
		usernameTextField.setText(username);
		passwordTextField.setText(username);
		rememberMeCheckbox.setSelected(rememberMeChecked);
		doctorNurseCheckbox.setSelected(nurseDoctorChecked);
	}
	
	
	@FXML public void handleLoginButtonAction(ActionEvent event) {
        	
        if (rememberMeCheckbox.isSelected()) {
        	rememberFields();
        } else {
        	resetFields();
        }
        	
        if (doctorNurseCheckbox.isSelected()) {
			App.loadPage("/application/views/WorkPortalView.fxml");
        } else {
        	App.loadPage("/application/views/PatientPortalView.fxml");
       	}
	}
	
	
	@FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) {
		System.out.println("forgot username or password!");

	}

	@FXML public void handleRememberMeButtonAction(ActionEvent event) {
		System.out.println("remember me!");
	}
	
	@FXML public void handleDoctorNurseCheckboxAction(ActionEvent event) {
		System.out.println("i am a doctor or nurse!");
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void rememberFields() {
    	username = usernameTextField.getText();
    	password = passwordTextField.getText();
    	rememberMeChecked = true;
    	nurseDoctorChecked = doctorNurseCheckbox.isSelected();
	}
	
	public void resetFields() {
		username = "";
    	password = "";
    	rememberMeChecked = false;
	}

}
