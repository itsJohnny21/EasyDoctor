package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.asu.easydoctor.DataRow;
import edu.asu.easydoctor.DataRow.Visit;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.ValueLabel;
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
    public static final boolean RESIZABLE = true;
    public static final String VIEW_FILENAME = "PatientPortalView";
    public static final String STYLE_FILENAME = "PatientPortalView";

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

        myVisitsButton.fire();
    }

    @FXML public void handleMyVisitsButtonAction(ActionEvent event) throws Exception {
        if (currentTab == myVisitsPane) {
            return;
        }

        setCurrentTab(myVisitsPane, myVisitsButton);

        ArrayList<Visit> visits = DataRow.Visit.getAllFor(Database.userID);
        Row[] rows = new Row[visits.size()];

        for (int i = 0; i < visits.size(); i++) {
            Visit visit = visits.get(i);

            String doctorName = Database.getEmployeeNameFor(Integer.parseInt(visit.doctorID.originalValue));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(visit.date.originalValue, formatter);
            DayOfWeek day = dateTime.getDayOfWeek();
            Time time = Time.valueOf(dateTime.toLocalTime());
            Date date = Date.valueOf(dateTime.toLocalDate()); 

            ValueLabel doctorLabel = new ValueLabel(doctorName);
            ValueLabel dayLabel = new ValueLabel(day.toString());
            ValueLabel timeLabel = new ValueLabel(time.toString());
            ValueLabel dateLabel = new ValueLabel(date.toString());
            ValueLabel reasonLabel = new ValueLabel(visit.reason.originalValue);
            ValueLabel completedLabel = new ValueLabel(visit.completed.originalValue == "1" ? "Complete" : "Incomplete");

            
            rows[i] = new Row(
                visit.tableName,
                visit.rowID,
                doctorLabel,
                dateLabel,
                dayLabel,
                timeLabel,
                reasonLabel,
                completedLabel
            );
        }

        SelectableTable myVisitsTables = new SelectableTable();
        myVisitsTables
            .withRowAction(row -> {
                row.setOnMouseClicked(event2 -> {
                    try {
                        boolean deleted = SelectedVisitController.loadDialog(row.rowID);
                        if (deleted) {
                            myVisitsTables.getChildren().remove(row);
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            })
            .withRows(rows)
            .build();

        myVisitsScrollPane.setContent(myVisitsTables);
    }   

    @FXML public void handleMyInformationButtonAction(ActionEvent event) {
        if (currentTab == myInformationPane) {
            return;
        }

        setCurrentTab(myInformationPane, myInformationButton);
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) {
        if (currentTab == scheduleVisitPane) {
            return;
        }

        setCurrentTab(scheduleVisitPane, scheduleVisitButton);
    }

    @FXML public void handleInboxButtonAction(ActionEvent event) {
        if (currentTab == inboxPane) {
            return;
        }

        setCurrentTab(inboxPane, inboxButton);
    }

    @FXML public void handleMyPillsButtonAction(ActionEvent event) {
        if (currentTab == myPillsPane) {
            return;
        }

        setCurrentTab(myPillsPane, myPillsButton);
    }

    @FXML public void handleUsernameButtonAction(ActionEvent event) {
        if (currentTab == usernamePane) {
            return;
        }

        setCurrentTab(usernamePane, usernameButton);
    }

    @FXML public void handleSignOutButtonAction(ActionEvent event) throws Exception {
        close();
        Database.signOut();
        SignInController.getInstance().load(stage);
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
