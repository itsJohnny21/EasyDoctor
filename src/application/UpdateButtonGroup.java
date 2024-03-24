package application;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Button;

public class UpdateButtonGroup {
    public Button editButton;
    public Button cancelButton;
    public Button saveButton;
    public ArrayList<Value> values;
    public boolean error;

    public UpdateButtonGroup(Button editButton, Button cancelButton, Button saveButton) {
        this.editButton = editButton;
        this.cancelButton = cancelButton;
        this.saveButton = saveButton;
        this.values = new ArrayList<Value>();

        this.editButton.setDisable(false);
        this.cancelButton.setDisable(true);
        this.saveButton.setDisable(true);

        editButton.setOnAction(e -> {
            editButton.setDisable(true);
            cancelButton.setDisable(false);
            saveButton.setDisable(false);

            for (Value value : values) {
                value.onEdit();
            }
        });

        cancelButton.setOnAction(e -> {
            cancelButton.setDisable(true);
            editButton.setDisable(false);
            saveButton.setDisable(true);

            for (Value value : values) {
                value.onCancel();
            }
        });
//SELECT COUNT(*) INTO @count FROM lookup_table WHERE value = 'new_value'; //! maybe use this to validate the input

// IF @count > 0 THEN
// UPDATE your_table SET your_column = 'new_value' WHERE id = your_id;
// END IF;

        saveButton.setOnAction(e -> {
            for (Value value : values) {
                try {
                    value.onSave();
                } catch (SQLException exc) {
                    value.onError();
                    error = true;
                }
            }
            
            saveButton.setDisable(true);
            editButton.setDisable(false);
            cancelButton.setDisable(true);

            if (error) {
                editButton.fire();
            }
        });
    }
}