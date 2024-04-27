package edu.asu.easydoctor.controllers;

import edu.asu.easydoctor.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class Controller extends BaseController {

    public void load() throws Exception {
        if (this.scene == null) {
            this.stage = App.primaryStage;
            FXMLLoader loader = new FXMLLoader(App.class.getResource(String.format("views/%s.fxml", viewFilename)));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.scene = scene;
            this.scene.getStylesheets().add(App.class.getResource(String.format("styles/%s.css", styleFilename)).toExternalForm());
            initializeStage();
        }
        
        loadHelper();
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public abstract void loadHelper() throws Exception;
}