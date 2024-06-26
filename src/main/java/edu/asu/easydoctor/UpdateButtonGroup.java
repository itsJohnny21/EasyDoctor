package edu.asu.easydoctor;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class UpdateButtonGroup {
    public Button editButton;
    public Button addButton;
    public Button cancelButton;
    public Button saveButton;
    public ArrayList<Connectable> connections;

    public UpdateButtonGroup(Button editButton, Button cancelButton, Button saveButton) {
        this.editButton = editButton;
        this.cancelButton = cancelButton;
        this.saveButton = saveButton;
        this.connections = new ArrayList<Connectable>();

        this.editButton.setDisable(false);
        this.cancelButton.setDisable(true);
        this.saveButton.setDisable(true);

        editButton.setOnAction(event -> {
            editButton.setDisable(true);
            cancelButton.setDisable(false);
            saveButton.setDisable(false);

            if (addButton != null) {
                addButton.setDisable(false);
            }

            for (Connectable connection : connections) {
                connection.onEdit();
            }
        });

        cancelButton.setOnAction(event -> {
            cancelButton.setDisable(true);
            editButton.setDisable(false);
            saveButton.setDisable(true);

            if (addButton != null) {
                addButton.setDisable(true);
            }

            for (Connectable connection : connections) {
                connection.onCancel();
            }


        });

        saveButton.setOnAction(event -> {
            boolean error = false;

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText("Are you sure you want to save the changes?");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get().getText().equals("Cancel")) {
                return;
            }

            for (Connectable connection : connections) {
                try {
                    connection.onSave();
                } catch (SQLException exc) {
                    connection.onError();
                    error = true;

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while saving the changes.");
                    alert.setContentText(exc.getMessage());
                    alert.showAndWait();
                }
            }
            
            saveButton.setDisable(true);
            editButton.setDisable(false);
            cancelButton.setDisable(true);

            if (addButton != null) {
                addButton.setDisable(true);
            }

            if (error) {
                editButton.fire();
            }
        });
    }

    public void addAddButton(Button addButton, EventHandler<ActionEvent> event) {
        this.addButton = addButton;
        addButton.setOnAction(event);
    }

    public void addConnection(Connectable connection) {
        connection.initialize();
        connections.add(connection);
    }
}