// package application.controllers;

// import java.io.IOException;
// import java.net.UnknownHostException;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.util.HashMap;

// import application.App;
// import application.Database;
// import javafx.animation.FadeTransition;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.geometry.HPos;
// import javafx.geometry.Pos;
// import javafx.geometry.VPos;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextField;
// import javafx.scene.input.KeyCode;
// import javafx.scene.input.MouseEvent;
// import javafx.scene.layout.AnchorPane;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.Pane;
// import javafx.scene.layout.Priority;
// import javafx.scene.text.Font;
// import javafx.stage.Screen;
// import javafx.util.Duration;

// public class PatientPortalController extends Controller {

// 	@FXML public AnchorPane mainPane;
// 	@FXML public GridPane tabsPane;
// 	public Pane currentTab;
// 	public Button currentButton;
	
// 	@FXML public AnchorPane myVisitsPane;
// 	@FXML public AnchorPane myInformationPane;
// 	@FXML public AnchorPane scheduleVisitPane;
// 	@FXML public AnchorPane inboxPane;
// 	@FXML public AnchorPane myPillsPane;
// 	@FXML public AnchorPane usernamePane;

// 	@FXML public Button myVisitsButton;
// 	@FXML public Button myInformationButton;
// 	@FXML public Button scheduleVisitButton;
// 	@FXML public Button inboxButton;
// 	@FXML public Button myPillsButton;
// 	@FXML public Button usernameButton;
// 	@FXML public Button signOutButton;

// 	@FXML public GridPane informationGridPane;
// 	@FXML public ScrollPane myInformationScrollPane;

// 	public HashMap<Button, Pane> buttonToPane = new HashMap<Button, Pane>();
	
// 	public void initialize() throws Exception {
// 		System.out.println("PatientPortalController");
// 		title = "Patient Portal";
// 		windowMaxWidth = Screen.getPrimary().getVisualBounds().getWidth();
// 		windowMaxHeight = Screen.getPrimary().getVisualBounds().getHeight();

// 		windowMinWidth = 1300;
// 		windowMinHeight = 800;

// 		preferredWindowWidth = windowMaxWidth ;
// 		preferredWindowHeight = windowMaxHeight;

// 		resizeable = true;
		
// 		currentTab = myVisitsPane;
// 		currentTab.setVisible(true);

// 		currentButton = myVisitsButton;
// 		currentButton.setStyle("-fx-background-color: #ff6666");

// 		usernameButton.setText(Database.getPatientInfo(Database.userID).getString("firstName"));

// 		mainPane.setOnKeyPressed(event -> {
// 			if (event.getCode() == KeyCode.S && event.isAltDown()) {
// 				signOutButton.fire();
// 			} else if (event.getCode() == KeyCode.DIGIT1) {
// 				myVisitsButton.fire();
// 			} else if (event.getCode() == KeyCode.DIGIT2) {
// 				myInformationButton.fire();
// 			} else if (event.getCode() == KeyCode.DIGIT3) {
// 				scheduleVisitButton.fire();
// 			} else if (event.getCode() == KeyCode.DIGIT4) {
// 				inboxButton.fire();
// 			} else if (event.getCode() == KeyCode.DIGIT5) {
// 				myPillsButton.fire();
// 			}
// 		});

// 		buttonToPane.put(myVisitsButton, myVisitsPane); //! REMOVE
// 		buttonToPane.put(myInformationButton, myInformationPane);
// 		buttonToPane.put(scheduleVisitButton, scheduleVisitPane);
// 		buttonToPane.put(inboxButton, inboxPane);
// 		buttonToPane.put(myPillsButton, myPillsPane);
// 		buttonToPane.put(usernameButton, myInformationPane);

// 		//! REMOVE
// 		informationGridPane.getChildren().removeIf(node -> {
// 			if (node instanceof GridPane) {
// 				GridPane row = (GridPane) node;
// 				// System.out.println(row.styleProperty().getValue());
// 				return true;
// 			}
// 			return false;
// 		});
// 		informationGridPane = new GridPane();
// 		myInformationScrollPane.setContent(informationGridPane);
// 		informationGridPane.setGridLinesVisible(true);
		
// 		Label contactInformationLabel = new Label("Contact Information");
// 		contactInformationLabel.setFont(Font.font("Arial", 25));
// 		contactInformationLabel.setPrefHeight(50);
// 		GridPane.setHalignment(contactInformationLabel, HPos.CENTER);
// 		GridPane.setValignment(contactInformationLabel, VPos.CENTER);
		
// 		informationGridPane.add(contactInformationLabel, 0, 0);

// 		GridPane myInformationRow = new GridPane();
// 		myInformationRow.setGridLinesVisible(true);
// 		myInformationRow.setPrefWidth(1512);
// 		myInformationRow.setPrefHeight(50);
// 		GridPane.setHgrow(myInformationRow, Priority.ALWAYS);
// 		GridPane.setFillWidth(myInformationRow, true);

// 		Label cl = new Label("First Name: ");
// 		cl.setFont(Font.font("Arial", 21));
// 		cl.setAlignment(Pos.CENTER_RIGHT);
// 		cl.setPrefWidth(1512 / 5);
		
// 		TextField vl = new TextField("John");
// 		vl.setFont(Font.font("Arial", 21));
// 		vl.setAlignment(Pos.CENTER_LEFT);
// 		vl.setPrefWidth(1512 / 5);

// 		Label cl2 = new Label("First Name: ");
// 		cl2.setFont(Font.font("Arial", 21));
// 		cl2.setAlignment(Pos.CENTER_RIGHT);
// 		cl2.setPrefWidth(1512 / 5);
		
// 		TextField vl2 = new TextField("John");
// 		vl2.setFont(Font.font("Arial", 21));
// 		vl2.setAlignment(Pos.CENTER_LEFT);
// 		vl2.setPrefWidth(1512 / 5);

// 		myInformationRow.add(cl, 0, 0);
// 		myInformationRow.add(vl, 1, 0);
// 		myInformationRow.add(cl2, 2, 0);
// 		myInformationRow.add(vl2, 3, 0);

// 		informationGridPane.add(myInformationRow, 0, 1);

// 		GridPane myInformationRow2 = new GridPane();
// 		myInformationRow2.setGridLinesVisible(true);
// 		myInformationRow2.setPrefWidth(1512);
// 		myInformationRow2.setPrefHeight(50);
// 		GridPane.setHgrow(myInformationRow2, Priority.ALWAYS);
// 		GridPane.setFillWidth(myInformationRow2, true);

// 		Label cl3 = new Label("Emergency Contact: ");
// 		cl3.setFont(Font.font("Arial", 21));
// 		cl3.setAlignment(Pos.CENTER_RIGHT);
// 		cl3.setPrefWidth(1512 / 5);
		
// 		TextField vl3 = new TextField("I don't know");
// 		vl3.setFont(Font.font("Arial", 21));
// 		vl3.setAlignment(Pos.CENTER_LEFT);
// 		vl3.setPrefWidth(1512 / 5);

// 		Label cl4 = new Label("Thi scolumn name is way too bigggggg jdsofj: ");
// 		cl4.setFont(Font.font("Arial", 21));
// 		cl4.setAlignment(Pos.CENTER_RIGHT);
// 		cl4.setPrefWidth(1512 / 5);
		
// 		TextField vl4 = new TextField("Eloisa");
// 		vl4.setFont(Font.font("Arial", 21));
// 		vl4.setAlignment(Pos.CENTER_LEFT);
// 		vl4.setPrefWidth(1512 / 5);

// 		myInformationRow2.add(cl3, 0, 0);
// 		myInformationRow2.add(vl3, 1, 0);
// 		myInformationRow2.add(cl4, 2, 0);
// 		myInformationRow2.add(vl4, 3, 0);

// 		informationGridPane.add(myInformationRow2, 0, 2);

// 		// HBox myInformationRow = new HBox();
// 		// // myInformationRow.setGridLinesVisible(true);
// 		// myInformationRow.setPrefWidth(1512);
// 		// myInformationRow.setPrefHeight(50);
// 		// myInformationRow.setAlignment(Pos.CENTER);

// 		// Label cl = new Label("First Name: ");
// 		// cl.setFont(Font.font("Arial", 21));
// 		// // cl.setPrefWidth(1512 / 5);
// 		// cl.setAlignment(Pos.CENTER_RIGHT);
		
// 		// TextField vl = new TextField("John");
// 		// vl.setFont(Font.font("Arial", 21));
// 		// // vl.setPrefWidth(1512 / 5);
// 		// vl.setAlignment(Pos.CENTER_LEFT);
		
// 		// Label cl2 = new Label("Last Name: ");
// 		// cl2.setFont(Font.font("Arial", 21));
// 		// // cl2.setPrefWidth(1512 / 5);
// 		// cl2.setAlignment(Pos.CENTER_RIGHT);
// 		// HBox.setMargin(cl2, new Insets(0, 0, 0, 100));
		
// 		// TextField vl2 = new TextField("Doe");
// 		// vl2.setFont(Font.font("Arial", 21));
// 		// // vl2.setPrefWidth(1512 / 5);
// 		// vl2.setAlignment(Pos.CENTER_LEFT);
		
// 		// myInformationRow.getChildren().add(cl);
// 		// myInformationRow.getChildren().add(vl);
// 		// myInformationRow.getChildren().add(cl2);
// 		// myInformationRow.getChildren().add(vl2);

// 		// informationGridPane.add(myInformationRow, 0, 1);
		
// 		// HBox myInformationRow2 = new HBox();
// 		// // myInformationRow2.setGridLinesVisible(true);
// 		// myInformationRow2.setPrefWidth(1512);
// 		// myInformationRow2.setPrefHeight(50);
// 		// myInformationRow2.setAlignment(Pos.CENTER);

// 		// Label cl3 = new Label("Emergency Contact: ");
// 		// cl3.setFont(Font.font("Arial", 21));
// 		// // cl3.setPrefWidth(1512 / 5);
// 		// cl3.setAlignment(Pos.CENTER_RIGHT);
		
// 		// TextField vl3 = new TextField("None");
// 		// vl3.setFont(Font.font("Arial", 21));
// 		// // vl3.setPrefWidth(1512 / 5);
// 		// vl3.setAlignment(Pos.CENTER_LEFT);
		
// 		// Label cl4 = new Label("Mother's First Name: ");
// 		// cl4.setFont(Font.font("Arial", 21));
// 		// // cl4.setPrefWidth(1512 / 5);
// 		// cl4.setAlignment(Pos.CENTER_RIGHT);
// 		// HBox.setMargin(cl4, new Insets(0, 0, 0, 100));
		
// 		// TextField vl4 = new TextField("Eloisa");
// 		// vl4.setFont(Font.font("Arial", 21));
// 		// // vl4.setPrefWidth(1512 / 5);
// 		// vl4.setAlignment(Pos.CENTER_LEFT);
		
// 		// myInformationRow2.getChildren().add(cl3);
// 		// myInformationRow2.getChildren().add(vl3);
// 		// myInformationRow2.getChildren().add(cl4);
// 		// myInformationRow2.getChildren().add(vl4);

// 		// informationGridPane.add(myInformationRow2, 0, 2);




// 		ResultSet resultSet = Database.getPatientInfo(Database.userID);
// 		ResultSetMetaData metaData = resultSet.getMetaData();

// 		for (int i = 999; i <= metaData.getColumnCount(); i++) {
// 			GridPane row = new GridPane();
// 			row.setGridLinesVisible(true);
// 			row.setPrefWidth(1512);
// 			row.setPrefHeight(50);
// 			row.setAlignment(Pos.CENTER);

// 			Label columnLabel = new Label(String.format("%s: ", metaData.getColumnName(i)));
// 			columnLabel.setFont(Font.font("Arial", 21));
// 			GridPane.setHalignment(columnLabel, HPos.RIGHT);
// 			columnLabel.setPrefWidth(informationGridPane.getPrefWidth() / 4);
			
// 			TextField valueField = new TextField(resultSet.getString(i));
// 			valueField.setFont(Font.font("Arial", 21));
// 			GridPane.setHalignment(valueField, HPos.LEFT);
// 			valueField.setPrefWidth(informationGridPane.getPrefWidth() / 4);
			
// 			Label columnLabel2 = new Label(String.format("%s: ", metaData.getColumnName(i)));
// 			columnLabel2.setFont(Font.font("Arial", 21));
// 			GridPane.setHalignment(columnLabel2, HPos.RIGHT);
// 			columnLabel2.setPrefWidth(informationGridPane.getPrefWidth() / 4);
			
// 			TextField valueField2 = new TextField(resultSet.getString(i));
// 			valueField2.setFont(Font.font("Arial", 21));
// 			GridPane.setHalignment(valueField, HPos.LEFT);
// 			valueField2.setPrefWidth(informationGridPane.getPrefWidth() / 4);

// 			row.add(columnLabel, 0, 0);
// 			row.add(valueField, 1, 0);
// 			row.add(columnLabel2, 2, 0);
// 			row.add(valueField2, 3, 0);

// 			informationGridPane.add(row, 0, i);
// 		}
// 	}
	
// 	@FXML public void handleTabButtonAction(ActionEvent event) throws IOException, Exception {
// 		Button button = (Button) event.getSource();

// 		currentTab.setVisible(false);
// 		currentTab = buttonToPane.get(button);
// 		currentTab.setVisible(true);

// 		currentButton.setStyle("-fx-background-color: #cc7878");
// 		currentButton = button;
// 		currentButton.setStyle("-fx-background-color: #ff6666");
// 	}
	
// 	@FXML public void handleSignOutButtonAction(ActionEvent event) throws IOException, SQLException, UnknownHostException, Exception {
// 		String username = Database.getPatientInfo(Database.userID).getString("username");
// 		System.out.println(username + " signed out");
// 		Database.signOut();
// 		App.loadPage("SignInView", stage);
// 	}

// 	@FXML public void handleOnMousePressed(MouseEvent event) throws IOException, Exception {
// 		Button button = (Button) event.getSource();

// 		button.setStyle("-fx-background-color: #ff6666");
// 		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
// 		ft.setFromValue(button.getOpacity());
// 		ft.setToValue(1);
// 		ft.setCycleCount(1);
// 		ft.play();
// 	}
	
// 	@FXML public void handleOnMouseReleased(MouseEvent event) throws IOException, Exception {
// 		Button button = (Button) event.getSource();

// 		if (button != currentButton) {
// 			button.setStyle("-fx-background-color: #cc7878");
// 		}
		
// 		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
// 		ft.setFromValue(button.getOpacity());
// 		ft.setToValue(0.7);
// 		ft.setCycleCount(1);
// 		ft.play();
// 	}

// 	@FXML public void handleOnMouseEntered(MouseEvent event) {
// 		Button button = (Button) event.getSource();

// 		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
// 		ft.setFromValue(button.getOpacity());
// 		ft.setToValue(0.7);
// 		ft.setCycleCount(1);
// 		ft.play();
// 	}

// 	@FXML public void handleOnMouseExited(MouseEvent event) {
// 		Button button = (Button) event.getSource();

// 		FadeTransition ft = new FadeTransition(Duration.millis(100), button);
// 		ft.setFromValue(button.getOpacity());
// 		ft.setToValue(0.5);
// 		ft.setCycleCount(1);
// 		ft.play();
// 	}

// 	@FXML public void handleOnMouseClickedVisits(MouseEvent event) throws IOException, Exception {
// 		App.loadPopup("PatientPortalVisitPopupView");
// 	}

// 	@FXML public void handleOnMouseEnteredVisits(MouseEvent event) {
// 		GridPane gridPane = (GridPane) event.getSource();
// 		gridPane.setStyle("-fx-background-color: #ff6666");
// 	}	

// 	@FXML public void handleOnMouseExitedVisits(MouseEvent event) {
// 		GridPane gridPane = (GridPane) event.getSource();
// 		gridPane.setStyle("-fx-background-color: #ab5b67");
// 	}

// 	@FXML public void handleMyInformationButtonAction(ActionEvent event) throws IOException, Exception { //! Needs work
// 		Button button = (Button) event.getSource();

// 		currentTab.setVisible(false); //! Disable the current tab
// 		currentTab = myInformationPane;
// 		currentTab.setVisible(true);

// 		currentButton.setStyle("-fx-background-color: #cc7878");
// 		currentButton = button;
// 		currentButton.setStyle("-fx-background-color: #ff6666");

// 		ResultSet resultSet = Database.getPatientInfo(Database.userID);
// 		ResultSetMetaData metaData = resultSet.getMetaData();

// 		GridPane contactInformationRow = new GridPane();
// 		contactInformationRow.setGridLinesVisible(true);

// 		GridPane.setHalignment(contactInformationRow, HPos.CENTER);

// 		contactInformationRow.setPrefWidth(1512);
// 		contactInformationRow.setPrefHeight(50);
// 		Label contactInformationLabel = new Label("Contact Information");
// 		contactInformationLabel.setFont(Font.font("Arial", 25));
// 		contactInformationRow.add(contactInformationLabel, 0, 0);
// 		GridPane.setHalignment(contactInformationLabel, HPos.RIGHT);
// 		GridPane.setValignment(contactInformationLabel, VPos.CENTER);

// 		informationGridPane.add(contactInformationRow, 0, 0);

// 		// for (int i = 1; i <= metaData.getColumnCount(); i++) {
// 		// 	String columnName = metaData.getColumnName(i);
// 		// 	String value = resultSet.getString(i);

// 		// 	GridPane gridPane = new GridPane();
// 		// 	gridPane.setPrefWidth(1512);
// 		// 	gridPane.setMinWidth(1512);
// 		// 	gridPane.setMaxWidth(1512);
// 		// 	informationGridPane.setGridLinesVisible(true);
// 		// 	gridPane.setLayoutX(500);
// 		// 	gridPane.resize(1512, 50);
// 		// 	Label columnLabel = new Label(String.format("%s: ", columnName));
// 		// 	TextField valueField = new TextField(value);
// 		// 	GridPane.setHalignment(columnLabel, HPos.RIGHT);
// 		// 	GridPane.setHalignment(valueField, HPos.CENTER);
// 		// 	gridPane.setGridLinesVisible(true);

// 		// 	Label columnLabel2 = new Label(String.format("%s: ", columnName));
// 		// 	TextField valueField2 = new TextField(value);
			
// 		// 	gridPane.add(columnLabel, 0, 0);
// 		// 	gridPane.add(valueField, 1, 0);
// 		// 	gridPane.add(columnLabel2, 2, 0);
// 		// 	gridPane.add(valueField2, 3, 0);

// 		// 	informationGridPane.add(gridPane, 0, i);
// 		// 	informationGridPane.setPrefHeight(informationGridPane.getPrefHeight() + 50);

// 		// 	for (Node node : informationGridPane.getChildren()) {
// 		// 		if (node instanceof GridPane) {
// 		// 			GridPane row = (GridPane) node;
// 		// 			if (!row.styleProperty().getValue().equals("-fx-background-color: #ab5b67;")) {
// 		// 				GridPane.setRowIndex(row
// 		// 			}
// 		// 		}
// 		// 	}
// 		// }
// 	}
	
// 	public String getTitle() {
// 		return this.title;
// 	}

// 	public void setcurrentTab(Pane pane) {
// 		currentTab.setVisible(false);
// 		currentTab = pane;
// 		currentTab.setVisible(true);
// 	}
// }
