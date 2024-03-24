package application.controllers;

import application.Database;
import application.Database.Table.Employee;
import application.Database.Table.Patient;
import application.Datum;
import application.UI;
import application.UI.Form;
import application.UI.Table;
import application.UpdateButtonGroup;
import application.ValueLabel;
import application.ValueOption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.util.StringConverter;

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

        Table allergiesTable = UI.allergiesTableFor(2)
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Allergies")
            .withHeader("Allergen", "Severity", "Common Source", "Notes")
            .withRowAction((row) -> {
             row.setOnMouseClicked(e -> {
                    ValueLabel value = (ValueLabel) row.getChildren().get(0);
                    System.out.println(value.datum.parent.rowID);
                });
            })
            .build();

        contentPane.add(allergiesTable, 0, 0);

        Form contactInformationForm = UI.contactInformationFormFor(2)
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Contact Information")
            .connectedTo(ubg)
            .withColumnCount(2)
            .build();

        contentPane.add(contactInformationForm, 0, 1);

        Patient p = Database.Table.Patient.getFor(2);
        ValueOption option = p.preferredDoctorID.createValueOption()
        .connectedTo(ubg);
        
        option.setConverter(new StringConverter<Datum>() {
            @Override
            public String toString(Datum datum) {
                try {
                    Employee doctor = Database.Table.Employee.getFor(Integer.parseInt(datum.originalValue));
                    datum.newValue = doctor.firstName.newValue + " " + doctor.lastName.newValue;
                    return datum.newValue;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
            
            @Override
            public Datum fromString(String string) {
                return null;
            }
        });

        option.setValue(p.preferredDoctorID);
        System.out.println(p.preferredDoctorID.originalValue);
        option.setPrefWidth(200);

        option.setTranslateX(700);
        option.setTranslateY(400);
        contentPane.add(option, 0, 1);
        
    }

    public String getTitle() {
        return title;
    }

}
