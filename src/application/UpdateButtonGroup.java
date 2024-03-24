package application;

import java.util.ArrayList;

import javafx.scene.control.Button;

public class UpdateButtonGroup {
    public Button editButton;
    public Button cancelButton;
    public Button saveButton;
    public ArrayList<Value> values;

    public UpdateButtonGroup(Button editButton, Button cancelButton, Button saveButton) {
        this.editButton = editButton;
        this.cancelButton = cancelButton;
        this.saveButton = saveButton;
        this.values = new ArrayList<Value>();

        editButton.setOnAction(e -> {
            for (Value value : values) {
                value.onEdit();
            }
        });

        cancelButton.setOnAction(e -> {
            for (Value value : values) {
                value.onCancel();
            }
        });

        saveButton.setOnAction(e -> {
            for (Value value : values) {
                try {
                    value.onSave();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });
    }
}