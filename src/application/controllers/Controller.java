package application.controllers;

import javafx.stage.Stage;

public abstract class Controller {
    public Stage stage;
    public String title;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public abstract String getTitle();
}
