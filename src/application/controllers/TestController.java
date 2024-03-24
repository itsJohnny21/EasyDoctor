package application.controllers;

import java.util.ArrayList;

import application.Database;
import application.Database.Table.Employee;
import application.Database.Table.Patient;
import application.UpdateButtonGroup;
import application.ValueOption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TestController extends Controller {
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button editButton;
    @FXML public Button saveButton;
    @FXML public ScrollPane scrollPane;
    @FXML public GridPane contentPane;
    
    public String title = "Test";

    public void initialize() throws Exception {
        rootPane.getStylesheets().add(getClass().getResource("/application/styles/test.css").toExternalForm());
        Database.connectAs(Database.Role.NEUTRAL);
        Database.signIn("john123", "123");

        UpdateButtonGroup ubg = new UpdateButtonGroup(editButton, cancelButton, saveButton);

        // Table allergiesTable = UI.allergiesTableFor(2)
        //     .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
        //     .withRowHeight(50)
        //     .withTitle("Allergies")
        //     .withHeader("Allergen", "Severity", "Common Source", "Notes")
        //     .withRowAction((row) -> {
        //         row.setOnMouseClicked(e -> {
        //             ValueLabel value = (ValueLabel) row.getChildren().get(0);
        //             System.out.println(value.datum.parent.rowID);
        //         });
        //     })
        //     .build();

        // contentPane.add(allergiesTable, 0, 0);

        // Form contactInformationForm = UI.contactInformationFormFor(2)
        //     .withWidth(0.5 * Screen.getPrimary().getVisualBounds().getWidth())
        //     .withRowHeight(50)
        //     .withTitle("Contact Information")
        //     .connectedTo(ubg)
        //     .build();

        // contentPane.add(contactInformationForm, 1, 0);

        Patient p = Database.Table.Patient.getFor(2);
        Employee e = Database.Table.Employee.getFor(3);
        ArrayList<String> doctors = Database.Table.Employee.getDoctors(); //! get ArrayList<Employee> doctors instead

        p.preferredDoctorID.newValue = e.firstName.originalValue + " " + e.lastName.originalValue;
        ValueOption option = p.preferredDoctorID
            .createValueOption()
            // .withConversion((newValue) -> {
            //     // Database.getDoctorIDFor()
            // })
            .connectedTo(ubg);

        for (String doctor : doctors) {
            option.getItems().add(doctor); //! Add Datum instead of String
        }
        option.setValue(option.getItems().get(0));

        option.setTranslateX(700);
        contentPane.add(option, 0, 1);
        
    }

    public String getTitle() {
        return title;
    }

}
