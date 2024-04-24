package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.CreationType;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

        LocalTime[] visitTimes = Database.getVisitTimes();
        for (LocalTime visitTime : visitTimes) {
            timeChoiceBox.getItems().add(visitTime.toString());
        }

        ResultSet doctors = Database.getDoctors();
        while (doctors.next()) {
            String doctorName = doctors.getString("firstName") + " " + doctors.getString("lastName");
            int doctorID = doctors.getInt("ID");

            doctorsMap.put(doctorName, doctorID);
            doctorChoiceBox.getItems().add(doctorName);
        }
        doctors.close();
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        patientID = (Integer) data.get("patientID");
        String patientName = (String) data.get("patientName");
        patientNameLabel.setText(patientName);
    }

    @FXML public void handleScheduleVisitButtonAction(ActionEvent event) throws SQLException {
        boolean valid = Utilities.validate(dateTextField, Utilities.BIRTH_DATE_REGEX) &
            Utilities.validate(timeChoiceBox) &
            Utilities.validate(creationTypeChoiceBox) &
            Utilities.validate(doctorChoiceBox) &
            Utilities.validate(reasonTextField, Utilities.MESSAGE_REGEX) &
            Utilities.validate(descriptionTextArea, Utilities.MESSAGE_REGEX);

        if (!valid) return;

        Database.insertVisitFor(2, CreationType.valueOf(creationTypeChoiceBox.getValue()), doctorsMap.get(doctorChoiceBox.getValue()), reasonTextField.getText(), descriptionTextArea.getText(), dateTextField.getText(), timeChoiceBox.getValue()); //! FIX ME
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws SQLException {
        closeAndNullify();
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}