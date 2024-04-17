package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.Utilities;
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
    public DialogController currentDialog;
    
    @FXML public Button signOutButton;
    
    @FXML public AnchorPane usernamePane;
    @FXML public Button usernameButton;
    @FXML public ScrollPane usernameScrollPane;
    
    @FXML public AnchorPane myVisitsPane;
    @FXML public ScrollPane myVisitsScrollPane;
    @FXML public Button myVisitsButton;
    
    @FXML public AnchorPane myInformationPane;
    @FXML public ScrollPane myInformationScrollPane;
    @FXML public Button myInformationButton;
    @FXML public Button myInformationEditButton;
    @FXML public Button myInformationSaveButton;
    @FXML public Button myInformationCancelButton;
    
    @FXML public AnchorPane scheduleVisitPane;
    @FXML public ScrollPane scheduleVisitScrollPane;
    @FXML public Button scheduleVisitButton;
    @FXML public ChoiceBox<String> scheduleVisitDoctorChoiceBox;
    @FXML public ChoiceBox<String> scheduleVisitDateChoiceBox;
    @FXML public ChoiceBox<String> scheduleVisitTimeChoiceBox;
    @FXML public TextArea scheduleVisitDescriptionTextArea;

    @FXML public AnchorPane chatPane;
    @FXML public ScrollPane chatScrollPane;
    @FXML public Button chatButton;
    @FXML public Button inboxNewMessageButton;
    
    @FXML public AnchorPane myPillsPane;
    @FXML public ScrollPane myPillsScrollPane;
    @FXML public Button myPillsButton;
    

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
                chatButton.fire();
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

        ResultSet visits = Database.getMyVisits();
        ArrayList<Row> rows = new ArrayList<>();

        while (visits.next()) {
            int rowID = visits.getInt("ID");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            String doctor = Database.getEmployeeNameFor(visits.getInt("doctorID"));
            String dayOfWeek = visits.getDate("date").toLocalDate().getDayOfWeek().toString();
            String time = visits.getTime("time").toLocalTime().format(timeFormatter);
            String date = visits.getDate("date").toLocalDate().format(dateFormatter);
            String reason = visits.getString("reason");
            boolean completed = visits.getBoolean("completed");

            LocalDateTime dateTime = LocalDateTime.of(visits.getDate("date").toLocalDate(), visits.getTime("time").toLocalTime());

            ValueLabel doctorLabel = new ValueLabel(doctor);
            ValueLabel dateLabel = new ValueLabel(date);
            ValueLabel dayLabel = new ValueLabel(dayOfWeek);
            ValueLabel timeLabel = new ValueLabel(time);
            ValueLabel reasonLabel = new ValueLabel(reason);
            ValueLabel statusLabel = new ValueLabel(Utilities.getVisitStatus(dateTime, completed));


            Row row = new Row( "visits", rowID, doctorLabel, dateLabel, dayLabel, timeLabel, reasonLabel, statusLabel);
            rows.add(row);  
        }
        visits.close();

        SelectableTable myVisitsTables = new SelectableTable();
        myVisitsTables
            .withRowAction(row -> {
                row.setOnMouseClicked(event2 -> {
                    try {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("rowID", row.rowID);
                        HashMap<String, Object> result = loadDialog(SelectedVisitController.getInstance(), data);

                        if (result != null && (boolean) result.containsKey("deleted")) {
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

    @FXML public void handleChatButtonAction(ActionEvent event) throws SQLException {
        if (currentTab == chatPane) {
            return;
        }

        setCurrentTab(chatPane, chatButton);

        ResultSet messages = Database.getMyChatMessages();

        while (messages.next()) {
            LocalDateTime creationTime = messages.getTimestamp("creationTime").toLocalDateTime();
            String message = messages.getString("message");
            boolean readStatus = messages.getBoolean("readStatus");
            int senderID = messages.getInt("senderID");
            int receiverID = messages.getInt("receiverID");

            if (senderID == 3) {
                System.out.println("Doctor: " + message);
            } else {
                System.out.println("Patient: " + message);
            }
            System.out.println("Read: " + readStatus);
            System.out.println("Sent: " + creationTime);
            System.out.println();
        }
        messages.close();
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
        closeAndNullify();
        Database.signOut();
        SignInController.getInstance().load();
    }

    @FXML public void handleScheduleVisitButton(ActionEvent event) throws IOException {
        // ScheduleVisitController.getInstance().load(); //! Implement this
    }

    @FXML public void handleNewMessageButtonAction(ActionEvent event) throws IOException {
        // NewMessageController.getInstance().load(); //! Implement this
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

    public HashMap<String, Object> loadDialog(DialogController dialogController, HashMap<String, Object> data) throws Exception {
        currentDialog = dialogController;
        return dialogController.loadDialog(data);
    }

    public void closeAndNullify() {
        instance = null;
        close();
        
        if (currentDialog != null) {
            currentDialog.closeAndNullify();
        }
    }
}
