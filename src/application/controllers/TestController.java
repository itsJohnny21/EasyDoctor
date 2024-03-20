package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import application.Database;
import application.Row.HealthCondition;
import application.Row.Surgery;
import application.Row.ValueField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TestController extends Controller {

    @FXML AnchorPane mainPane;
    @FXML Button editButton;
    @FXML Button cancelButton;
    @FXML Button saveButton;

    public void initialize() throws Exception {
        System.out.println("TestController loaded");
        title = "Test";

        windowMinWidth = mainPane.getPrefWidth();
        windowMinHeight = mainPane.getPrefHeight();
        windowMaxWidth = windowMinWidth;
        windowMaxHeight = windowMinHeight;
        resizeable = false;

        UpdateButtonGroup ubg = new UpdateButtonGroup(editButton, cancelButton, saveButton);

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(300);
        mainPane.getChildren().add(gridPane);
        
        Database.connectAs(Database.Role.DOCTOR);
        PreparedStatement preparedStatement = Database.connection.prepareStatement("SELECT * FROM surgeries WHERE ID = ?");
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        
        Surgery surgery = new Surgery(resultSet);
        HealthCondition healthConditions = HealthCondition.getForPatient(2);
        ArrayList<HealthCondition> healthConditionsArray = HealthCondition.getAllForPatient(2);

        HashMap<String, Boolean> permissions = Database.getUpdatePermissionsFor(Database.role);


        for (HealthCondition healthCondition : healthConditionsArray) {
            gridPane.add(healthCondition.healthCondition.createField(permissions.get(healthCondition.healthCondition.columnName)).connectedTo(ubg), 0, gridPane.getChildren().size());
            gridPane.add(healthCondition.notes.createField(permissions.get(healthCondition.patientID.columnName)).connectedTo(ubg), 0, gridPane.getChildren().size());
            gridPane.add(healthCondition.severity.createField(true).connectedTo(ubg), 0, gridPane.getChildren().size());
            gridPane.add(healthCondition.creationTime.createField(true).connectedTo(ubg), 0, gridPane.getChildren().size());
        }

        // ValueField creationTimeField = surgery.creationTime.createField(true);
        // gridPane.add(creationTimeField, 0, gridPane.getChildren().size());
        // gridPane.add(surgery.doctorID.createField(true), 0, gridPane.getChildren().size());
        // gridPane.add(surgery.location.createField(true), 0, gridPane.getChildren().size());
        ValueField idk = healthConditions.severity.createField(true).connectedTo(ubg);
        gridPane.add(surgery.location.connectedTo(ubg), 0, gridPane.getChildren().size());
        gridPane.add(idk, 0, gridPane.getChildren().size());
        addToGridPane(gridPane, surgery.notes);
        















        PreparedStatement preparedStatement2 = Database.connection.prepareStatement("SELECT * FROM healthConditions WHERE ID = 1");
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        resultSet2.next();

        // HealthCondition healthCondition = new HealthCondition(resultSet2);

        // ValueField healthConditionField = new ValueField(healthCondition.healthCondition, true);
        // gridPane.add(healthConditionField, 0, 1);

        // ValueField healthConditionField2 = new ValueField(healthCondition.notes, false);
        // gridPane.add(healthConditionField2, 0, 2);

        gridPane.add(ubg, 0, gridPane.getChildren().size());
        editButton.fire();
    }

    public String getTitle() {
        return title;
    }

    public void addToGridPane(GridPane gridPane, application.Row.Datum datum) {
        gridPane.add(datum.createField(true), 0, gridPane.getChildren().size());
    }

    public static class CancelButton extends Button {
        public HashMap<String, ValueField> fields;
        public CancelButton() {
            setText("Cancel");

            setOnAction(e -> {
                for (ValueField field : fields.values()) {
                    field.onCancel();
                }
            });
        }
    }

    public static class UpdateButtonGroup extends GridPane {
        public Button editButton;
        public Button cancelButton;
        public Button saveButton;
        public ArrayList<ValueField> fields;

        public UpdateButtonGroup(Button editButton, Button cancelButton, Button saveButton) {
            this.editButton = editButton;
            this.cancelButton = cancelButton;
            this.saveButton = saveButton;
            this.fields = new ArrayList<ValueField>();

            add(editButton, 0, 0);
            add(cancelButton, 1, 0);
            add(saveButton, 2, 0);

            editButton.setOnAction(e -> {
                for (ValueField field : fields) {
                    field.onEdit();
                }
            });

            cancelButton.setOnAction(e -> {
                for (ValueField field : fields) {
                    field.onCancel();
                }
            });

            saveButton.setOnAction(e -> {
                for (ValueField field : fields) {
                    try {
                         field.onSave();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }
}