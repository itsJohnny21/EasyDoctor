package edu.asu.easydoctor;

import edu.asu.easydoctor.Database.CreationType;
import edu.asu.easydoctor.Database.Ethnicity;
import edu.asu.easydoctor.Database.Race;
import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.Database.Sex;
import edu.asu.easydoctor.controllers.FindPatientController;
import edu.asu.easydoctor.controllers.ForgotUsernamePasswordController;
import edu.asu.easydoctor.controllers.ScheduleVisitController;
import edu.asu.easydoctor.controllers.SignInController;
import edu.asu.easydoctor.controllers.SignUpController;
import edu.asu.easydoctor.controllers.WelcomeController;
import edu.asu.easydoctor.controllers.WorkPortalController;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Bypass {

    public static void toPortal(String username, String password) throws Exception {
        WelcomeController welcomeController = WelcomeController.getInstance();
        welcomeController.signInButton.fire();
        
        SignInController signInController = SignInController.getInstance();
        signInController.usernameTextField.setText(username);
        signInController.passwordField.setText(password);
        signInController.signInButton.fire();
    }

	public static void toWorkPortalScheduleVisit(String username) throws Exception {
		Bypass.toWorkPortal();
		WorkPortalController workPortalController = WorkPortalController.getInstance();

		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Platform.runLater(() -> {
				for (Window window : Window.getWindows()) {
					if (window instanceof Stage) {
						Stage stage = (Stage) window;

						if (stage.getTitle().equals(FindPatientController.TITLE)) {
							FindPatientController findPatientController = FindPatientController.getInstance();
							findPatientController.usernameButton.fire();
							findPatientController.usernameTextField.setText(username);
							findPatientController.findPatientButton.fire();

							new Thread(() -> {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
					
								Platform.runLater(() -> {
									for (Window window2 : Window.getWindows()) {
										if (window2 instanceof Stage) {
											Stage stage2 = (Stage) window2;
					
											if (stage2.getTitle().equals(ScheduleVisitController.TITLE)) {
												ScheduleVisitController scheduleVisitController = ScheduleVisitController.getInstance();
												scheduleVisitController.dateTextField.setText("2021-12-31");
												scheduleVisitController.timeChoiceBox.setValue("12:00 PM");
												scheduleVisitController.creationTypeChoiceBox.setValue(CreationType.ONLINE.toString());
												return;
											}
										}
									}
								});
							}).start();
							findPatientController.doneButton.fire();
							return;
						}
					}
				}
			});
		}).start();

		workPortalController.scheduleVisitButton.fire();
	}

	public static void toWorkPortal() throws Exception {
		Bypass.toPortal("john123", "john123");
	}

	public static void toPatientPortal() throws Exception {
		Bypass.toPortal("john123", "john123");
	}

	public static void toWorkPortalInbox() throws Exception {
		Bypass.toWorkPortal();
		WorkPortalController workPortalController = WorkPortalController.getInstance();
		workPortalController.inboxButton.fire();
	}

	public static void toWorkPortalInboxNewMessage() throws Exception {
		Bypass.toWorkPortalInbox();
		WorkPortalController workPortalController = WorkPortalController.getInstance();
		workPortalController.inboxNewMessageButton.fire();
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
	}
    
}
