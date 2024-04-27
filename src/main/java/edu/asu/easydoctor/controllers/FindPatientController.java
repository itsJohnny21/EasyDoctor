package edu.asu.easydoctor.controllers;

import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class FindPatientController extends DialogController {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button doneButton;
    @FXML public Button findPatientButton;

    @FXML public Button nameAndBirthButton;
    @FXML public GridPane nameAndBirthPane;

    @FXML public Button usernameButton;
    @FXML public GridPane usernamePane;

    @FXML public Button emailButton;
    @FXML public GridPane emailPane;

    @FXML public Button phoneNumberButton;
    @FXML public GridPane phoneNumberPane;

    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextField birthDateTextField;
    @FXML public TextField usernameTextField;
    @FXML public TextField emailTextField;
    @FXML public TextField phoneNumberTextField;

    @FXML public Label patientLabel;

    public static FindPatientController instance = null;
    public final static String TITLE = "Find Patient";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "FindPatientView";
    public final static String STYLE_FILENAME = "PatientPortalView";
    public Integer patientID;
    public Button currentButton;
    public GridPane currentPane;

    private FindPatientController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static FindPatientController getInstance() {
        if (instance == null) {
            instance = new FindPatientController();
        }

        return instance;
    }

    public void initialize() {
        for (GridPane pane : new GridPane[] {nameAndBirthPane, usernamePane, emailPane, phoneNumberPane}) {
            pane.setVisible(false);
            pane.setDisable(true);
        }

        setCurrentPane(nameAndBirthPane, nameAndBirthButton);
        nameAndBirthButton.fire();
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
        if (data.containsKey("patientID")) {
            System.out.println("Patient ID: " + data.get("patientID"));
            patientID = (Integer) data.get("patientID");
            result.put("patientID", patientID);
            closeAndNullify();
        }
    }

    @FXML public void handleNameAndBirthButtonAction(ActionEvent event) {
        setCurrentPane(nameAndBirthPane, nameAndBirthButton);
    }

    @FXML public void handleUsernameButtonAction(ActionEvent event) {
        setCurrentPane(usernamePane, usernameButton);
    }

    @FXML public void handleEmailButtonAction(ActionEvent event) {
        setCurrentPane(emailPane, emailButton);
    }

    @FXML public void handlePhoneNumberButtonAction(ActionEvent event) {
        setCurrentPane(phoneNumberPane, phoneNumberButton);
    }

    @FXML public void handleKeyTyped(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    @FXML public void handleCancelButtonAction (ActionEvent event) {
        result.remove("patientID");
        patientID = null;
        closeAndNullify();
    }

    @FXML public void handleDoneButtonAction(ActionEvent event) {
        closeAndNullify();
    }

    @FXML public void handleFindPatientButtonAction(ActionEvent event) throws Exception {
        Integer patientIDToFind = null;

        if (currentPane == nameAndBirthPane) {
            if (Utilities.validate(firstNameTextField, Utilities.NAME_REGEX) && Utilities.validate(lastNameTextField, Utilities.NAME_REGEX) && Utilities.validate(birthDateTextField, Utilities.BIRTH_DATE_REGEX)) {
                patientIDToFind = Database.getPatientIDByFirstNameLastNameBirthDate(firstNameTextField.getText(), lastNameTextField.getText(), birthDateTextField.getText());
            }
        } else if (currentPane == usernamePane) {
            if (Utilities.validate(usernameTextField, Utilities.USERNAME_REGEX)) {
                patientIDToFind = Database.getPatientIDByUsername(usernameTextField.getText());
            }
        } else if (currentPane == emailPane) {
            if (Utilities.validate(emailTextField, Utilities.EMAIL_REGEX)) {
                patientIDToFind = Database.getPatientIDByEmail(emailTextField.getText());
            }
        } else if (currentPane == phoneNumberPane) {
            if (Utilities.validate(phoneNumberTextField, Utilities.PHONE_REGEX)) {
                patientIDToFind = Database.getPatientIDByPhoneNumber(phoneNumberTextField.getText());
            }
        }

        if (patientIDToFind == null) {
            addErrorsToFields();
            return;
        }

        patientID = patientIDToFind;
        doneButton.setDisable(false);
        String patientName = Database.getPatientNameFor(patientID);
        patientLabel.setText(patientName);
        result.put("patientID", patientID);
        result.put("patientName", patientName);
    }

    public void addErrorsToFields() {
        if (currentPane == nameAndBirthPane) {
            Utilities.addClass(firstNameTextField, "error");
            Utilities.addClass(lastNameTextField, "error");
            Utilities.addClass(birthDateTextField, "error");
        } else if (currentPane == usernamePane) {
            Utilities.addClass(usernameTextField, "error");
        } else if (currentPane == emailPane) {
            Utilities.addClass(emailTextField, "error");
        } else if (currentPane == phoneNumberPane) {
            Utilities.addClass(phoneNumberTextField, "error");
        }
    }

    public void setCurrentPane(GridPane pane, Button button) {
        if (currentPane == pane) {
            return;
        }

        if (currentPane != null) {
            currentPane.setVisible(false);
            currentPane.setDisable(true);
        }

        currentPane = pane;
        currentPane.setVisible(true);
        currentPane.setDisable(false);

        currentButton = button;
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}