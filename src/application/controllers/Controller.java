package application.controllers;

import javafx.stage.Stage;

public abstract class Controller {
	
	public String title;
	public Stage stage;

	public double windowMaxWidth;
	public double windowMaxHeight;

	public double windowMinWidth;
	public double windowMinHeight;

	public double preferredWindowWidth;
	public double preferredWindowHeight;

	public boolean resizeable;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public abstract String getTitle();
}
