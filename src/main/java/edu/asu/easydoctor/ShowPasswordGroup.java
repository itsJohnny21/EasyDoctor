package edu.asu.easydoctor;

import java.util.ArrayList;

import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;

public class ShowPasswordGroup {

    public ArrayList<PasswordField> passwordFields = new ArrayList<>();
    public ToggleButton showPasswordToggle;

    public ShowPasswordGroup(ToggleButton showPasswordToggle) {
        this.showPasswordToggle = showPasswordToggle;

        this.showPasswordToggle.setOnAction(event -> {
            if (showPasswordToggle.isSelected()) {
                for (PasswordField passwordField : passwordFields) {
                    passwordField.setPromptText(passwordField.getText());
                    passwordField.setText("");
                    passwordField.setDisable(true);
                    showPasswordToggle.setText("Show");
                }
            } else {
                for (PasswordField passwordField : passwordFields) {
                    passwordField.setText(passwordField.getPromptText());
                    passwordField.setPromptText("");
                    passwordField.setDisable(false);
                    showPasswordToggle.setText("Hide");
                }
            }
        });
    }
    
    public void addPasswordField(PasswordField passwordField) {
        passwordFields.add(passwordField);
    }
    
    public void addPasswordFields(PasswordField... passwordFields) {
        for (PasswordField passwordField : passwordFields) {
            this.passwordFields.add(passwordField);
        }
    }

    public void removePasswordField(PasswordField passwordField) {
        passwordFields.remove(passwordField);
    }

    public void clearPasswordFields() {
        for (PasswordField passwordField : passwordFields) {
            passwordField.setText("");
            passwordField.setPromptText("");
            passwordField.setDisable(false);
        }
    }

    
}
