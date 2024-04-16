package edu.asu.easydoctor.controllers;

import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class WelcomeController extends Controller {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button signInButton;
    @FXML public Button signUpButton;
    
    public static WelcomeController instance = null;
    public final static String TITLE = "Welcome";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "WelcomeView";
    public final static String STYLE_FILENAME = "WelcomeView";


    private WelcomeController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static WelcomeController getInstance() {
        if (instance == null) {
            instance = new WelcomeController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        Database.connect();
    }

    @FXML public void handleSignInButtonAction(ActionEvent event) throws Exception {
        SignInController.getInstance().load();
    }

    @FXML public void handleSignUpButtonAction(ActionEvent event) throws Exception {
        SignUpController.getInstance().load();
    }

    public void closeAndNullify() {
        instance = null;
        close();
    }
}