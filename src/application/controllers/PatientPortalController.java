package application.controllers;

import java.io.IOException;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PatientPortalController extends Controller {
	
	String title = "Patient Portal";


	@FXML Button contactInfoButton;
	@FXML Button scheduleVisitButton;
	@FXML Button myVisitsButton;
	@FXML Button inboxButton;
	@FXML Button logoutButton;
	@FXML Label usernameLabel;
	@FXML GridPane contentGridPane;
	
	
	@FXML public void handleMenuButtonAction(ActionEvent event) {
		Button button = (Button) event.getSource();
		String resource = String.format("/application/views/%sView.fxml", button.getText());
		App.loadPage(resource);
	}
	
	@FXML public void handleLogoutButtonAction(ActionEvent event) {
		App.logout();
	}
	
	public String getTitle() {
		return this.title;
	}
}
