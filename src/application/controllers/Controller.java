package application.controllers;

import javafx.stage.Stage;

public abstract class Controller {
	
	public String title;
	public Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public abstract String getTitle();

}
