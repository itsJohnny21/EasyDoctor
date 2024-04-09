package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.mail.MessagingException;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ForgotUsernamePasswordController extends Controller {

    @FXML public TextField emailTextField;
    @FXML public ChoiceBox<String> roleChoiceBox;
    @FXML public Button resetPasswordButton;
    @FXML public Button goBackButton;
    @FXML public AnchorPane rootPane;

    public void initialize() throws Exception {
        title = "Forgot Username/Password";
        rootPane.getStylesheets().add(App.class.getResource("styles/SignUpView.css").toExternalForm());
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight();
        resizable = false;

        for (Role role : Role.values()) {
            roleChoiceBox.getItems().add(role.toString());
        }

        roleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue && newValue != null) {
            }
        });

        //! Delete
        emailTextField.setText("jsalazar6421@gmail.com");
        roleChoiceBox.setValue(Role.PATIENT.toString());
    }

    @FXML public void handleResetPasswordButtonAction() throws IOException {
        boolean valid = validateEmail() & Utilities.validate(roleChoiceBox);
        if (!valid) return;

        try {
            Database.insertResetPasswordToken(emailTextField.getText(), Role.valueOf(roleChoiceBox.getValue()));
            Stage dialog = new Stage();
            App.loadPage("ResetPasswordDialog", dialog);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleGoBackButtonAction() throws IOException, Exception {
        App.loadPage("SignInView", stage);
    }

    public boolean validateEmail() {
        if (!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Utilities.addClass(emailTextField, "error");
            return false;
        }

        return true;
    }

    public String getTitle() {
        return title;
    }
}
