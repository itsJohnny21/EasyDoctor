package application.controllers;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class WorkPortalController extends Controller {
	
	@FXML public Button patientsButton;
	@FXML public Button tasksButton;
	@FXML public Button visitsButton;
	@FXML public Button inboxButton;
	@FXML public Button logoutButton;
	@FXML public Label usernameLabel;
	@FXML public GridPane contentGridPane;

	public void initialize() {
		title = "Work Portal";
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
