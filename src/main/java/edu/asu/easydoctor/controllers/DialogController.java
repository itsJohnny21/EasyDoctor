package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import edu.asu.easydoctor.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class DialogController extends Controller {
    public HashMap<String, Object> result = new HashMap<>();

    public HashMap<String, Object> loadDialog(HashMap<String, Object> data) throws IOException, SQLException {
        if (this.scene == null) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource(String.format("views/%s.fxml", viewFilename)));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.scene = scene;
            scene.getStylesheets().add(App.class.getResource(String.format("styles/%s.css", styleFilename)).toExternalForm());
            initializeStage();
        }

        loadDialogHelper(data);
        stage.setScene(this.scene);
        stage.showAndWait();
        return result;
    }

    public abstract void loadDialogHelper(HashMap<String, Object> data) throws SQLException;
}
