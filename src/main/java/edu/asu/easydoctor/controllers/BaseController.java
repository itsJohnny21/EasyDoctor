package edu.asu.easydoctor.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class BaseController {
    public Stage stage = null;
    public Scene scene = null;
    public String title;
    public boolean resizable;
    public String viewFilename;
    public String styleFilename;

    public void initializeStage() {
        stage.setTitle(title);
        stage.setResizable(resizable);
        stage.centerOnScreen();
        Pane root = (Pane) scene.getRoot();
        double width = root.getPrefWidth();
        double height = root.getPrefHeight();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.toFront();
        stage.setScene(scene);
    }
    

    public void close() {
        stage.close();
        scene = null;
    }
    public abstract void closeAndNullify() throws Exception;
}
