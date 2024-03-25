package application;

import java.sql.SQLException;

import application.UI.EditableTable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class ValueRow extends HBox implements Value { //! should have a variable called Row row instead
    public RadioButton radioButton;
    public EditableTable parent;

    public ValueRow(EditableTable parent) {
        super();
        this.parent = parent;
    }

    public void makeDeletable(UpdateButtonGroup updateButtonGroup) {
        radioButton = new RadioButton();

        radioButton.setOnAction(event -> {
            if (radioButton.isSelected()) {
                System.out.println("Deleting!");
                parent.deleteRow(this);
            }
        });

        radioButton.setDisable(true);
        getChildren().add(radioButton);
        connectedTo(updateButtonGroup);
    }
    
    public void onEdit() {
        if (radioButton != null) {
            radioButton.setDisable(false);
        }
    }

    public void onSave() throws SQLException {
        if (radioButton.isSelected()) {

            for (Node node : getChildren()) {
                if (node instanceof ValueLabel) {
                    ValueLabel valueLabel = (ValueLabel) node;
                    Datum datum = valueLabel.datum;
                    System.out.println("deleting row " + datum.parent.rowID + " from " + datum.parent.tableName);
                    break;
                }
            }

            radioButton.setSelected(false);
        }
        
        radioButton.setDisable(true);
    }

    public void onCancel() {
        if (radioButton != null) {
            radioButton.setSelected(false);
            radioButton.setDisable(true);
        }
    }

    public void onError() {
    }

    public Value connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.values.add(this);
        return this;
    }
    
}
