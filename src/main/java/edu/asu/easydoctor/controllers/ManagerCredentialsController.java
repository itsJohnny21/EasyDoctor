package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ManagerCredentialsController extends DialogController {

    @FXML DialogPane rootPane;
    @FXML Button confirmButton;
    @FXML Button cancelButton;
    @FXML Button showButton;
    @FXML TextField managerUsernameTextField;
    @FXML PasswordField managerPasswordField;

    public void initialize() throws Exception {
        title = "Manager Credentials";

        width = 400;
        height = 300;
        resizable = false;

        result = new HashMap<>();
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    @FXML public void handleConfirmButtonAction() throws SQLException {
        result.put("managerUsername", managerUsernameTextField.getText());
        result.put("managerPassword", managerPasswordField.getText());
        stage.close();
    }

    @FXML public void handleCancelButtonAction() {
        stage.close();
    }

    @FXML public void handleShowButtonAction() { //! Do this in css instead
        if (!managerPasswordField.isDisabled()) {
            managerPasswordField.setPromptText(managerPasswordField.getText());
            managerPasswordField.setText("");
            managerPasswordField.setDisable(true);
            showButton.setText("Show"); //! Do the same to sign up controller
        } else {
            managerPasswordField.setText(managerPasswordField.getPromptText());
            managerPasswordField.setPromptText("");
            managerPasswordField.setDisable(false);
            showButton.setText("Hide");
        }
    }

    public boolean validateUsername() throws SQLException {
        if (managerUsernameTextField.getText().isBlank() || !managerUsernameTextField.getText().matches("^[a-zA-Z][a-zA-Z0-9_]{4,}$") || managerUsernameTextField.getText().length() < 5) {
            managerUsernameTextField.requestFocus();
            Utilities.addClass(managerUsernameTextField, "error");
            return false;
        }

        return true;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String, String> getResult() {
        return result;
    }
}
