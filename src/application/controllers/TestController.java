package application.controllers;

import application.Database;
import application.Database.Role;
import application.UI;
import application.UI.InformationForm;
import application.UI.View;
import application.UpdateButtonGroup;
import application.ValueField;
import application.ValueLabel;
import application.ValueOption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

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

        View allergiesTable = UI.allergiesTableBaseFor(2)
            .withCustomHeader("Allergen", "Severity", "Common Source", "Notes")
            .withSelectAction(row -> {
                row.setOnMouseClicked(e -> {
                    ValueLabel value = (ValueLabel) row.getChildren().get(0);
                    System.out.println(value.datum.parent.rowID);
                });
            })
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Allergies")
            .connectedTo(ubg)
            .build();
        
        contentPane.getChildren().add(allergiesTable);
        
        View contactInformationForm = UI.contactInformationFormBaseFor(2)
            .withColumnCount(2)
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Contact Information")
            .connectedTo(ubg)
            .build();

        contentPane.getChildren().add(contactInformationForm);

        View surgeriesTable = UI.surgeriesTableBaseFor(2)
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Surgeries")
            .connectedTo(ubg)
            .build();

        contentPane.getChildren().add(surgeriesTable);

    // INSERT INTO users (username, password, role)
    // VALUES ('barb123', SHA2('123', 256), 'PATIENT');
    // SET @userID = LAST_INSERT_ID();
    // INSERT INTO patients (userID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
    // VALUES (@userID, 'Barbara', 'Williams',  'FEMALE', '2000-01-01', 'barb123@gmail.com', '1234567890', '123 Test St', 'WHITE', 'NON-HISPANIC');
        ValueField usernameField = new ValueField().withLabel("username");
        ValueField passwordField = new ValueField().withLabel("password");
        ValueOption roleField = Role.createValueOption().withLabel("role");
        
        Button signUpButton = new Button("Insert User");
        View signUpForm = new InformationForm()
            .withColumnCount(2)
            .withValues(
                usernameField,
                passwordField,
                roleField
            )
            .withTitle("Sign Up")
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .build();
            
            contentPane.getChildren().add(signUpForm);
            
            contentPane.getChildren().add(signUpButton);
            signUpButton.setOnAction(e -> {
                try {
                Database.connectAs(Role.NEUTRAL);
                Database.insertUser(usernameField.getText(), passwordField.getText(), Role.valueOf(roleField.getValue().originalValue));
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }

    public String getTitle() {
        return title;
    }

}
