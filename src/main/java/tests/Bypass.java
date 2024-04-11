package tests;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.asu.easydoctor.Database;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.controllers.ForgotUsernamePasswordController;
import edu.asu.easydoctor.controllers.ManagerCredentialsController;
import edu.asu.easydoctor.controllers.ResetPasswordController;
import edu.asu.easydoctor.controllers.SignInController;
import edu.asu.easydoctor.controllers.SignUpController;
import edu.asu.easydoctor.controllers.WelcomeController;

public class Bypass {

    public static void toPatientPortal(String username, String password) throws Exception {
        WelcomeController welcomeController = WelcomeController.getInstance();
        welcomeController.signInButton.fire();
        
        SignInController signInController = SignInController.getInstance();
        signInController.usernameTextField.setText(username);
        signInController.passwordField.setText(password);
        signInController.signInButton.fire();
    }

    public static void toResetPasswordDialog(String newPassword) throws Exception {
        WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signInButton.fire();
		SignInController signInController = SignInController.getInstance();
		signInController.forgotUsernamePasswordHyperLink.fire();
		ForgotUsernamePasswordController forgotUsernamePasswordController = ForgotUsernamePasswordController.getInstance();
		forgotUsernamePasswordController.roleChoiceBox.setValue(Role.PATIENT.toString());
		forgotUsernamePasswordController.emailTextField.setText("jsalazar6421@gmail.com");
		forgotUsernamePasswordController.sendEmailButton.fire();

		PreparedStatement statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens ORDER BY creationTime DESC LIMIT 1");
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		int token = resultSet.getInt("token");

		ResetPasswordController resetPasswordController = ResetPasswordController.getInstance();
		resetPasswordController.resetPasswordTokenTextField.setText(String.valueOf(token));
		resetPasswordController.newPasswordField.setText(newPassword);
		resetPasswordController.confirmNewPasswordField.setText(newPassword);
    }

	public static void toMangerCredentialsDialog(String username, String password) throws Exception {
		WelcomeController welcomeController = WelcomeController.getInstance();
		welcomeController.signUpButton.fire();
		SignUpController signUpController = SignUpController.getInstance();
		signUpController.usernameTextField.setText(username);
		signUpController.roleChoiceBox.setValue(Role.DOCTOR.toString());
		signUpController.passwordField.setText(password);
		signUpController.confirmPasswordField.setText(password);
		signUpController.nextButton.fire();

		signUpController.firstNameTextField.setText("null");
		signUpController.lastNameTextField.setText("null");
		signUpController.emailTextField.setText("null@gmail.com");
		signUpController.phoneTextField.setText("1234567890");
		signUpController.addressTextField.setText("1234 Test St");
		signUpController.birthDateTextField.setText("2000-01-01");
		signUpController.raceChoiceBox.setValue(Race.ASIAN.toString());
		signUpController.sexChoiceBox.setValue(Sex.MALE.toString());
		signUpController.ethnicityChoiceBox.setValue(Ethnicity.HISPANIC.toString());
		signUpController.signUpButton.fire();
		ManagerCredentialsController managerCredentialsController = ManagerCredentialsController.getInstance();
		managerCredentialsController.managerUsernameTextField.setText("itsJohnnyDoctor21");
		managerCredentialsController.managerPasswordField.setText("meatCuh21!");
	}
    
}