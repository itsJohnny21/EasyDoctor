package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.mail.MessagingException;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ForgotUsernamePasswordController extends Controller {

    @FXML public TextField emailTextField;
    @FXML public ChoiceBox<String> roleChoiceBox;
    @FXML public Button sendEmailButton;
    @FXML public Button goBackButton;
    @FXML public AnchorPane rootPane;

    public static ForgotUsernamePasswordController instance = null;
    public static final String TITLE = "Forgot Username/Password";
    public static final boolean RESIZABLE = false;
    public static final String VIEW_FILENAME = "ForgotUsernamePasswordView";
    public static final String STYLE_FILENAME = "SignUpView";

    private ForgotUsernamePasswordController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static ForgotUsernamePasswordController getInstance() {
        if (instance == null) {
            instance = new ForgotUsernamePasswordController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        for (Role role : Role.values()) {
            roleChoiceBox.getItems().add(role.toString());
        }

        roleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue && newValue != null) {
            }
        });
    }

    @FXML public void handleSendEmailButtonAction() throws IOException {
        boolean valid = validateEmail() & Utilities.validate(roleChoiceBox);
        if (!valid) return;

        try {
            Database.insertResetPasswordToken(emailTextField.getText(), Role.valueOf(roleChoiceBox.getValue()));
            HashMap<String, String> result = ResetPasswordController.loadDialog();

            if (result != null && result.get("successful") == "true") {
                SignInController.getInstance().load(stage);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
        } catch (UnknownHostException e) {
            System.out.println("No internet connection");
        }
    }

    @FXML public void handleGoBackButtonAction() throws IOException, Exception {
        close();
        SignInController.getInstance().load(stage);
    }

    public boolean validateEmail() {
        if (!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Utilities.addClass(emailTextField, "error");
            return false;
        }

        return true;
    }
}
