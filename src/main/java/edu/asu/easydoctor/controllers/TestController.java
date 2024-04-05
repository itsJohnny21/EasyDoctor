package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.asu.easydoctor.Connectable;
import edu.asu.easydoctor.DataRow;
import edu.asu.easydoctor.DataRow.Employee;
import edu.asu.easydoctor.DataRow.Patient;
import edu.asu.easydoctor.DataRow.Surgery;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.Datum;
import edu.asu.easydoctor.EditableTable;
import edu.asu.easydoctor.Form;
import edu.asu.easydoctor.Row;
import edu.asu.easydoctor.SelectableTable;
import edu.asu.easydoctor.UI.Table;
import edu.asu.easydoctor.UpdateButtonGroup;
import edu.asu.easydoctor.ValueField;
import edu.asu.easydoctor.ValueOption;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class TestController extends Controller {
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button editButton;
    @FXML public Button saveButton;
    @FXML public ScrollPane scrollPane;
    @FXML public VBox contentPane;
    
    public String title = "Test";

    public void initialize() throws Exception {
        rootPane.getStylesheets().add(getClass().getResource("/application/styles/test.css").toExternalForm());
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight();
        resizable = true;
        
        Database.changeRole(null);
        Database.signIn("john123", "123");

        UpdateButtonGroup ubg = new UpdateButtonGroup(editButton, cancelButton, saveButton);
        
        ArrayList<Surgery> surgeries = DataRow.Surgery.getAllFor(2);
        Row[] rows = new Row[surgeries.size()];
        for (int i = 0; i < surgeries.size(); i++) {
            Surgery surgery = surgeries.get(i);
            Employee doctor = DataRow.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.displayValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
            
            rows[i] = new Row(
                surgery.tableName,
                surgery.rowID,
                doctor.userID.createValueLabel(),
                surgery.type.createValueLabel(),
                surgery.date.createValueLabel(),
                surgery.notes.createValueLabel()
            );
        }

        Row[] rows2 = new Row[surgeries.size()];
        for (int i = 0; i < surgeries.size(); i++) {
            Surgery surgery = surgeries.get(i);
            Employee doctor = DataRow.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.displayValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
            
            rows2[i] = new Row(
                surgery.tableName,
                surgery.rowID,
                doctor.userID.createValueField(""),
                surgery.type.createValueField(""),
                surgery.date.createValueField(""),
                surgery.notes.createValueField("")
            );
        }

        SelectableTable table1 = new SelectableTable();
        table1
            .withRowAction(row -> {
                row.setOnMouseClicked(event -> {
                    System.out.printf("Row %d from %s clicked\n", row.rowID, row.tableName);
                });
            })
            .withTitle("Table 1")
            .withHeader("Surgeon", "Type", "Date", "Notes")
            .withRows(rows)
            .build();
        contentPane.getChildren().add(table1);

        EditableTable table2 = new EditableTable();
        table2
            .connectedTo(ubg)
            .withDeleteAction(row -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle(String.format("Delete from %s", row.tableName));
                alert.setHeaderText(String.format("Are you sure you want to delete this row from %s?", row.tableName));
                alert.setContentText("This action cannot be undone.");
                if (alert.showAndWait().get().getText().equals("OK")) {
                    try {
                        Database.deleteRow(row.tableName, row.rowID);

                        for (Table table : new Table[] {table1, table2}) {
                            Iterator<Row> iterator = table.rows.iterator();
                            while (iterator.hasNext()) {
                                Row iteratedRow = iterator.next();
                                if (iteratedRow.rowID == row.rowID) {
                                    iterator.remove();
                                    table.getChildren().remove(iteratedRow);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("An error occurred while deleting the row.");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
            })
            .withTitle("Table 2")
            .withHeader("Surgeon", "Type", "Date", "Notes")
            .withRows(rows2)
            .build();
        contentPane.getChildren().add(table2);

        Patient patient = DataRow.Patient.getFor(2);

        Form form1 = new Form()
            .withTitle("Patient Information")
            .connectedTo(ubg)
            .withColumnCount(2)
            .withFields(
                patient.firstName.createValueField("First Name"),
                patient.lastName.createValueField("Last Name"),
                Datum.createValueOptionForDoctors(patient.preferredDoctorID, "Preferred Doctor"),
                Datum.createValueOptionFromEnum(Database.BloodType.class, patient.bloodType, "Blood Type"),
                patient.email.createValueField("Email"),
                patient.phone.createValueField("Phone"),
                patient.address.createValueField("Address"),
                Datum.createValueOptionFromEnum(Database.Sex.class, patient.sex, "Sex")
            )
            .build();

        contentPane.getChildren().add(form1);

        Form form2 = new Form()
            .withTitle("Sign Up")
            .withSubmitAction(form -> {
                HashMap<String, String> values = new HashMap<>();

                for (Connectable field : form.fields) {
                    if (field instanceof ValueField) {
                        ValueField valueField = (ValueField) field;
                        values.put(valueField.label, valueField.getText());
                    } else if (field instanceof ValueOption) {
                        ValueOption valueOption = (ValueOption) field;
                        values.put(valueOption.label, valueOption.getValue().originalValue);
                    }
                }

                try {
                    Database.changeRole(null);
                    Database.insertEmployee(values.get("Username"), values.get("Password"), Database.Role.valueOf(values.get("Role")), "Jasmine", "Salazar", Sex.FEMALE, "1999-02-21 00:00:00", "idk", "1234567890", "idk", "2", "password", Race.WHITE, Ethnicity.HISPANIC);
                    System.out.printf("%s with username %s inserted\n", values.get("Role"), values.get("Username"));
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while signing up.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            })
            .withColumnCount(2)
            .withFields(
                new ValueField("Username"),
                new ValueField("Password"),
                Datum.createValueOptionFromEnum(Database.Role.class, null, "Role")
            )
            .build();

        contentPane.getChildren().add(form2);

        rootPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.R && event.isMetaDown()) {
				try {
                    refresh();
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while refreshing the page.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

    }

    public void refresh() throws Exception {
        System.out.println("Refreshing...");
        contentPane.getChildren().clear();
        initialize();
    }

    public String getTitle() {
        return title;
    }
}
