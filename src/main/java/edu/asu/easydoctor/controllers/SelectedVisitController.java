package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class SelectedVisitController extends DialogController {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button closeButton;
    @FXML public Button cancelVisitButton;

    @FXML public Label doctorLabel;
    @FXML public Label dateLabel;
    @FXML public Label dayLabel;
    @FXML public Label timeLabel;
    @FXML public Label reasonLabel;
    @FXML public Label statusLabel;

    @FXML public TextArea descriptionTextArea;
    @FXML public Label addressLabel;

    public static SelectedVisitController instance = null;
    public final static String TITLE = "Visit";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "SelectedVisitView";
    public final static String STYLE_FILENAME = "PatientPortalView";

    public Integer rowID;


    private SelectedVisitController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SelectedVisitController getInstance() {
        if (instance == null) {
            instance = new SelectedVisitController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        addressLabel.setText("Address: 2601 E Roosevelt St Phoenix, AZ 85008 United States");
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws Exception {
        closeAndNullify();
    }

    @FXML public void handleCancelVisitButtonAction(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Visit");
        alert.setHeaderText("Are you sure you want to cancel this visit?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            Database.deleteRow("visits", rowID);
            result.put("deleted", true);
            closeAndNullify();
        }
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        rowID = (Integer) data.get("rowID");
        ResultSet visit = Database.getVisitFor(rowID);

        if (visit.next()) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            doctorLabel.setText(Database.getEmployeeNameFor(visit.getInt("doctorID")));
            dateLabel.setText(visit.getDate("date").toLocalDate().format(dateFormatter));
            dayLabel.setText(visit.getDate("date").toLocalDate().getDayOfWeek().toString());
            timeLabel.setText(visit.getTime("time").toLocalTime().format(timeFormatter));
            reasonLabel.setText(visit.getString("reason"));
            statusLabel.setText(visit.getBoolean("completed") ? "Complete" : "Incomplete");
            descriptionTextArea.setText(visit.getString("description"));
        }
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}