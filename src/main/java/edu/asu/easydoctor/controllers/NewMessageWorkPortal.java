package edu.asu.easydoctor.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Utilities;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NewMessageWorkPortal extends DialogController {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button sendButton;
    @FXML public Button findPatientButton;

    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextField birthDateTextField;
    @FXML public TextField usernameTextField;
    @FXML public TextField emailTextField;
    @FXML public TextField phoneNumberTextField;

    @FXML public Label patientLabel;
    @FXML public TextArea messageTextArea;

    public static NewMessageWorkPortal instance = null;
    public final static String TITLE = "Send Message";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "NewMessageWorkPortalView";
    public final static String STYLE_FILENAME = "PatientPortalView";
    public Integer patientID;

    private NewMessageWorkPortal() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static NewMessageWorkPortal getInstance() {
        if (instance == null) {
            instance = new NewMessageWorkPortal();
        }

        return instance;
    }

    public void initialize() throws Exception {
        ObjectProperty<Integer> patientIDProperty = new SimpleObjectProperty<>(patientID);
        BooleanBinding patientIDBinding = patientIDProperty.isNull();
        sendButton.disableProperty().bind(patientIDBinding);
    }

    @FXML public void handleKeyTyped(ActionEvent event) throws Exception { //! Move this function to the base controlelr since every controller uses this
        TextField textField = (TextField) event.getSource();
        Utilities.removeClass(textField, "error");
    }

    @FXML public void handleCancelButtonAction (ActionEvent event) throws Exception {
        closeAndNullify();
    }

    @FXML public void handleSendButton(ActionEvent event) throws Exception {
        if (Utilities.validate(messageTextArea, styleFilename)) {
            System.out.println("messages sent: " + messageTextArea.getText());
            Database.sendMessageTo(patientID, messageTextArea.getText());
        }
    }

    @FXML public void handleFindPatientButtonAction(ActionEvent event) throws Exception {
        boolean patientFound = false;
        ResultSet patient = null;

        if (!firstNameTextField.getText().isBlank() && !lastNameTextField.getText().isBlank() && !birthDateTextField.getText().isBlank() && usernameTextField.getText().isBlank() && emailTextField.getText().isBlank() && phoneNumberTextField.getText().isBlank()) {
            if (Utilities.validate(firstNameTextField, Utilities.NAME_REGEX) && Utilities.validate(lastNameTextField, Utilities.NAME_REGEX) && Utilities.validate(birthDateTextField, Utilities.BIRTH_DATE_REGEX)) {
                patient = Database.getPatientByFirstNameLastNameBirthDate(firstNameTextField.getText(), lastNameTextField.getText(), birthDateTextField.getText());
                patientFound = patient.next();
                if (!patientFound) {
                    Utilities.addClass(firstNameTextField, "error");
                    Utilities.addClass(lastNameTextField, "error");
                    Utilities.addClass(birthDateTextField, "error");
                }
            }
        }
        
        if (!usernameTextField.getText().isBlank()) {
            if (Utilities.validate(usernameTextField, Utilities.USERNAME_REGEX)) {
                patient = Database.getPatientByUsername(usernameTextField.getText());
                patientFound = patient.next();

                if (!patientFound) {
                    Utilities.addClass(usernameTextField, "error");
                }
            }
        }
        
        if (!emailTextField.getText().isBlank()) {
            if (Utilities.validate(emailTextField, Utilities.EMAIL_REGEX)) {
                patient = Database.getPatientByEmail(emailTextField.getText());
                patientFound = patient.next();

                if (!patientFound) {
                    Utilities.addClass(emailTextField, "error");
                }
            }
        }
        
        if (!phoneNumberTextField.getText().isBlank()) {
            if (Utilities.validate(phoneNumberTextField, Utilities.PHONE_REGEX)) {
                patient = Database.getPatientByPhoneNumber(phoneNumberTextField.getText());
                patientFound = patient.next();

                if (!patientFound) {
                    Utilities.addClass(phoneNumberTextField, "error");
                }
            }
        }

        if (!patientFound) return;

        patientID = patient.getInt("ID");
        patientLabel.setText(Utilities.prettyName(patient));
        patient.close();
        System.out.println("Patient ID: " + patientID); //! remove me
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException {
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}