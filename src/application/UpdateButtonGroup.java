package application;

import java.util.ArrayList;

import javafx.scene.control.Button;

public class UpdateButtonGroup {
    public Button editButton;
    public Button cancelButton;
    public Button saveButton;
    public ArrayList<ValueField> fields;
    public ArrayList<ValueOption> options;

    public UpdateButtonGroup(Button editButton, Button cancelButton, Button saveButton) {
        this.editButton = editButton;
        this.cancelButton = cancelButton;
        this.saveButton = saveButton;
        this.fields = new ArrayList<ValueField>();
        this.options = new ArrayList<ValueOption>();

        editButton.setOnAction(e -> {
            for (ValueField field : fields) {
                field.onEdit();
            }

            for (ValueOption option : options) {
                option.onEdit();
            }
        });

        cancelButton.setOnAction(e -> {
            for (ValueField field : fields) {
                field.onCancel();
            }

            for (ValueOption option : options) {
                option.onCancel();
            }
        });

        saveButton.setOnAction(e -> {
            for (ValueField field : fields) {
                try {
                    field.onSave();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            for (ValueOption option : options) {
                try {
                    option.onSave();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}