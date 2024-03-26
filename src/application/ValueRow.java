package application;

import java.sql.SQLException;

import application.UI.EditableTable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ValueRow extends HBox implements Value { //! should have a variable called Row row instead
    public Button deleteButton;
    public EditableTable parent;

    public ValueRow(EditableTable parent) {
        super();
        this.parent = parent;
    }

    public void makeDeletable() {
        deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(parent.width / parent.header.getChildren().size());
        deleteButton.setDisable(true);
        getChildren().add(deleteButton);
        ValueField valueField = (ValueField) getChildren().get(0);
        
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle(String.format("Delete from %s", valueField.datum.parent.tableName));
            alert.setHeaderText(String.format("Are you sure you want to delete this row from %s?", valueField.datum.parent.tableName));
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get().getText().equals("OK")) {
                try {
                    Database.deleteRow(valueField.datum.parent.tableName, valueField.datum.parent.rowID);
                    parent.deleteRow(this);
                } catch (SQLException e) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while deleting the row.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }

        });
    }

    public void onEdit() {
        ValueField valueField = (ValueField) getChildren().get(0);

        if (Database.canDelete(valueField.datum.parent.tableName)) {
            deleteButton.setDisable(false);
        } else {
            deleteButton.setDisable(true);
        }
    }

    public void onSave() throws SQLException {
        deleteButton.setDisable(true);
    }

    public void onCancel() {
        deleteButton.setDisable(true);
    }

    public void onError() {
        deleteButton.setDisable(true);
    }

    @Override
    public Value connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.values.add(this);
        return this;
    }
}
