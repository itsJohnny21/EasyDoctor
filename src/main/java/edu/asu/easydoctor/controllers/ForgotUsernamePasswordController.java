package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.mail.MessagingException;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public static ForgotUsernamePasswordController instance = null;
    public static final String TITLE = "Forgot Username/Password";
    public static final boolean RESIZABLE = false;

    private ForgotUsernamePasswordController() {}

    public static ForgotUsernamePasswordController getInstance() {
        if (instance == null) {
            instance = new ForgotUsernamePasswordController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        stage.setTitle(ForgotUsernamePasswordController.TITLE);
        stage.setResizable(ForgotUsernamePasswordController.RESIZABLE);
        stage.setWidth(rootPane.getPrefWidth());
        stage.setHeight(rootPane.getPrefHeight());
        rootPane.getStylesheets().add(App.class.getResource("styles/SignUpView.css").toExternalForm());

        for (Role role : Role.values()) {
            roleChoiceBox.getItems().add(role.toString());
        }

        roleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue && newValue != null) {
            }
        });
    }

    @FXML public void handleResetPasswordButtonAction() throws IOException {
        boolean valid = validateEmail() & Utilities.validate(roleChoiceBox);
        if (!valid) return;

        try {
            Database.insertResetPasswordToken(emailTextField.getText(), Role.valueOf(roleChoiceBox.getValue()));
            Stage dialog = new Stage();
            HashMap<String, String> result = ResetPasswordController.loadDialog();

            if (result.get("successful") == "true") {
                SignInController.load(stage);
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
        SignInController.load(stage);
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

    public static void load(Stage stage) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/ForgotUsernamePasswordView.fxml"));
        ForgotUsernamePasswordController controller = ForgotUsernamePasswordController.getInstance();
        controller.setStage(stage);
        
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
