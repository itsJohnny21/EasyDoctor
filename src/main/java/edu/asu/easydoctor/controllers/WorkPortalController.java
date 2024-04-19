package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

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
import javafx.scene.Node;
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

    
    public AnchorPane currentTab;
    public Button currentButton;
    public DialogController currentDialog;

    @FXML public AnchorPane rootPane;
    @FXML public GridPane tabsPane;
    
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
    @FXML public Button inboxNewMessageButton;
    
    @FXML public AnchorPane prescriptionToolPane;
    @FXML public ScrollPane prescriptionToolScrollPane;
    @FXML public Button prescriptionToolButton;
    
    @FXML public Label chatPatientNameLabel;
    @FXML public ScrollPane chatScrollPane;
    @FXML public AnchorPane chatPane;
    @FXML public Button chatSendButton;
    @FXML public TextArea chatMessageTextArea;
    @FXML public Button chatGoBackButton;

    public static WorkPortalController instance = null;
    public static final String TITLE = "Work Portal";
    public static final boolean RESIZABLE = true;
    public static final String VIEW_FILENAME = "WorkPortalView";
    public static final String STYLE_FILENAME = "PatientPortalView";
    public final Integer myID = Database.getMyID();
    public static Integer chatPatientID;

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

        rootPane.setOnKeyPressed(event -> {
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
            } else if (event.getCode() == KeyCode.R && event.isMetaDown()) {
                currentButton.fire();
            }
        });

        chatMessageTextArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        chatMessageTextArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) {
                    chatMessageTextArea.appendText("\n");
                } else {
                    chatSendButton.fire();
                }
            }
        });


        visitsButton.fire();
        inboxButton.fire(); //! DELETE ME
    }

    @FXML public void handleKeyTyped(KeyEvent event) {
        Node source = (Node) event.getSource();
        Utilities.removeClass(source, "error");
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
        if (chatPatientID != null) {
            setCurrentTab(chatPane, inboxButton);
            loadChatFor(chatPatientID);
            return;
        }
        
        setCurrentTab(inboxPane, inboxButton);
        loadInboxMessages();
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
        // loadInboxMessages();
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) {

    }

    @FXML public void handleScheduleVisitButton(ActionEvent event) {

    }

    @FXML public void handleChatGoBackButtonAction(ActionEvent event) {
        setCurrentTab(inboxPane, inboxButton);
        chatPatientID = null;
    }

    public void loadInboxMessages() throws SQLException {
        ResultSet messages = Database.getMyMessages2(false);
        
        GridPane content = new GridPane();
        Utilities.addClass(content, "chat-content-pane");
        HashSet<Integer> visited = new LinkedHashSet<>();

        ArrayList<Row> rows = new ArrayList<>();

        while (messages.next()) {
            int senderID = messages.getInt("senderID");
            int receiverID = messages.getInt("receiverID");
            int patientID = senderID == myID ? receiverID : senderID;

            if (visited.contains(patientID)) {
                continue;
            }
            visited.add(patientID);

            String patient = Database.getPatientNameFor(patientID);
            String message = messages.getString("message");
            String dateTime = Utilities.prettyDateTime(messages.getTimestamp("creationTime"));
            String type = senderID == myID ? "Sent" : "Received";

            ValueLabel patientLabel = new ValueLabel(patient);
            ValueLabel messageLabel = new ValueLabel(message);
            ValueLabel dateTimeLabel = new ValueLabel(dateTime);
            ValueLabel typeLabel = new ValueLabel(type);

            Row row = new Row("conversations", patientID, patientLabel, messageLabel, dateTimeLabel, typeLabel);
            rows.add(row);
        }
        messages.close();

        SelectableTable myMessagesTable = new SelectableTable();
        myMessagesTable
            .withRowAction(row -> {
                row.setOnMouseClicked(event -> {
                    try {
                        loadChatFor(row.rowID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            })
            .withRows(rows)
            .build();

        inboxScrollPane.setContent(myMessagesTable);
    }

    public void loadChatFor(int patientID) throws SQLException {
        setCurrentTab(chatPane);
        chatPatientID = patientID;

        String patientName = Database.getPatientNameFor(patientID);
        chatPatientNameLabel.setText(patientName);

        ResultSet messages = Database.getMyMessagesWith(patientID);
        
        GridPane content = new GridPane();
        Utilities.addClass(content, "chat-content-pane");

        int counter = 0;

        while (messages.next()) {
            LocalDateTime creationTime = Utilities.convertUTCtoLocal(messages.getTimestamp("creationTime"));
            String message = messages.getString("message");
            int senderID = messages.getInt("senderID");

            VBox messageVBox = new VBox();
            Utilities.addClass(messageVBox, "chat-message-vbox");

            HBox messageHBox = new HBox();
            Utilities.addClass(messageHBox, "chat-message-hbox");

            Label dateTimeLabel = new Label(Utilities.prettyDateTime(creationTime));
            Utilities.addClass(dateTimeLabel, "chat-date-time-label");

            Label messageLabel = new Label(message);
            Utilities.addClass(messageLabel, "chat-message-label");
            messageLabel.setWrapText(true);

            VBox iconVBox = new VBox();
            Utilities.addClass(iconVBox, "chat-icon-vbox");

            ImageView icon = new ImageView();
            Utilities.addClass(icon, "chat-icon");
            iconVBox.getChildren().add(icon);

            if (senderID == myID) {
                icon.setImage(new Image(App.class.getResourceAsStream("icons/doctor.png")));
                HBox.setMargin(icon, new Insets(0, 50, 0, 10));
                
                Utilities.addClass(messageVBox, "chat-message-vbox-right");
                Utilities.addClass(messageHBox, "chat-message-hbox-right");
                GridPane.setMargin(messageVBox, new Insets(0, 50, 0, 500));
                VBox.setMargin(dateTimeLabel, new Insets(0, 100, 0, 0));
                
                messageHBox.getChildren().add(messageLabel);
                messageHBox.getChildren().add(iconVBox);
                
            } else {
                icon.setImage(new Image(App.class.getResourceAsStream("icons/patient.png")));
                HBox.setMargin(icon, new Insets(0, 10, 0, 50));

                Utilities.addClass(messageVBox, "chat-message-vbox-left");
                Utilities.addClass(messageHBox, "chat-message-hbox-left");
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
        
        chatScrollPane.setContent(content);
        chatScrollPane.setVvalue(1.0);

        Database.readAllMessagesWith(patientID);
    }

    @FXML public void handleChatSendButtonAction(ActionEvent event) throws SQLException {
        chatMessageTextArea.setText(chatMessageTextArea.getText().trim());
        if (!Utilities.validate(chatMessageTextArea, Utilities.MESSAGE_REGEX)) {
            return;
        }

        Database.sendMessageTo(chatPatientID, chatMessageTextArea.getText());
        chatMessageTextArea.clear();
        loadChatFor(chatPatientID);
    }

    public void setCurrentTab(AnchorPane pane) {
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
