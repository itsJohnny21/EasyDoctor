package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.util.HashMap;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.exceptions.ExpiredResetPasswordTokenException;
import edu.asu.easydoctor.exceptions.InvalidResetPasswordTokenException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ResetPasswordController extends Controller {

    @FXML public DialogPane rootPane;
    @FXML public Button resetButton;
    @FXML public Button cancelButton;
    @FXML public TextField resetPasswordTokenTextField;
    @FXML public PasswordField newPasswordField;
    @FXML public PasswordField confirmNewPasswordField;

    public static ResetPasswordController instance = null;
    public static HashMap<String, String> result = new HashMap<>();
    public final static String TITLE = "Reset Password";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "ResetPasswordDialog";
    public final static String STYLE_FILENAME = "SignUpView";

    private ResetPasswordController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static ResetPasswordController getInstance() {
        if (instance == null) {
            instance = new ResetPasswordController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        resetPasswordTokenTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]") || event.getCharacter().length() == 0 || resetPasswordTokenTextField.getText().length() >= 6 && resetPasswordTokenTextField.getSelectedText().length() == 0) {
                event.consume();
            }
        });
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        textField.getStyleClass().remove("error");
        textField.positionCaret(textField.getText().length());
    }

    @FXML public void handleResetButtonAction(ActionEvent event) {
        boolean valid = validateResetPasswordToken() & validateNewPassword() & validateConfirmNewPassword();
        if (!valid) return;

        try {
            Database.resetPassword(Integer.parseInt(resetPasswordTokenTextField.getText()), newPasswordField.getText());
            result.put("successful", "true");
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText("Password reset successfully");
            alert.showAndWait();
            ForgotUsernamePasswordController.getInstance().close();
            close();
            SignInController.getInstance().load(stage);

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
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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

    public static HashMap<String, String> loadDialog() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/ResetPasswordDialog.fxml"));
        ResetPasswordController controller = ResetPasswordController.getInstance();
        controller.setStage(stage);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return result;
    }
}
