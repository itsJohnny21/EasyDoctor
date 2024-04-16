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
import edu.asu.easydoctor.ShowPasswordGroup;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SignUpController extends Controller {

    @FXML public AnchorPane rootPane;

    @FXML public GridPane form1;
    @FXML public GridPane form2;
    GridPane currentForm;

    @FXML public TextField usernameTextField;
    @FXML public ChoiceBox<String> roleChoiceBox;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField confirmPasswordField;

    @FXML public Button goBackButton;
    @FXML public Button nextButton;
    @FXML public Button signUpButton;
    @FXML public ToggleButton showPasswordToggle;

    @FXML public TextField firstNameTextField;
    @FXML public TextField middleNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextField emailTextField;
    @FXML public TextField phoneTextField;
    @FXML public TextField birthDateTextField;
    @FXML public TextField addressTextField;
    @FXML public ChoiceBox<String> sexChoiceBox;
    @FXML public ChoiceBox<String> raceChoiceBox;
    @FXML public ChoiceBox<String> ethnicityChoiceBox;

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

        ShowPasswordGroup spg = new ShowPasswordGroup(showPasswordToggle);
        spg.addPasswordFields(passwordField, confirmPasswordField);
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
                HashMap<String, Object> data = new HashMap<>();
                HashMap<String, Object> result = ManagerCredentialsController.getInstance().loadDialog(data);

                if (result.get("successful") == "true") {
                    closeAndNullify();
                    SignInController.getInstance().load();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            WelcomeController.getInstance().load();
        } else {
            setCurrentForm(form1);
        }
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
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

    public void closeAndNullify() {
        instance = null;
        close();
    }
}


// TODO: Test multiple popular email domains for email validation
// TODO: Test multiple phone numbers for phone validation
// TODO Test all possible dates for birthDate validation