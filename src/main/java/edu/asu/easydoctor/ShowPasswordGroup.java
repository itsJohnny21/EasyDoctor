package edu.asu.easydoctor;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;

public class ShowPasswordGroup {

    public ArrayList<PasswordField> passwordFields = new ArrayList<>();
    public ToggleButton showPasswordToggle;
    public Button connectedButton;

    public ShowPasswordGroup(ToggleButton showPasswordToggle) {
        this.showPasswordToggle = showPasswordToggle;

        this.showPasswordToggle.setOnAction(event -> {

            if (showPasswordToggle.isSelected()) {
                int count = 0;
                for (PasswordField passwordField : passwordFields) {
                    count += passwordField.getText().length();
                }
                if (count == 0) {
                    showPasswordToggle.setSelected(false);
                    return;
                }
            }

            if (showPasswordToggle.isSelected()) {
                for (PasswordField passwordField : passwordFields) {
                    passwordField.setPromptText(passwordField.getText());
                    passwordField.setText("");
                    passwordField.setDisable(true);
                    showPasswordToggle.setText("Hide");
                }
            } else {
                for (PasswordField passwordField : passwordFields) {
                    passwordField.setText(passwordField.getPromptText());
                    passwordField.setPromptText("");
                    passwordField.setDisable(false);
                    showPasswordToggle.setText("Show");
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

    public void setConnectedButton(Button connectedButton) {
        this.connectedButton = connectedButton;
        
        
        EventHandler<ActionEvent> originalAction = this.connectedButton.getOnAction();
        this.connectedButton.setOnAction(event -> {
            if (showPasswordToggle.isSelected()) {
                showPasswordToggle.fire();
            }

            if (originalAction != null) {
                originalAction.handle(event);
            }

        });
    }
}
