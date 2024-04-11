package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.ShowPasswordGroup;
import edu.asu.easydoctor.Utilities;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ManagerCredentialsController extends Controller {

    @FXML public DialogPane rootPane;
    @FXML public Button confirmButton;
    @FXML public Button cancelButton;
    @FXML public ToggleButton showPasswordToggle;
    @FXML public TextField managerUsernameTextField;
    @FXML public PasswordField managerPasswordField;

    public static ManagerCredentialsController instance = null;
    public final static String TITLE = "Manager Credentials";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "ManagerCredentialsDialog";
    public final static String STYLE_FILENAME = "Basic";
    public static HashMap<String, String> result = new HashMap<>();

    private ManagerCredentialsController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static ManagerCredentialsController getInstance() {
        if (instance == null) {
            instance = new ManagerCredentialsController();
        }

        return instance;
    }

    public void initialize() {
        ShowPasswordGroup spg = new ShowPasswordGroup(showPasswordToggle);
        spg.addPasswordField(managerPasswordField);

        stage.setOnCloseRequest(event -> {
            close2();
        });
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    @FXML public void handleConfirmButtonAction() throws SQLException {
        // result.put("managerUsername", managerUsernameTextField.getText());
        // result.put("managerPassword", managerPasswordField.getText());
        // close2();

        try {
            Database.insertEmployee(
                SignUpController.getInstance().usernameTextField.getText(),
                SignUpController.getInstance().passwordField.getText(),
                Role.valueOf(SignUpController.getInstance().roleChoiceBox.getValue()),
                SignUpController.getInstance().firstNameTextField.getText(),
                SignUpController.getInstance().lastNameTextField.getText(),
                Sex.valueOf(SignUpController.getInstance().sexChoiceBox.getValue()),
                SignUpController.getInstance().birthDateTextField.getText(),
                SignUpController.getInstance().emailTextField.getText(),
                SignUpController.getInstance().phoneTextField.getText(),
                SignUpController.getInstance().addressTextField.getText(),
                managerUsernameTextField.getText(),
                managerPasswordField.getText(),
                Race.valueOf(SignUpController.getInstance().raceChoiceBox.getValue()),
                Ethnicity.valueOf(SignUpController.getInstance().ethnicityChoiceBox.getValue())
            );

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Sign up successful");
            alert.setContentText(String.format("You have successfully signed up as a %s!", SignUpController.getInstance().roleChoiceBox.getValue().toLowerCase()));
            alert.showAndWait();

            result.put("successful", "true");
            close2();

        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText(null);
            alert.setContentText("The manager username or password you entered is incorrect. Please try again.");
            alert.showAndWait();


        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML public void handleCancelButtonAction() {
        close2();
    }
    
    public boolean validateUsername() throws SQLException {
        if (managerUsernameTextField.getText().isBlank() || !managerUsernameTextField.getText().matches("^[a-zA-Z][a-zA-Z0-9_]{4,}$") || managerUsernameTextField.getText().length() < 5) {
            managerUsernameTextField.requestFocus();
            Utilities.addClass(managerUsernameTextField, "error");
            return false;
        }

        return true;
    }

    public static HashMap<String, String> loadDialog() throws Exception {
        if (instance != null) return null;

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(String.format("views/%s.fxml", VIEW_FILENAME)));
        ManagerCredentialsController controller = ManagerCredentialsController.getInstance();
        controller.setStage(stage);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(App.class.getResource(String.format("styles/%s.css", STYLE_FILENAME)).toExternalForm());
        stage.show();

        return result;
    }

    public void close2() {
        stage.close();
        instance = null;
        scene = null;
    }
}
