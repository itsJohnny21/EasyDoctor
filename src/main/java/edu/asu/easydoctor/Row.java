package edu.asu.easydoctor;

import java.sql.SQLException;
import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Row extends HBox implements Connectable {
    public String tableName;
    public int rowID;
    public Button deleteButton;

    public Row(String tableName) {
        this.tableName = tableName;
    }

    public Row(String tableName, int rowID, Connectable... values) {
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

    public void makeDeletable(Consumer<Row> deleteAction) {
        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        getChildren().add(deleteButton);
        
        deleteButton.setOnAction(event -> {
            deleteAction.accept(this);
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
