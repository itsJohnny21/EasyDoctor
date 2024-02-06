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

public class WorkPortalLoginController {
	
	@FXML TextField usernameTextField;
	@FXML PasswordField passwordTextField;
	@FXML Button loginButton;
	@FXML Button goBackButton;
	@FXML CheckBox rememberMeCheckbox;
	@FXML Hyperlink forgotUsernamePasswordHyperlink;
	
	@FXML public void handleGoBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/StartView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            App.stage.setScene(scene);

            App.stage.setTitle("Start");
            App.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML public void handleLoginButtonAction(ActionEvent event) {
        try {
        	usernameTextField.setStyle("-fx-border-color: #42f563;");
        	passwordTextField.setStyle("-fx-border-color: #42f563;");
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/WorkPortalView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            App.stage.setScene(scene);
            
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            App.stage.setX((screenBounds.getWidth() - 1280) / 2);
            App.stage.setY((screenBounds.getHeight() - 720) / 2);

            App.stage.setTitle("Work Portal");
            App.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) {
		System.out.print("forgot username or password!");

	}
	
	@FXML public void handleRememberMeButtonAction(ActionEvent event) {
		System.out.print("remember me!");
	}

}
