package application.controllers;

import javafx.stage.Stage;

public abstract class Controller {
    public Stage stage;
    public String title;
    public boolean resizable;
    public double width = 1000;
    public double height = 1000;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getTitle() {
        return title;
    }
}
