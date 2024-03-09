package application.controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PatientPortalController extends Controller {
	
	@FXML public Button contactInfoButton;
	@FXML public Button scheduleVisitButton;
	@FXML public Button myVisitsButton;
	@FXML public Button inboxButton;
	@FXML public Button logoutButton;
	@FXML public Label usernameLabel;
	@FXML public GridPane contentGridPane;

	public void initialize() {
		title = "Patient Portal";
	}
	
	
	@FXML public void handleMenuButtonAction(ActionEvent event) {
		Button button = (Button) event.getSource();
		String resource = String.format("%sView", button.getText());
		App.loadPage(resource, stage);
	}
	
	@FXML public void handleLogoutButtonAction(ActionEvent event) {
		App.logout(stage);
	}
	
	public String getTitle() {
		return this.title;
	}
}
