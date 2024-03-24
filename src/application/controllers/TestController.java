package application.controllers;

import application.Database;
import application.Database.Ethnicity;
import application.Database.Race;
import application.Database.Role;
import application.Database.Sex;
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
        // Employee e = Database.Table.Employee.getFor(3);
        
        Database.connectAs(Role.DOCTOR);
        
        Database.insertEmployee("bobby", "123", Role.DOCTOR, "Bobby", "Fridgle", Sex.MALE, "1999-02-21", "idk", "123", "123 Main St", null);
        Employee bobby = Database.Table.Employee.getFor(31);
        Database.insertPatient("johnny", "123", "Johnny", "Salazar", Sex.MALE, "1999-02-21", "jsalaz59@asu.edu", "1233210123", "123 Main St", Race.HISPANIC, Ethnicity.HISPANIC);
        // Database.Table.User user = Database.Table.User.getFor(29);
        // System.out.println(user.username.originalValue);
        
        // INSERT INTO users (username, password, role)
        // VALUES ('peter', SHA2('123', 256), 'DOCTOR');
        // SET @userID = LAST_INSERT_ID();
        // INSERT INTO employees (userID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID)
        // VALUES (@userID, 'Peter', 'Davidson', 'MALE', '1979-12-31', CURRENT_TIMESTAMP, 'peterdavidson123@gmail.com', '1234567890', '123 Main St', NULL);
        // String[] columns = new String[] {"userID", "firstName", "lastName", "sex", "birthDate", "hireDate", "email", "phone", "address", "managerID"};

        // String[] c = new String[] {"username", "password", "role"};
        // String[] v = new String[] {"johnny", "123", "DOCTOR"};

        // Database.insertNewDoctor("peter321", "123", "Peter", "Davidson", "MALE", "1979-12-31", "CURRENT_TIMESTAMP", "1979-12-31", "CURRENT_TIMESTAMP", "peterdavidson123@gmail.com", "1234567890", "123 Main St", "NULL");

        
        // Database.insertRow("employees", null);
        // String[] values = new String[] {String.valueOf(userID), "Peter", "Davidson", "MALE", "1979-12-31", "CURRENT_TIMESTAMP", "1979-12-31", "CURRENT_TIMESTAMP", "peterdavidson123@gmail.com", "1234567890", "123 Main St", "NULL"};



        // ArrayList<Employee> doctors = Database.Table.Employee.getAllDoctors();
        // ArrayList<String> doctorNames = new ArrayList<String>();

        // for (Employee doctor : doctors) {
        //     doctorNames.add(doctor.firstName + " " + doctor.lastName);
        // }

        // p.preferredDoctorID.newValue = e.userID.originalValue;
        ValueOption option = p.preferredDoctorID
            .createValueOption()
            // .withConversion((newValue) -> {
            //     // Database.getDoctorIDFor()
            // })
            .connectedTo(ubg);

        // for (String doctorName : doctorNames) {
        //     option.getItems().add(doctorName); //! Add Datum instead of String
        // }
        // option.setValue(option.getItems().get(0));

        option.setTranslateX(700);
        contentPane.add(option, 0, 1);
        
    }

    public String getTitle() {
        return title;
    }

}
