package tests;

import edu.asu.easydoctor.controllers.SignInController;
import edu.asu.easydoctor.controllers.WelcomeController;

public class Bypass {

    public static void loginToPatientPortal(String username, String password) throws Exception {
        WelcomeController welcomeController = WelcomeController.getInstance();
        welcomeController.signInButton.fire();
        
        SignInController signInController = SignInController.getInstance();
        signInController.usernameTextField.setText(username);
        signInController.passwordField.setText(password);
        signInController.signInButton.fire();
    }
    
}
