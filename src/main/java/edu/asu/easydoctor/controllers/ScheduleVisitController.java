package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.CreationType;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
    
public class ScheduleVisitController extends DialogController {
    @FXML public AnchorPane rootPane;
    @FXML public Label patientNameLabel;
    
    @FXML public Button scheduleVisitButton;
    @FXML public TextField dateTextField;
    @FXML public ChoiceBox<String> timeChoiceBox;
    @FXML public ChoiceBox<String> creationTypeChoiceBox;
    @FXML public ChoiceBox<String> doctorChoiceBox;
    @FXML public TextField reasonTextField;
    @FXML public TextArea descriptionTextArea;

    public static ScheduleVisitController instance = null;
    public final static String TITLE = "Visit";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "ScheduleVisitView";
    public final static String STYLE_FILENAME = "PatientPortalView";
    public Integer patientID = null;
    public HashMap<String, Integer> doctorsMap = new HashMap<>();

    private ScheduleVisitController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static ScheduleVisitController getInstance() {
        if (instance == null) {
            instance = new ScheduleVisitController();
        }

        return instance;
    }

    public void initialize() throws SQLException {
        CreationType[] creationTypes = CreationType.values();
        for (CreationType e : creationTypes) {
            creationTypeChoiceBox.getItems().add(e.toString());
        }

        ArrayList<LocalTime> visitTimes = Database.getVisitTimesFor(DayOfWeek.valueOf(LocalDate.now().getDayOfWeek().toString()));
        for (LocalTime visitTime : visitTimes) {
            timeChoiceBox.getItems().add(Utilities.prettyTime(visitTime));
        }

        ResultSet doctors = Database.getDoctors();
        while (doctors.next()) {
            String doctorName = doctors.getString("firstName") + " " + doctors.getString("lastName");
            int doctorID = doctors.getInt("ID");

            doctorsMap.put(doctorName, doctorID);
            doctorChoiceBox.getItems().add(doctorName);
        }
        doctors.close();

        timeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                boolean valid = validateDate() && newValue != null;


                if (valid) {
                    String date = dateTextField.getText();
                    String time = Utilities.standardTime(Utilities.parsePrettyTime(timeChoiceBox.getValue()));
                    valid = Database.isVisitAvailable(date, time);

                    if (valid) {
                        Utilities.addClass(timeChoiceBox, "valid");
                        Utilities.addClass(dateTextField, "valid");
                        return;
                    }
                }

                Utilities.removeClass(timeChoiceBox, "valid");
                Utilities.removeClass(dateTextField, "valid");

            } catch (SQLException e) {
                Utilities.removeClass(timeChoiceBox, "valid");
                Utilities.removeClass(dateTextField, "valid");
                return;
            }
        });
    }

    @FXML public void handleDateTextFieldOnKeyTyped(KeyEvent event) throws SQLException {
        validateDate();
    }

    public boolean validateDate() throws SQLException {
        boolean valid = dateTextField.getText().matches(Utilities.BIRTH_DATE_REGEX);

        try {
            LocalDate.parse(dateTextField.getText());
        } catch (Exception e) {
            valid = false;
        }

        if (valid && Utilities.validate(timeChoiceBox)) {
            String date = dateTextField.getText();
            String time = Utilities.standardTime(Utilities.parsePrettyTime(timeChoiceBox.getValue()));

            valid = Database.isVisitAvailable(date, time);

            if (valid) {
                Utilities.addClass(timeChoiceBox, "valid");
                Utilities.addClass(dateTextField, "valid");
                return true;
            }
        }

        Utilities.removeClass(timeChoiceBox, "valid");
        Utilities.removeClass(dateTextField, "valid");
        return false;
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        patientID = (Integer) data.get("patientID");
        String patientName = (String) data.get("patientName");
        patientNameLabel.setText(patientName);
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) throws SQLException {
        boolean valid = validateDate() &
            Utilities.validate(timeChoiceBox) &
            Utilities.validate(creationTypeChoiceBox) &
            Utilities.validate(doctorChoiceBox) &
            Utilities.validate(reasonTextField, Utilities.MESSAGE_REGEX) &
            Utilities.validate(descriptionTextArea, Utilities.MESSAGE_REGEX);

        if (!valid) return;

        CreationType creationType = CreationType.valueOf(creationTypeChoiceBox.getValue());
        int doctorID = doctorsMap.get(doctorChoiceBox.getValue());
        String reason = reasonTextField.getText();
        String description = descriptionTextArea.getText();
        String date = dateTextField.getText();
        String time = Utilities.standardTime(Utilities.parsePrettyTime(timeChoiceBox.getValue()));
        
        try {
            Database.insertVisitFor(patientID, creationType, doctorID, reason, description, date, time);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Visit Scheduled");
            alert.setContentText("Visit scheduled for %s on %s at %s.".formatted(patientNameLabel.getText(), Utilities.prettyDate(LocalDate.parse(date)), timeChoiceBox.getValue()));
            alert.setHeaderText("Success!");
            alert.showAndWait();
            
            closeAndNullify();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error!");
            alert.showAndWait();
        }
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws SQLException {
        closeAndNullify();
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}