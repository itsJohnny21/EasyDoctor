package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.ValueLabel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WorkPortalController extends Controller {

    @FXML public AnchorPane mainPane;
    @FXML public GridPane tabsPane;

    public AnchorPane currentTab;
    public Button currentButton;
    public DialogController currentDialog;
    
    @FXML public Button signOutButton;
    
    @FXML public AnchorPane usernamePane;
    @FXML public Button usernameButton;
    @FXML public ScrollPane usernameScrollPane;
    
    @FXML public AnchorPane visitsPane;
    @FXML public ScrollPane visitsScrollPane;
    @FXML public Button visitsButton;
    @FXML public Button scheduleVisitButton;
    
    @FXML public AnchorPane patientRecordsPane;
    @FXML public ScrollPane patientRecordsScrollPane;
    @FXML public Button patientRecordsButton;
    @FXML public Button patientRecordsEditButton;
    @FXML public Button patientRecordsSaveButton;
    @FXML public Button patientRecordsCancelButton;
    
    @FXML public AnchorPane activeSessionsPane;
    @FXML public ScrollPane activeSessionsScrollPane;
    @FXML public Button activeSessionsButton;
    @FXML public Button activeSessionsPutBackButton;
    @FXML public Button activeSessionsStartButton;
    @FXML public Button activeSessionsFinishButton;

    @FXML public AnchorPane inboxPane;
    @FXML public ScrollPane inboxScrollPane;
    @FXML public Button inboxButton;
    @FXML public Button inboxSendButton;
    @FXML public TextArea inboxMessageTextArea;
    
    @FXML public AnchorPane prescriptionToolPane;
    @FXML public ScrollPane prescriptionToolScrollPane;
    @FXML public Button prescriptionToolButton;
    

    public static WorkPortalController instance = null;
    public static final String TITLE = "Work Portal";
    public static final boolean RESIZABLE = true;
    public static final String VIEW_FILENAME = "WorkPortalView";
    public static final String STYLE_FILENAME = "WorkPortalView";
    public final Integer myID = Database.getMyID();

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

        usernameButton.setText(Database.getMy("username"));

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
                prescriptionToolButton.fire();
            }
        });

        inboxMessageTextArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        inboxMessageTextArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    inboxMessageTextArea.appendText("\n");
                } else {
                    inboxSendButton.fire();
                }
            }
        });

        visitsButton.fire();
    }

    @FXML public void handleVisitsButtonAction(ActionEvent event) throws Exception {
        setCurrentTab(visitsPane, visitsButton);

        ResultSet visits = Database.getMyVisits();
        ArrayList<Row> rows = new ArrayList<>();

        while (visits.next()) {
            int rowID = visits.getInt("ID");

            String doctor = Database.getEmployeeNameFor(visits.getInt("doctorID"));
            String dayOfWeek = visits.getDate("date").toLocalDate().getDayOfWeek().toString();
            String date = Utilities.prettyDate(visits.getDate("date"));
            String time = Utilities.prettyTime(visits.getTime("time"));
            String reason = visits.getString("reason");
            boolean completed = visits.getBoolean("completed");

            ValueLabel doctorLabel = new ValueLabel(doctor);
            ValueLabel dateLabel = new ValueLabel(date);
            ValueLabel dayLabel = new ValueLabel(dayOfWeek);
            ValueLabel timeLabel = new ValueLabel(time);
            ValueLabel reasonLabel = new ValueLabel(reason);
            ValueLabel statusLabel = new ValueLabel(Utilities.getVisitStatus(visits.getDate("date"), visits.getTime("time"), completed));


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

        visitsScrollPane.setContent(myVisitsTables);
    }   

    @FXML public void handlePatientRecordsButtonAction(ActionEvent event) {
        setCurrentTab(patientRecordsPane, patientRecordsButton);
    }

    @FXML public void handleActiveSessionsButton(ActionEvent event) {
        setCurrentTab(activeSessionsPane, activeSessionsButton);
    }

    @FXML public void handleInboxButtonAction(ActionEvent event) throws Exception {
        setCurrentTab(inboxPane, inboxButton);

        // String doctorName = Database.getMyDoctorName();

        // if (doctorName == null) {
        //     // inboxDoctorNameLabel.setText("No doctor assigned");
        //     inboxPane.setDisable(true);
        //     return;
        // }

        // // inboxDoctorNameLabel.setText(doctorName);
        // loadinboxMessages();
    }

    @FXML public void handlePrescriptionToolButtonAction(ActionEvent event) {
        setCurrentTab(prescriptionToolPane, prescriptionToolButton);
    }

    @FXML public void handleUsernameButtonAction(ActionEvent event) {
        setCurrentTab(usernamePane, usernameButton);
    }

    @FXML public void handleSignOutButtonAction(ActionEvent event) throws Exception {
        closeAndNullify();
        Database.signOut();
        SignInController.getInstance().load();
    }

    @FXML public void handleInboxSendButtonAction(ActionEvent event) throws SQLException {
        // inboxMessageTextArea.setText(inboxMessageTextArea.getText().trim());
        // if (!Utilities.validate(inboxMessageTextArea, Utilities.MESSAGE_REGEX)) {
        //     return;
        // }

        // Database.sendMessageToMyDoctor(inboxMessageTextArea.getText());
        // inboxMessageTextArea.clear();
        // loadinboxMessages();
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) {

    }

    @FXML public void handleScheduleVisitButton(ActionEvent event) {

    }

    public void loadinboxMessages() throws SQLException {
        ResultSet messages = Database.getMyChatMessages();
        
        GridPane content = new GridPane();
        Utilities.addClass(content, "inbox-content-pane");

        int counter = 0;

        while (messages.next()) {
            LocalDateTime creationTime = Utilities.convertUTCtoLocal(messages.getTimestamp("creationTime"));
            String message = messages.getString("message");
            int senderID = messages.getInt("senderID");

            VBox messageVBox = new VBox();
            Utilities.addClass(messageVBox, "inbox-message-vbox");

            HBox messageHBox = new HBox();
            Utilities.addClass(messageHBox, "inbox-message-hbox");

            Label dateTimeLabel = new Label(Utilities.prettyDateTime(creationTime));
            Utilities.addClass(dateTimeLabel, "inbox-date-time-label");

            Label messageLabel = new Label(message);
            Utilities.addClass(messageLabel, "inbox-message-label");
            messageLabel.setWrapText(true);

            VBox iconVBox = new VBox();
            Utilities.addClass(iconVBox, "inbox-icon-vbox");

            ImageView icon = new ImageView();
            Utilities.addClass(icon, "inbox-icon");
            iconVBox.getChildren().add(icon);

            if (senderID == myID) {
                icon.setImage(new Image(App.class.getResourceAsStream("icons/patient.png")));
                HBox.setMargin(icon, new Insets(0, 50, 0, 10));
                
                Utilities.addClass(messageVBox, "inbox-message-vbox-right");
                Utilities.addClass(messageHBox, "inbox-message-hbox-right");
                GridPane.setMargin(messageVBox, new Insets(0, 50, 0, 500));
                VBox.setMargin(dateTimeLabel, new Insets(0, 100, 0, 0));
                
                messageHBox.getChildren().add(messageLabel);
                messageHBox.getChildren().add(iconVBox);
                
            } else {
                icon.setImage(new Image(App.class.getResourceAsStream("icons/doctor.png")));
                HBox.setMargin(icon, new Insets(0, 10, 0, 50));

                Utilities.addClass(messageVBox, "inbox-message-vbox-left");
                Utilities.addClass(messageHBox, "inbox-message-hbox-left");
                GridPane.setMargin(messageVBox, new Insets(0, 500, 0, 50));
                VBox.setMargin(dateTimeLabel, new Insets(0, 0, 0, 100));

                messageHBox.getChildren().add(iconVBox);
                messageHBox.getChildren().add(messageLabel);
            }

            icon.setFitWidth(75); //! Do this in css
            icon.setFitHeight(75); //! Do this in css

            messageVBox.getChildren().add(dateTimeLabel);
            messageVBox.getChildren().add(messageHBox);
            content.add(messageVBox, 0, counter++);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    messageHBox.prefHeightProperty().bind(messageLabel.heightProperty());
                }
            });
        }
        messages.close();
            
        inboxScrollPane.setContent(content);
    }

    public void setCurrentTab(AnchorPane pane, Button button) {
        if (currentTab == pane) {
            return;
        }

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
