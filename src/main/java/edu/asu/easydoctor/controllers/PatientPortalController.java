package edu.asu.easydoctor.controllers;

import java.util.HashMap;

import edu.asu.easydoctor.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

    double rowWidth;
    double rowHeight;

    public HashMap<Button, Pane> buttonToPane = new HashMap<Button, Pane>();
    
    public void initialize() throws Exception {
        System.out.println("PatientPortalController");
        title = "Patient Portal";
        width = 1512;
        height = 800;
        rowWidth = 1512;
        rowHeight = 50;
        resizable = true;

        setCurrentTab(myVisitsPane, myVisitsButton);

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
    }
    
    public void setCurrentTab(AnchorPane pane, Button button) {
        if (currentTab != null) {
            currentTab.setVisible(false);
            currentTab.setDisable(true);

            currentButton.setStyle("-fx-background-color: #cc7878");
        }

        currentTab = pane;
        currentTab.setVisible(true);
        currentTab.setDisable(false);

        currentButton = button;
        currentButton.setStyle("-fx-background-color: #ff6666");
    }
}
