package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SignUpController extends Controller {

    @FXML AnchorPane rootPane;

    @FXML GridPane form1;
    @FXML GridPane form2;
    GridPane currentForm;

    @FXML TextField usernameTextField;
    @FXML ChoiceBox<String> roleChoiceBox;
    @FXML PasswordField passwordField;
    @FXML PasswordField confirmPasswordField;

    @FXML Button goBackButton;
    @FXML Button nextButton;
    @FXML Button signUpButton;
    @FXML ToggleButton viewPasswordToggle;

    @FXML TextField firstNameTextField;
    @FXML TextField middleNameTextField;
    @FXML TextField lastNameTextField;
    @FXML TextField emailTextField;
    @FXML TextField phoneTextField;
    @FXML TextField birthDateTextField;
    @FXML TextField addressTextField;
    @FXML ChoiceBox<String> sexChoiceBox;
    @FXML ChoiceBox<String> raceChoiceBox;
    @FXML ChoiceBox<String> ethnicityChoiceBox;

    public static SignUpController instance = null;
    public static final String TITLE = "Sign Up";
    public static final boolean RESIZABLE = false;
    public static final String VIEW_FILENAME = "SignUpView";
    public static final String STYLE_FILENAME = "SignUpView";

    private SignUpController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SignUpController getInstance() {
        if (instance == null) {
            instance = new SignUpController();
        }

        return instance;
    }

    public void initialize() {
        setCurrentForm(form1);

        for (Ethnicity e : Ethnicity.values()) {
            ethnicityChoiceBox.getItems().add(e.toString());
        }

        for (Race r : Race.values()) {
            raceChoiceBox.getItems().add(r.toString());
        }

        for (Sex s : Sex.values()) {
            sexChoiceBox.getItems().add(s.toString());
        }

        for (Role role : Role.values()) {
            roleChoiceBox.getItems().add(role.toString());
        }


        for (ChoiceBox<String> choiceBox : Arrays.asList(roleChoiceBox, sexChoiceBox, raceChoiceBox, ethnicityChoiceBox)) {
            choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != oldValue && newValue != null) {
                    Utilities.removeClass(choiceBox, "error");
                }
            });
        }

        for (TextField textField : Arrays.asList(firstNameTextField, middleNameTextField, lastNameTextField)) {
            textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                if (!event.getCharacter().matches("[a-zA-Z'\\-\\u00c0-\\u01ff]") || event.getCharacter().length() == 0) {
                    event.consume();
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                textField.setText(Utilities.prettyCapitalize(newValue));
            });
        }

        phoneTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]") || event.getCharacter().length() == 0 || phoneTextField.getText().length() >= 10) {
                event.consume();
            }
        });

        birthDateTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9\\-]") || event.getCharacter().length() == 0 || birthDateTextField.getText().length() >= 10) {
                event.consume();
            }
        });
    }

    @FXML public void handleSignUpButtonAction() {
        boolean valid = validateFirstName() & validateMiddleName() & validateLastName() & validateEmail() & validatePhone() & validateBirthDate() & validateAddress() & validate(sexChoiceBox) & validate(raceChoiceBox) & validate(ethnicityChoiceBox);
        if (!valid) return;

        try {
            if (roleChoiceBox.getValue().equals(Role.PATIENT.toString())) {
                Database.insertPatient(
                    usernameTextField.getText(),
                    passwordField.getText(),
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    Sex.valueOf(sexChoiceBox.getValue()),
                    birthDateTextField.getText(),
                    emailTextField.getText(),
                    phoneTextField.getText(),
                    addressTextField.getText(),
                    Race.valueOf(raceChoiceBox.getValue()),
                    Ethnicity.valueOf(ethnicityChoiceBox.getValue())
                );

            } else {
                HashMap<String, String> result = ManagerCredentialsController.loadDialog();

                Database.insertEmployee(
                    usernameTextField.getText(),
                    passwordField.getText(),
                    Role.valueOf(roleChoiceBox.getValue()),
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    Sex.valueOf(sexChoiceBox.getValue()),
                    birthDateTextField.getText(),
                    emailTextField.getText(),
                    phoneTextField.getText(),
                    addressTextField.getText(),
                    result.get("managerUsername"),
                    result.get("managerPassword"),
                    Race.valueOf(raceChoiceBox.getValue()),
                    Ethnicity.valueOf(ethnicityChoiceBox.getValue())
                );
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Sign up successful");
            alert.setContentText(String.format("You have successfully signed up as a %s!", roleChoiceBox.getValue().toLowerCase()));
            alert.showAndWait();

            if (alert.getResult().getText().equals("OK")) {
                SignInController.getInstance().load(stage);
            }

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            if (alert.getResult().getText().equals("OK")) {
                signUpButton.fire();
            }
        }
    }

    @FXML public void handleNextButtonAction(ActionEvent event) throws SQLException {
        boolean valid = validateUsername() & validatePassword() & validateConfirmPassword() & validate(roleChoiceBox);

        if (!valid) return;

        setCurrentForm(form2);
    }

    @FXML public void handleGoBackButtonAction(ActionEvent event) throws Exception {
        System.out.println("Go back button clicked");

        if (currentForm == form1) {
            WelcomeController.getInstance().load(stage);
        } else {
            setCurrentForm(form1);
        }
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }
    
    @FXML public void handleViewPasswordButtonAction (ActionEvent event) {
        ToggleButton viewPasswordToggle = (ToggleButton) event.getSource();

        if (viewPasswordToggle.isSelected()) {
            passwordField.setPromptText(passwordField.getText());
            passwordField.setText("");
            passwordField.setDisable(true);

            confirmPasswordField.setPromptText(confirmPasswordField.getText());
            confirmPasswordField.setText("");
            confirmPasswordField.setDisable(true);
        } else {
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("");
            passwordField.setDisable(false);

            confirmPasswordField.setText(confirmPasswordField.getPromptText());
            confirmPasswordField.setPromptText("");
            confirmPasswordField.setDisable(false);
        }
    }

    public boolean validate(TextField textField, String regex) {
        if (textField.getText().isBlank()) {
            textField.requestFocus();
            Utilities.addClass(textField, "error");
            return false;
        }

        return true;
    }
    
    public boolean validate(ChoiceBox<String> choiceBox) {
        if (choiceBox.getValue() == null) {
            choiceBox.requestFocus();
            Utilities.addClass(choiceBox, "error");
            return false;
        }

        return true;
    }

    public boolean validatePassword() {
        if (passwordField.getText().isBlank() || !passwordField.getText().matches("^(?=.*[A-Z])(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,}$")) {
            passwordField.requestFocus();
            Utilities.addClass(passwordField, "error");
            return false;
        }

        return true;
    }

    public boolean validateUsername() throws SQLException {
        if (usernameTextField.getText().isBlank() || !usernameTextField.getText().matches("^[a-zA-Z][a-zA-Z0-9_]{4,}$") || usernameTextField.getText().length() < 5 || Database.userExists(usernameTextField.getText())) {
            usernameTextField.requestFocus();
            Utilities.addClass(usernameTextField, "error");
            return false;
        }

        return true;
    }

    public boolean validateFirstName() {
        if (firstNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{2,}$")) {
            return true;
        }

        Utilities.addClass(firstNameTextField, "error");
        return false;
    }

    public boolean validateLastName() {
        if (!lastNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{2,}$")) {
            Utilities.addClass(lastNameTextField, "error");
            return false;
        }

        return true;
    }

    public boolean validateMiddleName() {
        if (!middleNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{0,}$")) {
            Utilities.addClass(middleNameTextField, "error");
            return false;
        }

        return true;
    }

    public boolean validateEmail() {
        if (!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Utilities.addClass(emailTextField, "error");
            return false;
        }
        
        return true;
    }

    public boolean validatePhone() {
        if (!phoneTextField.getText().matches("^[0-9]{10}$")) {
            Utilities.addClass(phoneTextField, "error");
            return false;
        }
        
        return true;
    }

    public boolean validateBirthDate() {
        if (!birthDateTextField.getText().matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
            Utilities.addClass(birthDateTextField, "error");
            return false;
        }

        try {
            LocalDate.parse(birthDateTextField.getText());
        } catch (DateTimeParseException e) {
            Utilities.addClass(birthDateTextField, "error");
            return false;
        }

        return true;
    }

    public boolean validateAddress() {
        if (!addressTextField.getText().matches("^[a-zA-Z0-9'\\-\\u00c0-\\u01ff ]{2,}$")) {
            Utilities.addClass(addressTextField, "error");
            return false;
        }
        
        return true;
    }

    public boolean validateConfirmPassword() {
        if (!passwordField.getText().equals(confirmPasswordField.getText()) || confirmPasswordField.getText().isBlank()) {
            Utilities.addClass(confirmPasswordField, "error");
            return false;
        }
        
        return true;
    }

    public void setCurrentForm(GridPane form) {
        if (currentForm != null) {
            currentForm.setVisible(false);
            currentForm.setDisable(true);
        }

        form.setVisible(true);
        form.setDisable(false);
        currentForm = form;

        nextButton.setVisible(currentForm == form1);
        nextButton.setDisable(currentForm != form1);
        signUpButton.setVisible(currentForm != form1);
        signUpButton.setDisable(currentForm == form1);
    }

    // public static void load(Stage stage) throws IOException {
    //     SignUpController controller = SignUpController.getInstance();
    //     controller.loadHelper(VIEW_FILENAME, stage);
    // }
}


// TODO: Test multiple popular email domains for email validation
// TODO: Test multiple phone numbers for phone validation
// TODO Test all possible dates for birthDate validation