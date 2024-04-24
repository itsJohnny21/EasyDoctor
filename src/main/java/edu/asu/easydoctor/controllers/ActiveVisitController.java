package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.AllergyType;
import edu.asu.easydoctor.Database.HealthConditionSeverity;
import edu.asu.easydoctor.Database.HealthConditionType;
import edu.asu.easydoctor.Database.Severity;
import edu.asu.easydoctor.Database.VaccineGroup;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.ValueLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ActiveVisitController extends DialogController {
    @FXML public AnchorPane rootPane;
    @FXML public Label patientNameLabel;
    @FXML public Button closeButton;
    @FXML public Button goBackButton;
    @FXML public Button nextButton;

    @FXML public AnchorPane vitalsPane;
    @FXML public AnchorPane healthConditionsPane;
    @FXML public AnchorPane allergiesPane;
    @FXML public AnchorPane vaccinesPane;
    @FXML public AnchorPane notesPane;

    @FXML public TextField weightTextField;
    @FXML public TextField heightTextField;
    @FXML public TextField systolicBloodPressureTextField;
    @FXML public TextField diastolicBloodPressureTextField;
    @FXML public TextField heartRateTextField;
    @FXML public TextField bodyTemperatureTextField;

    @FXML public ScrollPane healthConditionsScrollPane;
    @FXML public ScrollPane allergiesScrollPane;
    @FXML public ScrollPane vaccinesScrollPane;

    @FXML public TextArea notesTextArea;

    public static ActiveVisitController instance = null;
    public final static String TITLE = "Visit";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "ActiveVisitView";
    public final static String STYLE_FILENAME = "ActiveVisitView";
    public AnchorPane[] panes;
    public int currentPaneIndex;

    public Integer rowID;
    public Integer patientID;


    private ActiveVisitController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static ActiveVisitController getInstance() {
        if (instance == null) {
            instance = new ActiveVisitController();
        }

        return instance;
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        rowID = (Integer) data.get("rowID");
        ResultSet activeVisit = Database.getVisit(rowID);
        
        if (activeVisit.next()) {
            patientID = activeVisit.getInt("patientID");
            String height = activeVisit.getString("height");
            String weight = activeVisit.getString("weight");
            String systolicBloodPressure = activeVisit.getString("systolicBloodPressure");
            String diastolicBloodPressure = activeVisit.getString("diastolicBloodPressure");
            String heartRate = activeVisit.getString("heartRate");
            String bodyTemperature = activeVisit.getString("bodyTemperature");
            String notes = activeVisit.getString("notes");
            currentPaneIndex = activeVisit.getInt("currentPage");
            
            patientNameLabel.setText(Database.getPatientNameFor(patientID));
            heightTextField.setText(height);
            weightTextField.setText(weight);
            systolicBloodPressureTextField.setText(systolicBloodPressure);
            diastolicBloodPressureTextField.setText(diastolicBloodPressure);
            heartRateTextField.setText(heartRate);
            bodyTemperatureTextField.setText(bodyTemperature);
            notesTextArea.setText(notes);

            int nurseID = activeVisit.getInt("nurseID");
            if (activeVisit.wasNull()) {
                Database.updateActiveVisitNurse(rowID, Database.getMyID());
            }
        }
        activeVisit.close();

        panes = new AnchorPane[]{vitalsPane, healthConditionsPane, allergiesPane, vaccinesPane, notesPane};

        for (AnchorPane pane : panes) {
            pane.setDisable(true);
            pane.setVisible(false);
        }

        currentPaneIndex = Math.min(panes.length - 1, Math.max(0, currentPaneIndex));
        switchPane(0);
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws Exception {
        closeAndNullify();
    }

    @FXML public void handleGoBackButtonAction(ActionEvent event) throws Exception {
        if (currentPaneIndex <= 0) {
            currentPaneIndex = 0;
            return;
        }

        switchPane(currentPaneIndex--);
        Database.updateActiveVisitCurrentPage(rowID, currentPaneIndex);
    }

    @FXML public void handleNextButtonAction(ActionEvent event) throws Exception {
        if (currentPaneIndex >= panes.length - 1) {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Finish Visit");
            alert.setHeaderText("Are you sure you want to finish the visit?");
            alert.showAndWait();

            if (alert.getResult().getText().equals("Cancel")) {
                currentPaneIndex = panes.length - 1;
                return;
            }

            Database.finishVisit(rowID);
            closeAndNullify();
            return;
        }

        if (panes[currentPaneIndex] == vitalsPane && !validateVitalsPane()) {
            return;
        }

        switchPane(currentPaneIndex++);
        Database.updateActiveVisit(rowID, currentPaneIndex, Integer.parseInt(weightTextField.getText()), Integer.parseInt(heightTextField.getText()), Integer.parseInt(systolicBloodPressureTextField.getText()), Integer.parseInt(diastolicBloodPressureTextField.getText()), Integer.parseInt(heartRateTextField.getText()), Integer.parseInt(bodyTemperatureTextField.getText()), notesTextArea.getText());
    }

    public void switchPane(int oldPaneIndex) throws SQLException {
        AnchorPane oldPane = panes[oldPaneIndex];
        oldPane.setDisable(true);
        oldPane.setVisible(false);

        AnchorPane newPane = panes[currentPaneIndex];
        newPane.setDisable(false);
        newPane.setVisible(true);

        if (newPane == vitalsPane) {
            goBackButton.setDisable(true);
        } else {
            goBackButton.setDisable(false);
        }

        if (newPane == notesPane) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }

        loadCurrentPane();
    }

    public boolean validateVitalsPane() {
        TextField[] textFields = new TextField[]{weightTextField, heightTextField, systolicBloodPressureTextField, diastolicBloodPressureTextField, heartRateTextField, bodyTemperatureTextField};
        TextField[] invalidFields = new TextField[textFields.length];
        int invalidFieldIndex = 0;

        for (TextField textField : textFields) {
            if (!Utilities.validate(textField, Utilities.DIGITS_ONLY_REGEX, 1, 3)) {
                invalidFields[invalidFieldIndex++] = textField;
            }
        }

        if (invalidFieldIndex > 0) {
            invalidFields[0].requestFocus();
        }

        return invalidFieldIndex == 0;
    }

    public void loadHealthConditionsPane() throws SQLException {
        ResultSet healthConditions = Database.getHealthConditionsFor(patientID);

        ArrayList<Row> rows = new ArrayList<>();

        while (healthConditions.next()) {
            int rowID = healthConditions.getInt("ID");

            String condition = healthConditions.getString("healthCondition");
            String severity = HealthConditionSeverity.valueOf(healthConditions.getString("severity")).toString();
            String type = HealthConditionType.valueOf(healthConditions.getString("type")).toString();
            String notes = healthConditions.getString("notes");

            ValueLabel conditionLabel = new ValueLabel(condition);
            ValueLabel severityLabel = new ValueLabel(severity);
            ValueLabel typeLabel = new ValueLabel(type);
            ValueLabel notesLabel = new ValueLabel(notes);

            Row row = new Row("healthConditions", rowID, conditionLabel, severityLabel, typeLabel, notesLabel);
            rows.add(row);
        }
        healthConditions.close();

        SelectableTable healthConditionsTable = new SelectableTable();
        healthConditionsTable
            .withRows(rows)
            .build();
        
        healthConditionsScrollPane.setContent(healthConditionsTable);
    }


    public void loadAllergiesPane() throws SQLException {
        ResultSet allergies = Database.getAllergiesFor(patientID);

        ArrayList<Row> rows = new ArrayList<>();

        while (allergies.next()) {
            int rowID = allergies.getInt("ID");

            String condition = allergies.getString("allergen");
            String type = AllergyType.valueOf(allergies.getString("type")).toString();
            String severity = Severity.valueOf(allergies.getString("severity")).toString();
            String notes = allergies.getString("notes");

            ValueLabel conditionLabel = new ValueLabel(condition);
            ValueLabel severityLabel = new ValueLabel(severity);
            ValueLabel typeLabel = new ValueLabel(type.toString());
            ValueLabel notesLabel = new ValueLabel(notes);

            Row row = new Row("allergies", rowID, conditionLabel, severityLabel, typeLabel, notesLabel);
            rows.add(row);
        }
        allergies.close();

        SelectableTable allergiesTable = new SelectableTable();
        allergiesTable
            .withRows(rows)
            .build();
        
        allergiesScrollPane.setContent(allergiesTable);
    }

    public void loadVaccinesPane() throws SQLException {
        ResultSet vaccines = Database.getVaccinesFor(patientID);

        LinkedHashMap<String, Row> vaccineGroups = new LinkedHashMap<String, Row>();

        for (VaccineGroup vaccineGroup : VaccineGroup.values()) {
            Row row = new Row("vaccines");
            row.getStyleClass().add("table-row");
            ValueLabel vaccineGroupLabel = new ValueLabel(vaccineGroup.toString());
            vaccineGroupLabel.getStyleClass().add("table-label");
            
            row.getChildren().addAll(vaccineGroupLabel);
            vaccineGroups.put(vaccineGroup.toString(), row);
        }

        while (vaccines.next()) {
            String vaccineGroup = VaccineGroup.valueOf(vaccines.getString("vaccineGroup")).toString();
            String date = vaccines.getString("date");
            ValueLabel dateLabel = new ValueLabel(date);
            dateLabel.getStyleClass().add("table-label");
            vaccineGroups.get(vaccineGroup).getChildren().add(dateLabel);
        }
        vaccines.close();

        for (Row row : vaccineGroups.values()) {
            while (row.getChildren().size() < 6) {
                ValueLabel NALabel = new ValueLabel("N/A");
                NALabel.getStyleClass().add("table-label");
                row.getChildren().add(NALabel);
            }
        }

        ArrayList<Row> rows = new ArrayList<Row>(vaccineGroups.values());

        SelectableTable vaccinesTable = new SelectableTable();
        vaccinesTable
            .withRows(rows)
            .build();

        vaccinesScrollPane.setContent(vaccinesTable);
    }


    public void loadCurrentPane() throws SQLException {
        switch (currentPaneIndex) {
            case 0:
            case 4:
                break;
            case 1:
                loadHealthConditionsPane();
                break;
            case 2:
                loadAllergiesPane();
                break;
            case 3:
                loadVaccinesPane();
                break;
        }
    }

    public void closeAndNullify() throws SQLException {
        instance = null;
        close();

        Database.updateActiveVisitCurrentPage(rowID, currentPaneIndex);
    }
}
