package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class PatientPortalController extends Controller {

    @FXML public AnchorPane mainPane;
    @FXML public GridPane tabsPane;

    public AnchorPane currentTab;
    public Button currentButton;
    
    @FXML public AnchorPane myVisitsPane;
    @FXML public AnchorPane myPillsPane;
    @FXML public AnchorPane usernamePane;
    
    @FXML public Button myVisitsButton;
    @FXML public Button myPillsButton;
    @FXML public Button usernameButton;
    @FXML public Button signOutButton;
    
    @FXML public ScrollPane myVisitsScrollPane;
    @FXML public ScrollPane myPillsScrollPane;
    @FXML public ScrollPane usernameScrollPane;
    @FXML public ScrollPane signOutScrollPane;
    
    @FXML public AnchorPane myInformationPane;
    @FXML public ScrollPane myInformationScrollPane;
    @FXML public Button myInformationButton;
    @FXML public Button myInformationEditButton;
    @FXML public Button myInformationSaveButton;
    @FXML public Button myInformationCancelButton;

    @FXML public AnchorPane inboxPane;
    @FXML public ScrollPane inboxScrollPane;
    @FXML public Button inboxButton;
    @FXML public Button inboxNewMessageButton;
    
    @FXML public AnchorPane scheduleVisitPane;
    @FXML public ScrollPane scheduleVisitScrollPane;
    @FXML public Button scheduleVisitButton;
    @FXML public ChoiceBox<String> scheduleVisitDoctorChoiceBox;
    @FXML public ChoiceBox<String> scheduleVisitDateChoiceBox;
    @FXML public ChoiceBox<String> scheduleVisitTimeChoiceBox;
    @FXML public TextArea scheduleVisitDescriptionTextArea;

    public static PatientPortalController instance = null;
    public static final String TITLE = "Patient Portal";
    public static final boolean RESIZABLE = false;
    public static final String VIEW_FILENAME = "PatientPortalView";
    public static final String STYLE_FILENAME = "SignUpView";

    private PatientPortalController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static PatientPortalController getInstance() {
        if (instance == null) {
            instance = new PatientPortalController();
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

    @FXML public void handleMyVisitsButtonAction(ActionEvent event) {
        setCurrentTab(myVisitsPane, myVisitsButton);
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

    @FXML public void handleSignOutButtonAction(ActionEvent event) throws Exception {
        close();
        Database.signOut();
    }

    @FXML public void handleScheduleVisitButton(ActionEvent event) throws IOException {
        // ScheduleVisitController.getInstance().load(stage); //! Implement this
    }

    @FXML public void handleNewMessageButtonAction(ActionEvent event) throws IOException {
        // NewMessageController.getInstance().load(stage); //! Implement this
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
}
