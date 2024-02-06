package application.controllers;

import java.io.IOException;

import application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class StartController {
	
	@FXML Button doctorNurseButton;
	@FXML Button patientButton;
	
	@FXML
	public void handlePatientButtonAction(ActionEvent event) {
        try {        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/PatientPortalLoginView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            App.stage.setScene(scene);

            App.stage.setTitle("PatientPortal Login");
            App.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	public void handleDoctorNurseButtonAction(ActionEvent event) {
        try {        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/WorkPortalLoginView.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            App.stage.setScene(scene);

            App.stage.setTitle("WorkPortal Login");
            App.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
