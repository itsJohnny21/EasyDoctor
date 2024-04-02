// package application.controllers;

// import application.App;
// import application.Database;
// import application.Database.Ethnicity;
// import application.Database.Race;
// import application.Database.Role;
// import application.Database.Sex;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Alert.AlertType;
// import javafx.scene.control.Button;
// import javafx.scene.control.ChoiceBox;
// import javafx.scene.control.TextField;
// import javafx.scene.input.InputMethodEvent;
// import javafx.scene.input.KeyEvent;
// import javafx.scene.layout.AnchorPane;

// public class SignUpController extends Controller {

//     @FXML AnchorPane rootPane;

//     @FXML TextField usernameTextField;
//     @FXML TextField passwordTextField;
//     @FXML TextField firstNameTextField;
//     @FXML TextField lastNameTextField;
//     @FXML TextField emailTextField;
//     @FXML TextField phoneNumberTextField;
//     @FXML TextField birthDateTextField;
//     @FXML TextField addressTextField;
//     @FXML ChoiceBox<String> ethnicityChoiceBox;
//     @FXML ChoiceBox<String> sexChoiceBox;
//     @FXML ChoiceBox<String> raceChoiceBox;
//     @FXML ChoiceBox<String> roleChoiceBox;
//     @FXML Button signUpButton;

//     public void initialize() {
//         title = "Sign Up";
//         resizable = false;
//         width = 600;
//         height = 430;
//         rootPane.getStylesheets().add(getClass().getResource("/application/styles/SignUpView.css").toExternalForm());

//         for (Ethnicity e : Ethnicity.values()) {
//             ethnicityChoiceBox.getItems().add(e.toString());
//         }

//         for (Race r : Race.values()) {
//             raceChoiceBox.getItems().add(r.toString());
//         }

//         for (Sex s : Sex.values()) {
//             sexChoiceBox.getItems().add(s.toString());
//         }

//         for (Role r : Role.values()) {
//             roleChoiceBox.getItems().add(r.toString());
//         }

//         //! DELETE ME
//         usernameTextField.setText("user1");
//         passwordTextField.setText("123456");
//         firstNameTextField.setText("John");
//         lastNameTextField.setText("Doe");
//         emailTextField.setText("idk");
//         phoneNumberTextField.setText("123");
//         birthDateTextField.setText("2021-01-01");
//         addressTextField.setText("123");

//         roleChoiceBox.setValue(roleChoiceBox.getItems().get(3));
//         sexChoiceBox.setValue(sexChoiceBox.getItems().get(0));
//         ethnicityChoiceBox.setValue(ethnicityChoiceBox.getItems().get(0));
//         raceChoiceBox.setValue(raceChoiceBox.getItems().get(0));
//     }

//     public String getTitle() {
//         return title;
//     }

//     @FXML public void handleSignUpButtonAction() {

//         boolean valid = validate(usernameTextField) && validate(passwordTextField) && validate(firstNameTextField) && validate(lastNameTextField) && validate(emailTextField) && validate(phoneNumberTextField) && validate(addressTextField) && validate(birthDateTextField) && validate(ethnicityChoiceBox) && validate(sexChoiceBox) && validate(raceChoiceBox) && validate(roleChoiceBox);
        
//         if (!valid) return;

//         try {
//             if (Role.valueOf(roleChoiceBox.getValue()).equals(Role.PATIENT)) {
//                 System.out.println("uh");
//                 Database.insertPatient(
//                     usernameTextField.getText(),
//                     passwordTextField.getText(),
//                     firstNameTextField.getText(),
//                     lastNameTextField.getText(),
//                     sexChoiceBox.getValue(),
//                     birthDateTextField.getText(),
//                     emailTextField.getText(),
//                     phoneNumberTextField.getText(),
//                     addressTextField.getText(),
//                     raceChoiceBox.getValue(),
//                     ethnicityChoiceBox.getValue()
//                 );
//             }

//             Alert alert = new Alert(AlertType.INFORMATION);
//             alert.setTitle("Success");
//             alert.setHeaderText("Sign up successful");
//             alert.setContentText("You have successfully signed up!");
//             alert.showAndWait();

//             if (alert.getResult().getText().equals("OK")) {
//                 stage.close();
//                 App.loadPage("TestView", stage);
//             }

//         } catch (Exception e) {
//             Alert alert = new Alert(AlertType.ERROR);
//             alert.setTitle("Error");
//             alert.setHeaderText("An error occurred");
//             alert.setContentText(e.getMessage());
//             alert.showAndWait();
//         }
//     }

//     @FXML public void handleChoiceBoxInputChanged(InputMethodEvent event) {
//         ChoiceBox<String> choiceBox = (ChoiceBox<String>) event.getSource();
//         choiceBox.getStyleClass().remove("error");
//     }

//     @FXML public void handleTextFieldKeyTyped(KeyEvent event) {
//         TextField textField = (TextField) event.getSource();
//         textField.getStyleClass().remove("error");
//     }

//     public boolean validate(TextField textField) {
//         if (textField.getText().isBlank()) {
//             textField.requestFocus();
//             textField.getStyleClass().add("error");
//             return false;
//         }
//         return true;
//     }
    
//     public boolean validate(ChoiceBox<String> choiceBox) {
//         if (choiceBox.getValue() == null) {
//             choiceBox.requestFocus();
//             choiceBox.getStyleClass().add("error");
//             return false;
//         }
//         return true;
//     }
// }

// // TODO: Validate input!
// // TODO: Highlight in red for invalid inputs