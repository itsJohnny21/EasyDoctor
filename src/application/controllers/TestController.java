package application.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import application.Database;
import application.Database.Row.Employee;
import application.Database.Row.Patient;
import application.Database.Row.Surgery;
import application.Datum;
import application.Row2;
import application.UI2.Form;
import application.UI2.Table;
import application.UI2.Table.EditableTable;
import application.UI2.Table.SelectableTable;
import application.UpdateButtonGroup;
import application.ValueField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
        Database.connectAs(Database.Role.NEUTRAL);
        Database.signIn("john123", "123");

        UpdateButtonGroup ubg = new UpdateButtonGroup(editButton, cancelButton, saveButton);
        
        ArrayList<Surgery> surgeries = Database.Row.Surgery.getAllFor(2);
        Row2[] rows = new Row2[surgeries.size()];
        for (int i = 0; i < surgeries.size(); i++) {
            Surgery surgery = surgeries.get(i);
            Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
            
            rows[i] = new Row2(
                surgery.tableName,
                surgery.rowID,
                doctor.userID.createValueLabel(),
                surgery.type.createValueLabel(),
                surgery.date.createValueLabel(),
                surgery.notes.createValueLabel()
            );
        }

        Row2[] rows2 = new Row2[surgeries.size()];
        for (int i = 0; i < surgeries.size(); i++) {
            Surgery surgery = surgeries.get(i);
            Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
            
            rows2[i] = new Row2(
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
                    System.out.printf("Row %d clicked\n", row.rowID);
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
                            Iterator<Row2> iterator = table.rows.iterator();
                            while (iterator.hasNext()) {
                                Row2 iteratedRow = iterator.next();
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

        Patient patient = Database.Row.Patient.getFor(2);

        Form form1 = new Form()
            .withTitle("Patient Information")
            .connectedTo(ubg)
            .withColumnCount(2)
            .withFields(
                patient.firstName.createValueField("First Name"),
                patient.lastName.createValueField("Last Name"),
                patient.preferredDoctorID.createValueOption("Preferred Doctor")
                    .withData(Datum.getOptionsForDoctors(patient.preferredDoctorID))
                    .withConverter(new StringConverter<Datum>() {
                        @Override
                        public String toString(Datum datum) {
                            try {
                                Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(datum.originalValue));
                                return doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
                            } catch (Exception e) {
                                return "";
                            }
                        }

                        @Override
                        public Datum fromString(String string) {
                            return null;
                        }
                    }),
                Database.BloodType.createValueOption(patient.bloodType, "Blood Type"),
                patient.email.createValueField("Email"),
                patient.phone.createValueField("Phone"),
                patient.address.createValueField("Address"),
                Database.Sex.createValueOption(patient.sex, "Sex")
            )
            .build();

        contentPane.getChildren().add(form1);

        Form form2 = new Form()
            .withTitle("Sign Up")
            .isSubmittable(true)
            .withColumnCount(2)
            .withFields(
                new ValueField("Username"),
                new ValueField("Password"),
                Database.Role.createValueOption("Role"),
                Database.Sex.createValueOption("Sex")
            )
            .build();

        contentPane.getChildren().add(form2);
    }
}
