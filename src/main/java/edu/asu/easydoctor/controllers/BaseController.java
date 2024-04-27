package edu.asu.easydoctor.controllers;

import javafx.scene.Group;
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
        this.stage.setTitle(title);
        this.stage.setResizable(resizable);
        this.stage.centerOnScreen();
        Pane root = (Pane) this.scene.getRoot();
        double width = root.getPrefWidth();
        double height = root.getPrefHeight();
        this.stage.setWidth(width);
        this.stage.setHeight(height);
        this.stage.toFront();
        this.stage.setScene(this.scene);

        this.stage.setOnCloseRequest(event -> {
            try {
                closeAndNullify();
            } catch (Exception e) {
                System.out.println("Error closing dialog");
                e.printStackTrace();
            }
        });
        
        this.stage.setHeight(this.scene.getRoot().prefHeight(-1) + getTitleBarHeight());
    }

    public double getTitleBarHeight() {
        Stage tempStage = new Stage();
        Scene tempScene = new Scene(new Group(), 0, 0);
        tempStage.setScene(tempScene);
        tempStage.show();
        double titleBarHeight = tempStage.getHeight() - tempScene.getHeight();
        tempStage.close();
        return titleBarHeight;
    }

    public void close() {
        stage.close();
        scene = null;
    }
    public abstract void closeAndNullify();
}
