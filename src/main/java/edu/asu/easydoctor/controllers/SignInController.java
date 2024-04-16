package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.ShowPasswordGroup;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SignInController extends Controller {
    
    
    @FXML public TextField usernameTextField;
    @FXML public PasswordField passwordField;
    @FXML public ToggleButton showPasswordToggle;
    @FXML public Button signInButton;
    @FXML public Button goBackButton;
    @FXML public Hyperlink forgotUsernamePasswordHyperLink;
    @FXML public CheckBox rememberMeCheckbox;
    @FXML public AnchorPane rootPane;
    
    private static Preferences preferences = Preferences.userNodeForPackage(SignInController.class);
    public static SignInController instance = null;
    public static final String TITLE = "Sign In";
    public static final boolean RESIZABLE = false;
    public static final String VIEW_FILENAME = "SignInView";
    public static final String STYLE_FILENAME = "SignUpView";

    private SignInController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SignInController getInstance() {
        if (instance == null) {
            instance = new SignInController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        usernameTextField.setText(preferences.get("username", ""));
        passwordField.setText(preferences.get("password", ""));
        rememberMeCheckbox.setSelected(preferences.getBoolean("rememberMeChecked", false));

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signInButton.fire();
            }
        });

        ShowPasswordGroup spg = new ShowPasswordGroup(showPasswordToggle);
        spg.addPasswordField(passwordField);
    }
    
    @FXML public void handleSignInButtonAction(ActionEvent event) throws SQLException, UnknownHostException, IOException, Exception {
        rememberMe();

        if (usernameTextField.getText().isBlank()) {
            usernameTextField.requestFocus();
            usernameTextField.getStyleClass().add("error");
            return;
        }
        
        if (passwordField.getText().isEmpty()) {
            passwordField.requestFocus();
            passwordField.getStyleClass().add("error");
            return;
        }

        boolean successful = Database.signIn(usernameTextField.getText(), passwordField.getText());

        if (successful) {
            close();
            if (Database.role == Role.DOCTOR || Database.role == Role.NURSE) {
                WorkPortalController.getInstance().load(stage);
            } else if (Database.role == Role.PATIENT) {
                PatientPortalController.getInstance().load(stage);
            }

        } else {
            usernameTextField.requestFocus();
            usernameTextField.getStyleClass().add("error");
            passwordField.getStyleClass().add("error");
        }
    }

    @FXML public void handleGoBackButtonAction(ActionEvent event) throws IOException, Exception {
        WelcomeController.getInstance().load(stage);
    }
    
    @FXML public void handleForgotUsernamePasswordButtonAction(ActionEvent event) throws IOException, Exception {
        ForgotUsernamePasswordController.getInstance().load(stage);
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    
    public void rememberMe() {
        if (rememberMeCheckbox.isSelected()) {
            preferences.put("username", usernameTextField.getText());
            preferences.put("password", passwordField.getText());
            preferences.putBoolean("rememberMeChecked", rememberMeCheckbox.isSelected());
        } else {
            preferences.remove("username");
            preferences.remove("password");
            preferences.remove("rememberMeChecked");
        }
    }

    public void close() {
        instance = null;
        stage.close();
        scene = null;
        stage = null;
    }
}
