package edu.asu.easydoctor.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller {
    public Stage stage = null;
    public Scene scene = null;
    public String title = "Controller";
    public boolean resizable = true;
    public double width = 1000;
    public double height = 1000;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}