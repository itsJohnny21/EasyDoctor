package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ForgotUsernamePasswordController extends Controller {

	@FXML public TextField emailTextField;
	@FXML public Button retrieveUsernameButton;
	@FXML public Button resetPasswordButton;
    @FXML public Text invalidCredentialsText;
    @FXML public BorderPane mainPane;

    public void initialize() {
        title = "Reset Password";
    }

    @FXML public void handleResetPasswordButtonAction() {
        if (emailTextField.getText().isBlank()) {
            emailTextField.requestFocus();
            emailTextField.setStyle("-fx-border-color: red;");
            return;
        }
    }

    @FXML public void handleRetrieveUsernameButtonAction() {
        if (emailTextField.getText().isBlank()) {
            emailTextField.requestFocus();
            emailTextField.setStyle("-fx-border-color: red;");
            return;
        }
    }

    @FXML public void handleKeyTyped(KeyEvent event) {
        emailTextField.setStyle("-fx-border-color: none;");
    }
    
    @FXML public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            resetPasswordButton.setStyle("-fx-background-color: #d3d3d3;");
        }
    }
    
    @FXML public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
			resetPasswordButton.fire();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }
}
