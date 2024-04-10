package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PatientPortalController 	extends Controller {

	@FXML public Button button1;
	public static PatientPortalController instance = null;
	public static void main (String args) {
		
		System.out.println("hello");
		
		
	}
	public void initialize() throws Exception{
		stage.setTitle(TITLE);
	}
	
	
	@FXML public void handleButtonAction() {
		
		System.out.println("Buttonclicked");
	}
	
	public final static String TITLE = "PatientPortal";
	private PatientPortalController() {}
	public static PatientPortalController getInstance() {
			
		
	if (instance == null)
	{
		instance = new PatientPortalController();
		
	}
	
	return instance;
	
		}



public static void load(Stage stage) throws IOException {
	PatientPortalController controller = PatientPortalController.getInstance();

    if (controller.scene == null) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/PatientPortalView2.fxml"));
        controller.setStage(stage);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controller.setScene(scene);
    }

    stage.setScene(controller.scene);
    stage.show();
}

}