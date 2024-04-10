package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller {
    public Stage stage = null;
    public Scene scene = null;
    public String title;
    public boolean resizable;
    public String viewFilename;
    public String styleFilename;
    public double width;
    public double height;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void load(Stage stage) throws IOException {
        if (this.scene == null) {
            this.stage = stage;
            FXMLLoader loader = new FXMLLoader(App.class.getResource(String.format("views/%s.fxml", viewFilename)));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.scene = scene;
            initializeStage();
        }

        loadHelper();
        stage.setScene(this.scene);
        stage.show();
    }

    public void loadHelper() {
        if (this instanceof SignInController) {
            SignInController signInController = (SignInController) this;

            if (!signInController.rememberMeCheckbox.isSelected()) {
                signInController.usernameTextField.clear();
                signInController.passwordField.clear();
            }
        }        
    }

    public void initializeStage() {
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                double decorationWidth = stage.getWidth() - width;
                double decorationHeight = stage.getHeight() - height;
                
                stage.setWidth(width + decorationWidth);
                stage.setHeight(height + decorationHeight);
            }
        });
        scene.getStylesheets().add(App.class.getResource(String.format("styles/%s.css", styleFilename)).toExternalForm());
        stage.centerOnScreen();
    }

    public void close() {
        stage.close();
        scene = null;
    }
}
