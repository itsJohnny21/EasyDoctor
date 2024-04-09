package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WelcomeController extends Controller {
    public static WelcomeController instance = null;

    @FXML public AnchorPane rootPane;
    @FXML public Button signInButton;
    @FXML public Button signUpButton;
    
    public final static String TITLE = "Welcome";
    public final static boolean RESIZABLE = false;

    private WelcomeController() {}

    public static WelcomeController getInstance() {
        if (instance == null) {
            instance = new WelcomeController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        stage.setTitle(WelcomeController.TITLE);
        stage.setResizable(WelcomeController.RESIZABLE);
        stage.setWidth(rootPane.getPrefWidth());
        stage.setHeight(rootPane.getPrefHeight());
        stage.centerOnScreen();
        rootPane.getStylesheets().add(App.class.getResource("styles/WelcomeView.css").toExternalForm());
        stage.show();
        
        Database.connect();
    }

    @FXML public void handleSignInButtonAction(ActionEvent event) throws Exception {
        SignInController.load(stage);
    }

    @FXML public void handleSignUpButtonAction(ActionEvent event) throws Exception {
        SignUpController.load(stage);
    }

    public String getTitle() {
        return title;
    }

    public static void load(Stage stage) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/WelcomeView.fxml"));
        WelcomeController controller = WelcomeController.getInstance();

        controller.setStage(stage);
        
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}