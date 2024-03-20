package application.controllers;

import application.App;
import application.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class WorkPortalController extends Controller {
	
	@FXML public Label usernameLabel;
	@FXML public Button visitsButton;
	@FXML public Button patientRecordsButton;
	@FXML public Button activeSessionsButton;
	@FXML public Button inboxButton;
	@FXML public Button prescriptionToolsButton;
	@FXML public Button signOutButton;
	@FXML public AnchorPane mainPane;
	@FXML public GridPane tabsPane;
	@FXML public AnchorPane visitsPane;
	@FXML public AnchorPane patientRecordsPane;
	@FXML public AnchorPane activeSessionsPane;
	@FXML public AnchorPane inboxPane;
	@FXML public AnchorPane prescriptionToolsPane;

	public Pane currentPane;
	
	public void initialize() throws Exception {
		title = "Work Portal";
		windowMaxWidth = Screen.getPrimary().getVisualBounds().getWidth();
		windowMaxHeight = Screen.getPrimary().getVisualBounds().getHeight();

		windowMinWidth = 1300;
		windowMinHeight = 800;

		preferredWindowWidth = windowMaxWidth ;
		preferredWindowHeight = windowMaxWidth;

		resizeable = true;

		currentPane = visitsPane;
		currentPane.setVisible(true);

		usernameLabel.setText(Database.getEmployeeInfo(Database.userID).getString("username"));

		mainPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.S && event.isAltDown()) {
				signOutButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT1) {
				visitsButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT2) {
				patientRecordsButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT3) {
				activeSessionsButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT4) {
				inboxButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT5) {
				prescriptionToolsButton.fire();
			}
		});
	}

	@FXML public void handleTabButtonAction(ActionEvent event) throws Exception{
		Button button = (Button) event.getSource();

		if (button == visitsButton) {
			setCurrentPane(visitsPane);
		} else if (button == patientRecordsButton) {
			setCurrentPane(patientRecordsPane);
		} else if (button == activeSessionsButton) {
			setCurrentPane(activeSessionsPane);
		} else if (button == inboxButton) {
			setCurrentPane(inboxPane);
		} else if (button == prescriptionToolsButton) {
			setCurrentPane(prescriptionToolsPane);
		}
	}
	
	@FXML public void handleSignOutButtonAction(ActionEvent event) throws Exception{
		String username = Database.getEmployeeInfo(Database.userID).getString("username");
		System.out.println(username + " signed out");
		Database.signOut();
		App.loadPage("SignInView", stage);
	}

	public String getTitle() {
		return this.title;
	}

	public void setCurrentPane(Pane pane) {
		currentPane.setVisible(false);
		currentPane = pane;
		currentPane.setVisible(true);
	}
}
