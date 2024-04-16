package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class WorkPortalController extends Controller {

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

    public static WorkPortalController instance = null;
    public static final String TITLE = "Patient Portal";
    public static final boolean RESIZABLE = false;
    public static final String VIEW_FILENAME = "PatientPortalView";
    public static final String STYLE_FILENAME = "SignUpView";

    private WorkPortalController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static WorkPortalController getInstance() {
        if (instance == null) {
            instance = new WorkPortalController();
        }

        return instance;
    }

    
    public void initialize() throws Exception {

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

    @FXML public void handleMyInformationButtonAction(ActionEvent event) {
        setCurrentTab(myInformationPane, myInformationButton);
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) {
        setCurrentTab(scheduleVisitPane, scheduleVisitButton);
    }

    @FXML public void handleInboxButtonAction(ActionEvent event) {
        setCurrentTab(inboxPane, inboxButton);
    }

    @FXML public void handleMyPillsButtonAction(ActionEvent event) {
        setCurrentTab(myPillsPane, myPillsButton);
    }

    @FXML public void handleUsernameButtonAction(ActionEvent event) {
        setCurrentTab(usernamePane, usernameButton);
    }

    @FXML public void handleSignOutButtonAction(ActionEvent event) throws IOException {
        closeAndNullify();
        SignInController.getInstance().load();
    }
    
    public void setCurrentTab(AnchorPane pane, Button button) {
        if (currentTab != null) {
            currentTab.setVisible(false);
            currentTab.setDisable(true);
        }

        currentTab = pane;
        currentTab.setVisible(true);
        currentTab.setDisable(false);

        currentButton = button;
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}
