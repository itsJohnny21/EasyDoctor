package edu.asu.easydoctor.controllers;

import edu.asu.easydoctor.App;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class WelcomeController extends Controller {
    @FXML public AnchorPane rootPane;
    @FXML public Button signInButton;
    @FXML public Button signUpButton;
    
    public String title = "Welcome";

    public void initialize() throws Exception {
        rootPane.getStylesheets().add(App.class.getResource("styles/WelcomeView.css").toExternalForm());
        width = rootPane.getPrefWidth();
        height = rootPane.getPrefHeight();

        //! Delete me!
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            //signUpButton.fire();
        });
        pause.play();
    }

    @FXML public void handleSignInButtonAction(ActionEvent event) {
        try {
            App.loadPage("SignInView", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleSignUpButtonAction(ActionEvent event) {
        try {
            App.loadPage("SignUpView", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }
}