package application;

import java.sql.SQLException;

import application.UI2.Table.EditableTable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Row2 extends HBox implements Connectable {
    public String tableName;
    public int rowID;
    public Button deleteButton;

    public Row2(String tableName, int rowID, Connectable... values) {
        this.tableName = tableName;
        this.rowID = rowID;

        for (Connectable value : values) {
            if (value instanceof ValueField) {
                ValueField field = (ValueField) value;
                field.getStyleClass().add("table-field");
                getChildren().add(field);
                
            } else if (value instanceof ValueLabel) {
                ValueLabel label = (ValueLabel) value;
                label.getStyleClass().add("table-label");
                getChildren().add(label);
                
            } else if (value instanceof ValueOption) {
                ValueOption option = (ValueOption) value;
                option.getStyleClass().add("table-option");
                getChildren().add(option);
            }
        }
    }

    public void makeDeletable() {
        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        getChildren().add(deleteButton);
        
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle(String.format("Delete from %s", tableName));
            alert.setHeaderText(String.format("Are you sure you want to delete this row from %s?", tableName));
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get().getText().equals("OK")) {
                try {
                    Database.deleteRow(tableName, rowID);
                    EditableTable parent = ((EditableTable) getParent());
                    parent.getChildren().remove(this);
                    //! Handle other tables with the same rowID
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
        if (Database.canDelete(tableName)) {
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

    public String toString() {
        return String.format("Row %d from %s", rowID, tableName);
    }

    public void initialize() {
    }
}
