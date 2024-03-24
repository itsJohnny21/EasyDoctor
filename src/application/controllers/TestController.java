package application.controllers;

import application.Database;
import application.UI;
import application.UI.View;
import application.UpdateButtonGroup;
import application.ValueLabel;
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
            .withRowAction(row -> {
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
            .withRowAction(row -> {
                row.setOnMouseClicked(e -> {
                    ValueLabel value = (ValueLabel) row.getChildren().get(0);
                    System.out.println(value.datum.parent.rowID);
                });
            })
            .withWidth(Screen.getPrimary().getVisualBounds().getWidth())
            .withRowHeight(50)
            .withTitle("Surgeries")
            .connectedTo(ubg)
            .build();

        contentPane.getChildren().add(surgeriesTable);
    }

    public String getTitle() {
        return title;
    }

}
