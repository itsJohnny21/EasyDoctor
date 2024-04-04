package application.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import application.App;
import application.Database;
import application.Database.Ethnicity;
import application.Database.Race;
import application.Database.Role;
import application.Database.Sex;
import application.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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

    public void initialize() {
        title = "Sign Up";
        rootPane.getStylesheets().add(getClass().getResource("/application/styles/SignUpView.css").toExternalForm());
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight() + 30;
        resizable = false;

        //! Delete me!
        form1.setVisible(true);
        form1.setDisable(false);
        form2.setVisible(false);
        form2.setDisable(true);
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

        // //! DELETE ME
        usernameTextField.setText("auser1");
        passwordField.setText("Password!");
        confirmPasswordField.setText(passwordField.getText());
        firstNameTextField.setText("John");
        lastNameTextField.setText("Doe");
        emailTextField.setText("idk");
        phoneTextField.setText("");
        birthDateTextField.setText("2021-01-01");
        addressTextField.setText("123");

        roleChoiceBox.setValue(roleChoiceBox.getItems().get(0));
        sexChoiceBox.setValue(sexChoiceBox.getItems().get(0));
        ethnicityChoiceBox.setValue(ethnicityChoiceBox.getItems().get(0));
        raceChoiceBox.setValue(raceChoiceBox.getItems().get(0));

        nextButton.fire();
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

                //TODO: Direct to the patient's information tab so that they can fill out the rest of their informaiton such as:
                /*
                insuranceProvider
                insuranceID
                emergencyContactName
                emergencyContactPhone
                motherFirstName
                motherLastName
                fatherFirstName
                fatherLastName
                 */

            } else {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Manager Credentials");
                dialog.setHeaderText("Enter manager credentials");

                ButtonType loginButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                GridPane grid = new GridPane();

                TextField managerID = new TextField();
                managerID.setPromptText("Manager ID");

                PasswordField managerPassword = new PasswordField();
                managerPassword.setPromptText("Manager Password");

                grid.add(managerID, 0, 0);
                grid.add(managerPassword, 1, 0);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(managerID.getText(), managerPassword.getText());
                    }

                    return null;
                });

                Pair<String, String> result = dialog.showAndWait().orElse(null);

                if (result == null) return;

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
                    result.getKey(),
                    result.getValue(),
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
                App.loadPage("TestView", stage);
            }

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML public void handleNextButtonAction(ActionEvent event) throws SQLException {
        System.out.println("Next button clicked");

        boolean valid = validateUsername() & validatePassword() & validateConfirmPassword() & validate(roleChoiceBox);

        if (!valid) return;

        setCurrentForm(form2);
    }

    @FXML public void handleGoBackButtonAction(ActionEvent event) throws Exception {
        System.out.println("Go back button clicked");

        if (currentForm == form1) {
            App.loadPage("WelcomeView", stage);
        } else {
            setCurrentForm(form1);
        }
    }

    @FXML public void handleTextFieldKeyTyped (KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        textField.getStyleClass().remove("error");
    }
    
    @FXML public void handleViewPasswordButtonAction (ActionEvent event) {
        ToggleButton viewPasswordToggle = (ToggleButton) event.getSource();

        if (viewPasswordToggle.isSelected()) {
            passwordField.setPromptText(passwordField.getText());
            passwordField.setText("");
            passwordField.setDisable(true);
            passwordField.setOpacity(1);

            confirmPasswordField.setPromptText(confirmPasswordField.getText());
            confirmPasswordField.setText("");
            confirmPasswordField.setDisable(true);
            confirmPasswordField.setOpacity(1);
        } else {
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("");
            passwordField.setDisable(false);

            confirmPasswordField.setText(confirmPasswordField.getPromptText());
            confirmPasswordField.setPromptText("");
            confirmPasswordField.setDisable(false);
        }
    }

    public boolean validate(TextField textField) {
        if (textField.getText().isBlank()) {
            textField.requestFocus();
            textField.getStyleClass().add("error");
            return false;
        }

        return true;
    }
    
    public boolean validate(ChoiceBox<String> choiceBox) {
        if (choiceBox.getValue() == null) {
            choiceBox.requestFocus();
            choiceBox.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public boolean validatePassword() {
        if (passwordField.getText().isBlank() || !passwordField.getText().matches("^(?=.*[A-Z])(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,}$")) {
            passwordField.requestFocus();
            passwordField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public boolean validateUsername() throws SQLException {
        if (usernameTextField.getText().isBlank() || !usernameTextField.getText().matches("^[a-zA-Z][a-zA-Z0-9_]{4,}$") || usernameTextField.getText().length() < 5 || Database.userExists(usernameTextField.getText())) {
            usernameTextField.requestFocus();
            usernameTextField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public boolean validateFirstName() {
        if (firstNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{2,}$")) {
            return true;
        }

        firstNameTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validateLastName() {
        if (lastNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{2,}$")) {
            return true;
        }

        lastNameTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validateMiddleName() {
        if (middleNameTextField.getText().matches("^[a-zA-Z'\\-\\u00c0-\\u01ff]{1,}$") || middleNameTextField.getText().isBlank()) {
            return true;
        }

        middleNameTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validateEmail() {
        if (emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            return true;
        }

        emailTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validatePhone() {
        if (phoneTextField.getText().matches("^[0-9]{10}$")) {
            return true;
        }

        phoneTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validateBirthDate() {
        try {
            LocalDate.parse(birthDateTextField.getText());
            return true;
        } catch (DateTimeParseException e) {
            birthDateTextField.getStyleClass().add("error");
            return false;
        }
    }

    public boolean validateAddress() {
        if (addressTextField.getText().matches("^[a-zA-Z0-9'\\-\\u00c0-\\u01ff ]{2,}$")) {
            return true;
        }
        
        addressTextField.getStyleClass().add("error");
        return false;
    }

    public boolean validateConfirmPassword() {
        if (passwordField.getText().equals(confirmPasswordField.getText()) && !confirmPasswordField.getText().isBlank()) {
            return true;
        }
        
        confirmPasswordField.getStyleClass().add("error");
        return false;
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

    public String getTitle() {
        return title;
    }
}

// TODO: Validate input!
// TODO: Highlight in red for invalid inputs
// TODO: Add middleName parameter to insertPatient() and insertEmployee()
// TODO: Test multiple popular email domains for email validation
// TODO: Test multiple phone numbers for phone validation
// TODO Test all possible dates for birthDate validation