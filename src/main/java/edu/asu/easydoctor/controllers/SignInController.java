package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SignInController extends Controller {
    
    private static Preferences preferences = Preferences.userNodeForPackage(SignInController.class);

    @FXML public TextField usernameTextField;
    @FXML public PasswordField passwordTextField;
    @FXML public Button signInButton;
    @FXML public Button goBackButton;
    @FXML public Hyperlink forgotUsernamePasswordHyperLink;
    @FXML public CheckBox rememberMeCheckbox;
    @FXML public AnchorPane rootPane;

    public void initialize() throws Exception {
        title = "Sign In";
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight();
        resizable = false;

        usernameTextField.setText(preferences.get("username", ""));
        passwordTextField.setText(preferences.get("password", ""));
        rememberMeCheckbox.setSelected(preferences.getBoolean("rememberMeChecked", false));

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signInButton.fire();
            }
        });

        // //! Delete me
        // PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(0.1));
        // pause.setOnFinished(event -> {
        //     signInButton.fire();
        // });
        // pause.play();
    }
    
    @FXML public void handleSignInButtonAction(ActionEvent event) throws SQLException, UnknownHostException, IOException, Exception {

        rememberMe(rememberMeCheckbox.isSelected());

        if (usernameTextField.getText().isBlank()) {
            usernameTextField.requestFocus();
            usernameTextField.setStyle("-fx-border-color: red;");
            return;
        }
        
        if (passwordTextField.getText().isEmpty()) {
            passwordTextField.requestFocus();
            passwordTextField.setStyle("-fx-border-color: red;");
            return;
        }

        boolean successful = Database.signIn(usernameTextField.getText(), passwordTextField.getText());


        if (successful) {
            if (Database.role == Role.DOCTOR || Database.role == Role.NURSE) {
                // App.loadPage("WorkPortalView", stage); //! Implement this
            } else if (Database.role == Role.PATIENT) {
                // App.loadPage("PatientPortalView", stage);
            }

        } else {
            usernameTextField.requestFocus();
            usernameTextField.setStyle("-fx-border-color: red;");
            passwordTextField.setStyle("-fx-border-color: red;");
        }
    }

    @FXML public void handleGoBackButtonAction(ActionEvent event) throws IOException, Exception {
        App.loadPage("WelcomeView", stage);
    }
    
    @FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) throws IOException, Exception {
        App.loadPage("ForgotUsernamePasswordView", stage);
    }
    
    public String getTitle() {
        return this.title;
    }

    @FXML public void handleKeyTyped(KeyEvent event) {
        passwordTextField.setStyle("-fx-border-color: none;");
        usernameTextField.setStyle("-fx-border-color: none;");
    }
    
    @FXML public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signInButton.setStyle("-fx-background-color: #d3d3d3;");
        }
    }
    
    @FXML public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signInButton.fire();
        }
    }

    public void rememberMe(boolean rememberMe) {
        if (rememberMe) {
            preferences.put("username", usernameTextField.getText());
            preferences.put("password", passwordTextField.getText());
            preferences.putBoolean("rememberMeChecked", rememberMeCheckbox.isSelected());
        } else {
            preferences.remove("username");
            preferences.remove("password");
            preferences.remove("rememberMeChecked");
            preferences.remove("doctorNurseChecked");
        }
    }
}
