package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.mail.MessagingException;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.EmailManager;
import edu.asu.easydoctor.Utilities;
import edu.asu.easydoctor.exceptions.InvalidResetPasswordTokenException;
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
        boolean valid = Utilities.validate(emailTextField, styleFilename) & Utilities.validate(roleChoiceBox);
        if (!valid) return;

        try {
            int token = Database.insertResetPasswordToken(emailTextField.getText(), Role.valueOf(roleChoiceBox.getValue()));
            EmailManager.sendResetPasswordEmail(title, styleFilename, token, Database.getExpirationTimeForResetPasswordToken(token));
            
            HashMap<String, Object> data = new HashMap<>();
            HashMap<String, Object> result = ResetPasswordController.getInstance().loadDialog(data);

            if (result != null && result.get("successful") == "true") {
                SignInController.getInstance().load();
            }

        } catch (MessagingException e) {
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
        } catch (UnknownHostException e) {
            System.out.println("No internet connection");
        } catch (InvalidResetPasswordTokenException e) {
            System.out.println("Invalid reset password token");
        }
    }

    @FXML public void handleGoBackButtonAction() throws IOException, Exception {
        closeAndNullify();
        SignInController.getInstance().load();
    }

    public void loadHelper() {}
    
    public void closeAndNullify() {
        instance = null;
        close();
    }
}
