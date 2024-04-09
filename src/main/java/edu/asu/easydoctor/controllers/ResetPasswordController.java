package edu.asu.easydoctor.controllers;

import java.util.HashMap;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.exceptions.ExpiredResetPasswordTokenException;
import edu.asu.easydoctor.exceptions.InvalidResetPasswordTokenException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ResetPasswordController extends DialogController {

    @FXML DialogPane rootPane;
    @FXML Button resetButton;
    @FXML Button cancelButton;
    @FXML TextField resetPasswordTokenTextField;
    @FXML PasswordField newPasswordField;
    @FXML PasswordField confirmNewPasswordField;

    public void initialize() throws Exception {
        title = "Reset Password";
        rootPane.getStylesheets().add(App.class.getResource("styles/SignUpView.css").toExternalForm());
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight() + 20;
        resizable = false;

        resetPasswordTokenTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]") || event.getCharacter().length() == 0 || resetPasswordTokenTextField.getText().length() >= 6 && resetPasswordTokenTextField.getSelectedText().length() == 0) {
                event.consume();
            }
        });

        result = new HashMap<>();

        //! Delete me
        resetPasswordTokenTextField.setText("946930");
        newPasswordField.setText("idkBruh21!");
        confirmNewPasswordField.setText("idkBruh21!");
    }

    public String getTitle() {
        return title;
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        textField.getStyleClass().remove("error");
    }

    @FXML public void handleResetButtonAction(ActionEvent event) {
        boolean valid = validateResetPasswordToken() & validateNewPassword() & validateConfirmNewPassword();
        if (!valid) return;

        try {
            Database.resetPassword(Integer.parseInt(resetPasswordTokenTextField.getText()), newPasswordField.getText()); //TODO: Make sure the same password is not used again
            result.put("successful", "true");
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText("Password reset successfully");
            alert.showAndWait();

            if (alert.getResult().getText().equals("OK")) {
                stage.close();
            }

        } catch (InvalidResetPasswordTokenException e) {
            Utilities.addClass(resetPasswordTokenTextField, "error");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText("The token you entered is invalid. Please try again.");
            alert.showAndWait();

        } catch (ExpiredResetPasswordTokenException e) {
            Utilities.addClass(resetPasswordTokenTextField, "error");
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText("The token you entered has expired.");
            alert.showAndWait();
            

        } catch (Exception e) {
            resetPasswordTokenTextField.getStyleClass().add("error");
            e.printStackTrace();
        }
    }

    @FXML public void handleCancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML public void handleShowPasswordButtonAction(ActionEvent event) {
        if (!newPasswordField.isDisabled()) {
            newPasswordField.setPromptText(newPasswordField.getText());
            newPasswordField.setText("");
            newPasswordField.setDisable(true);

            confirmNewPasswordField.setPromptText(confirmNewPasswordField.getText());
            confirmNewPasswordField.setText("");
            confirmNewPasswordField.setDisable(true);
        } else {
            newPasswordField.setText(newPasswordField.getPromptText());
            newPasswordField.setPromptText("");
            newPasswordField.setDisable(false);

            confirmNewPasswordField.setText(confirmNewPasswordField.getPromptText());
            confirmNewPasswordField.setPromptText("");
            confirmNewPasswordField.setDisable(false);
        }
    }

    public boolean validateNewPassword() {
        if (newPasswordField.getText().isBlank() || !newPasswordField.getText().matches("^(?=.*[A-Z])(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,}$")) {
            newPasswordField.requestFocus();
            Utilities.addClass(newPasswordField, "error");
            return false;
        }

        return true;
    }

    public boolean validateConfirmNewPassword() {
        if (confirmNewPasswordField.getText().isBlank() || !confirmNewPasswordField.getText().equals(newPasswordField.getText()) || !confirmNewPasswordField.getText().matches("^(?=.*[A-Z])(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,}$")) {
            Utilities.addClass(confirmNewPasswordField, "error");
            return false;
        }

        return true;
    }

    public boolean validateResetPasswordToken() {
        if (!resetPasswordTokenTextField.getText().matches("^[0-9]{6}$")) {
            Utilities.addClass(resetPasswordTokenTextField, "error");
            return false;
        }

        return true;
    }

    public HashMap<String, String> getResult() {
        return result;
    }
}
