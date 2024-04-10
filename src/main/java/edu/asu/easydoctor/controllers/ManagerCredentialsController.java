package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.ShowPasswordGroup;
import edu.asu.easydoctor.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ManagerCredentialsController extends Controller {

    @FXML DialogPane rootPane;
    @FXML Button confirmButton;
    @FXML Button cancelButton;
    @FXML ToggleButton showPasswordToggle;
    @FXML TextField managerUsernameTextField;
    @FXML PasswordField managerPasswordField;

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
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    @FXML public void handleConfirmButtonAction() throws SQLException {
        result.put("managerUsername", managerUsernameTextField.getText());
        result.put("managerPassword", managerPasswordField.getText());
        stage.close();
    }

    @FXML public void handleCancelButtonAction() {
        stage.close();
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
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/ManagerCredentialsDialog.fxml"));
        ManagerCredentialsController controller = ManagerCredentialsController.getInstance();
        controller.setStage(stage);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        return result;
    }
}
