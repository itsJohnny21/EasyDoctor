package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.VisitStatus;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class SelectedVisitWorkPortalView extends DialogController {

    @FXML public AnchorPane rootPane;
    @FXML public Button closeButton;
    @FXML public Button contactButton;
    @FXML public Button startButton;

    @FXML public Label patientLabel;
    @FXML public Label doctorLabel;
    @FXML public Label dateLabel;
    @FXML public Label timeLabel;
    @FXML public Label reasonLabel;
    @FXML public Label statusLabel;

    @FXML public TextArea descriptionTextArea;

    public static SelectedVisitWorkPortalView instance = null;
    public final static String TITLE = "Visit";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "SelectedVisitWorkPortalView";
    public final static String STYLE_FILENAME = "PatientPortalView";
    Integer patientID = null;
    Integer rowID = null;


    private SelectedVisitWorkPortalView() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SelectedVisitWorkPortalView getInstance() {
        if (instance == null) {
            instance = new SelectedVisitWorkPortalView();
        }

        return instance;
    }

    public void initialize() throws Exception {
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws Exception {
        closeAndNullify();
    }

    @FXML public void handleContactButtonAction(ActionEvent event) throws Exception {
        result.put("patientUsername", Database.getUsernameFor(patientID));
        closeAndNullify();
    }

    @FXML public void handleStartButtonAction(ActionEvent event) throws Exception {
        result.put("start", true);
        closeAndNullify();
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        rowID = (Integer) data.get("rowID");
        ResultSet visit = Database.getVisit(rowID);

        if (visit.next()) {
            patientID = visit.getInt("patientID");
            VisitStatus visitStatus = VisitStatus.valueOf(visit.getString("status"));

            String patient = Database.getPatientNameFor(patientID);
            String doctor = Database.getEmployeeNameFor(visit.getInt("doctorID"));
            String localdate = Utilities.prettyDate(visit.getDate("localdate").toLocalDate());
            String localtime = Utilities.prettyTime(visit.getTime("localtime").toLocalTime());
            String reason = visit.getString("reason");
            String status = visitStatus.toString();

            patientLabel.setText(patient);
            doctorLabel.setText(doctor);
            dateLabel.setText(localdate);
            timeLabel.setText(localtime);
            reasonLabel.setText(reason);
            statusLabel.setText(status);

            if (visitStatus == VisitStatus.PENDING) {
                startButton.setDisable(false);
            } else {
                startButton.setDisable(true);
            }
        }
        visit.close();
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}