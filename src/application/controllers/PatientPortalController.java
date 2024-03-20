package application.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import application.App;
import application.Database;
import application.FormBuilder.Form;
import application.TableBuilder;
import application.TableBuilder.Row;
import application.TableBuilder.Table;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.util.Duration;

public class PatientPortalController extends Controller {

	@FXML public AnchorPane mainPane;
	@FXML public GridPane tabsPane;
	public AnchorPane currentTab;
	public Button currentButton;
	
	@FXML public AnchorPane myVisitsPane;
	@FXML public AnchorPane myInformationPane;
	@FXML public AnchorPane scheduleVisitPane;
	@FXML public AnchorPane inboxPane;
	@FXML public AnchorPane myPillsPane;
	@FXML public AnchorPane usernamePane;

	@FXML public Button myVisitsButton;
	@FXML public Button myInformationButton;
	@FXML public Button scheduleVisitButton;
	@FXML public Button inboxButton;
	@FXML public Button myPillsButton;
	@FXML public Button usernameButton;
	@FXML public Button signOutButton;

	@FXML public ScrollPane myVisitsScrollPane;
	@FXML public ScrollPane myInformationScrollPane;
	@FXML public ScrollPane scheduleVisitScrollPane;
	@FXML public ScrollPane inboxScrollPane;
	@FXML public ScrollPane myPillsScrollPane;
	@FXML public ScrollPane usernameScrollPane;
	@FXML public ScrollPane signOutScrollPane;

	public double rowWidth;
	public double rowHeight;

	public static Integer currentVisitID;

	public void initialize() throws Exception {
		System.out.println("PatientPortalController");
		title = "Patient Portal";
		windowMaxWidth = Screen.getPrimary().getVisualBounds().getWidth();
		windowMaxHeight = Screen.getPrimary().getVisualBounds().getHeight();

		windowMinWidth = 1300;
		windowMinHeight = 800;

		rowWidth = 1512;
		rowHeight = 50;

		preferredWindowWidth = windowMaxWidth ;
		preferredWindowHeight = windowMaxHeight;

		resizeable = true;
		
		currentTab = myVisitsPane;
		currentTab.setVisible(true);

		currentButton = myVisitsButton;
		currentButton.setStyle("-fx-background-color: #ff6666");

		usernameButton.setText(Database.getMy("username"));

		mainPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.S && event.isAltDown()) {
				signOutButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT1) {
				myVisitsButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT2) {
				myInformationButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT3) {
				scheduleVisitButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT4) {
				inboxButton.fire();
			} else if (event.getCode() == KeyCode.DIGIT5) {
				myPillsButton.fire();
			}
		});

		for (AnchorPane tab : new AnchorPane[] {myVisitsPane, myInformationPane, scheduleVisitPane, inboxPane, myPillsPane}) {
			tab.setVisible(false);
			tab.setDisable(true);
		}

		myVisitsButton.fire();
	}
	
	@FXML public void handleSignOutButtonAction(ActionEvent event) throws IOException, SQLException, UnknownHostException, Exception {
		Database.signOut();
		App.loadPage("SignInView", stage);
	}

	@FXML public void handleOnMousePressed(MouseEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();

		button.setStyle("-fx-background-color: #ff6666");
		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
		ft.setFromValue(button.getOpacity());
		ft.setToValue(1);
		ft.setCycleCount(1);
		ft.play();
	}
	
	@FXML public void handleOnMouseReleased(MouseEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();

		if (button != currentButton) {
			button.setStyle("-fx-background-color: #cc7878");
		}
		
		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
		ft.setFromValue(button.getOpacity());
		ft.setToValue(0.7);
		ft.setCycleCount(1);
		ft.play();
	}

	@FXML public void handleOnMouseEntered(MouseEvent event) {
		Button button = (Button) event.getSource();

		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
		ft.setFromValue(button.getOpacity());
		ft.setToValue(0.7);
		ft.setCycleCount(1);
		ft.play();
	}

	@FXML public void handleOnMouseExited(MouseEvent event) {
		Button button = (Button) event.getSource();

		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
		ft.setFromValue(button.getOpacity());
		ft.setToValue(0.5);
		ft.setCycleCount(1);
		ft.play();
	}

	@FXML public void handleOnMouseClickedVisits(MouseEvent event) throws IOException, Exception { //! DELETE ME
		// App.loadPopup("PatientPortalVisitPopupView");
	}

	@FXML public void handleOnMouseEnteredVisits(MouseEvent event) { //! DELETE ME
		GridPane gridPane = (GridPane) event.getSource();
		gridPane.setStyle("-fx-background-color: #ff6666");
	}	

	@FXML public void handleOnMouseExitedVisits(MouseEvent event) { //! DELETE ME
		GridPane gridPane = (GridPane) event.getSource();
		gridPane.setStyle("-fx-background-color: #ab5b67");
	}

	@FXML public void handleMyInformationButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(myInformationPane, button);

		GridPane contentPane = new GridPane();
		contentPane.setPrefWidth(rowWidth);
		contentPane.setAlignment(Pos.CENTER);

		loadMyContactInformation(contentPane);
		loadMyMedicalInformation(contentPane);
		loadMyHealthConditions(contentPane);
		loadMyVaccineRecord(contentPane);
		loadMyAllergiesTable(contentPane);
		loadMySurgeries(contentPane);

		myInformationScrollPane.setContent(contentPane);
	}

	@FXML public void handleMyVisitsButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(myVisitsPane, button);

		GridPane contentPane = new GridPane();
		contentPane.setPrefWidth(rowWidth);
		contentPane.setAlignment(Pos.CENTER);

		loadMyVisits(contentPane);

		myVisitsScrollPane.setContent(contentPane);
	}

	@FXML public void handleScheduleVisitButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(scheduleVisitPane, button);
	}

	@FXML public void handleScheduleVisitButtonAction2(ActionEvent event) throws IOException, Exception { //! RENAME ME
		scheduleVisitButton.fire();
	}

	@FXML public void handleInboxButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(inboxPane, button);
	}

	@FXML public void handleMyPillsButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(myPillsPane, button);
	}
	
	@FXML public void handleUsernameButtonAction(ActionEvent event) throws IOException, Exception {
		Button button = (Button) event.getSource();
		setCurrentTab(inboxPane, button);
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setCurrentTab(AnchorPane pane, Button button) {
		currentTab.setVisible(false);
		currentTab.setDisable(true);
		currentTab = pane;
		currentTab.setVisible(true);
		currentTab.setDisable(false);

		currentButton.setStyle("-fx-background-color: #cc7878");
		currentButton = button;
		currentButton.setStyle("-fx-background-color: #ff6666");
	}

	public void loadContactInformationFor(int patientID, GridPane content) throws Exception {
		Form contactInformationForm = TableBuilder.generateContactInformationFormFor(patientID, "Contact Information", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		contactInformationForm.generate();
		content.add(contactInformationForm, 0, content.getRowCount());
	}

	public void loadMyContactInformation(GridPane content) throws Exception {
		loadContactInformationFor(Database.userID, content);
	}

	public void loadMedicalInformationFor(int patientID, GridPane content) throws Exception {
		Form medicalInformationForm = TableBuilder.generateMedicalInformationFormFor(patientID, "Medical Information", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		medicalInformationForm.generate();
		content.add(medicalInformationForm, 0, content.getRowCount());
	}

	public void loadMyMedicalInformation(GridPane content) throws Exception {
		loadMedicalInformationFor(Database.userID, content);
	}

	public void loadHealthConditionsFor(int patientID, GridPane content) throws Exception {
		Table healthConditionsTable = TableBuilder.generateHealthConditionsTableFor(patientID, "Health Conditions", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		healthConditionsTable.generate();
		content.add(healthConditionsTable, 0, content.getRowCount());
	}

	public void loadMyHealthConditions(GridPane content) throws Exception {
		loadHealthConditionsFor(Database.userID, content);
	}
	
	public void loadVaccineRecordFor(int patientID, GridPane content) throws Exception {
		Table vaccineRecordTable = TableBuilder.generateVaccineRecordFor(patientID, "Vaccine Record", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		vaccineRecordTable.generate();
		content.add(vaccineRecordTable, 0, content.getRowCount());
	}

	public void loadMyVaccineRecord(GridPane content) throws Exception {
		loadVaccineRecordFor(Database.userID, content);
	}

	public void loadAllergiesFor(int patientID, GridPane content) throws Exception {
		Table allergiesTable = TableBuilder.generateAllergiesTableFor(patientID, "Allergies", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		allergiesTable.generate();
		content.add(allergiesTable, 0, content.getRowCount());
	}

	public void loadMyAllergiesTable(GridPane content) throws Exception {
		loadAllergiesFor(Database.userID, content);
	}

	public void loadSurgeriesFor(int patientID, GridPane content) throws Exception {
		Table surgeriesTable = TableBuilder.generateSurgeriesTableFor(patientID, "Surgeries", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		surgeriesTable.generate();
		content.add(surgeriesTable, 0, content.getRowCount());
	}

	public void loadMySurgeries(GridPane content) throws Exception {
		loadSurgeriesFor(Database.userID, content);
	}

	public void loadVisitsFor(int patientID, GridPane content) throws Exception {
		Table visitsTable = TableBuilder.generateVisitsTableFor(patientID, "Visits", rowWidth, rowHeight, "-fx-background-color: #cc7878", "");
		visitsTable.displayTableNameOption = false;
		visitsTable.displayHeaderOption = false;
		
		visitsTable.selectAction = (Row visitButton) -> {
			try {
				visitButton.setStyle("-fx-background-color: #cc7878");
				int visitID = Integer.parseInt(visitButton.originalValues[visitButton.originalValues.length - 1]);
				currentVisitID = visitID;
				App.loadPopup("MyVisitView");

			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		visitsTable.onMouseEntered = (Row visitButton) -> {
			visitButton.setStyle("-fx-background-color: #cc7878");
		};

		visitsTable.onMouseExited = (Row visitButton) -> {
			visitButton.setStyle("");
		};
		
		visitsTable.generate();
		content.add(visitsTable, 0, content.getRowCount());
	}

	public void loadMyVisits(GridPane content) throws Exception {
		loadVisitsFor(Database.userID, content);
	}
}
