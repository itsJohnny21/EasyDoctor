package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.asu.easydoctor.DataRow;
import edu.asu.easydoctor.DataRow.Drug;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Units;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.ValueLabel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddPrescriptionController extends DialogController {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button doneButton;
    @FXML public ScrollPane drugsScrollPane;

    @FXML public TextField drugTextField;
    @FXML public TextField quantityTextField;
    @FXML public ChoiceBox<String> unitsChoiceBox;
    @FXML public ChoiceBox<String> intakeDayChoiceBox;
    @FXML public ChoiceBox<String> intakeTimeChoiceBox;
    @FXML public TextArea specialInstructionsTextArea;

    public static AddPrescriptionController instance = null;
    public final static String TITLE = "Find Drug";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "AddPrescriptionView";
    public final static String STYLE_FILENAME = "PatientPortalView";
    public HashSet<String> drugsSet = new HashSet<>();
    public Integer drugID;

    private AddPrescriptionController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static AddPrescriptionController getInstance() {
        if (instance == null) {
            instance = new AddPrescriptionController();
        }

        return instance;
    }

    @FXML public void initialize() throws SQLException {

        for (Units e : Units.values()) {
            unitsChoiceBox.getItems().add(e.toString());
        }
        unitsChoiceBox.setValue(null);
        
        for (DayOfWeek e : DayOfWeek.values()) {
            intakeDayChoiceBox.getItems().add(e.toString());
        }
        intakeDayChoiceBox.setValue(null);

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                LocalTime localTime = LocalTime.of(i, j);
                String time = Utilities.prettyTime(localTime);
                intakeTimeChoiceBox.getItems().add(time);
            }
        }
        intakeTimeChoiceBox.setValue(null);

        for (Node node : new Node[] {drugTextField, quantityTextField, unitsChoiceBox, intakeDayChoiceBox, intakeTimeChoiceBox}) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != oldValue) {
                        validateFields();
                    }
                    textField.requestFocus(); //! when validating fields, the focus is lost
                });
            } else if (node instanceof ChoiceBox) {
                ChoiceBox<String> choiceBox = (ChoiceBox<String>) node;
                choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != oldValue) {
                        validateFields();
                    }
                    choiceBox.requestFocus();
                });
            }
        }

        ArrayList<Drug> drugs = DataRow.Drug.getAll();
        ArrayList<Row> rows = new ArrayList<>();

        for (Drug drug : drugs) {
            ValueLabel name = new ValueLabel(drug.name);
            ValueLabel manufacturer = new ValueLabel(drug.manufacturer);
            ValueLabel shelfLife = new ValueLabel(drug.shelfLife);
            ValueLabel instructions = new ValueLabel(drug.instructions);
            ValueLabel description = new ValueLabel(drug.description);
            ValueLabel sideEffects = new ValueLabel(drug.sideEffects);
            ValueLabel storageInformation = new ValueLabel(drug.storageInformation);
            ValueLabel activeIngredients = new ValueLabel(drug.activeIngredients);

            Row row = new Row(drug.tableName, drug.rowID, name, manufacturer, shelfLife, instructions, description, sideEffects, storageInformation, activeIngredients);
            rows.add(row);

            drugsSet.add(drug.name.originalValue);
        }

        SelectableTable drugsTable = new SelectableTable();
        drugsTable
            .withRowAction(row -> {
                ValueLabel nameValueLabel = (ValueLabel) row.getChildren().get(0);
                drugTextField.setText(nameValueLabel.datum.originalValue);
                drugID = row.rowID;
            })
            .withTitle("Drugs")
            .withHeader("Name", "Manufacturer", "Shelf Life", "Instructions", "Description", "Side Effects", "Storage Information", "Active Ingredients")
            .withRows(rows)
            .build();

            drugsScrollPane.setContent(drugsTable);
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {}

    @FXML public void handleCancelButtonAction (ActionEvent event) {
        closeAndNullify();
    }

    @FXML public void handleDoneButtonAction(ActionEvent event) throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Add Prescription");
        alert.setHeaderText("Are you sure you want to add this prescription for %s?".formatted(Database.getPatientNameFor(WorkPortalController.getInstance().prescriptionToolPatientID)));
        alert.showAndWait();

        if (alert.getResult().getButtonData().isCancelButton()) return;

        String quantity = quantityTextField.getText();
        String units = unitsChoiceBox.getValue();
        String intakeDay = intakeDayChoiceBox.getValue();
        String intakeTime = Utilities.parsePrettyTime(intakeTimeChoiceBox.getValue()).toString();
        String specialInstructions = specialInstructionsTextArea.getText() == null ? "" : specialInstructionsTextArea.getText();

        try {
            Database.insertPrescriptionFor(WorkPortalController.getInstance().prescriptionToolPatientID, Database.getMyID(), drugID, intakeDay, intakeTime, quantity, units, specialInstructions);
        } catch (SQLException e) {
            WorkPortalController.showDefaultErrorAlert(e);
            return;
        }

        closeAndNullify();
    }

    public boolean validateFields() {
        boolean valid = true;

        valid = Utilities.validate(drugTextField, Utilities.MESSAGE_REGEX) //! Remove adding "error" to the style class
            & drugsSet.contains(drugTextField.getText())
            & Utilities.validate(quantityTextField, Utilities.DIGITS_ONLY_REGEX)
            & Utilities.validate(unitsChoiceBox)
            & Utilities.validate(intakeDayChoiceBox)
            & Utilities.validate(intakeTimeChoiceBox);

        if (valid) {
            doneButton.setDisable(false);
        } else {
            doneButton.setDisable(true);
        }

        return valid;
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}