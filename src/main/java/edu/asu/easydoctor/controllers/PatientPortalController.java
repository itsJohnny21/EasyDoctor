package edu.asu.easydoctor.controllers;

import java.io.IOException;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PatientPortalController 	extends Controller {

	@FXML public Button button1;
	@FXML public Label usernameLabel;
	@FXML public Label firstNameLabel;
	@FXML public Label lastNameLabel;
	@FXML public Label fatherLabel;
	@FXML public Label motherLabel;
	@FXML public Label dobLabel;
	@FXML public Label medsLabel;
	@FXML public Label phoneLabel;
	@FXML public Label insuranceLabel;
	@FXML public Label addressLabel;
	@FXML public Label pharmLabel;
	@FXML public Label doctorLabel;
	@FXML public Label emailLabel;
	public static PatientPortalController instance = null;
	public static void main (String args) {
		
		System.out.println("hello");
		
		
	}
	public void initialize() throws Exception{
		stage.setTitle(TITLE);
		Database.connect();
		syncinfo();
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

	
	
public void syncinfo() throws Exception {
	
	Database.connect();
    Database.signIn("barb123", "barb123");
    System.out.println(Database.userID);
    String firstName = Database.getMy("firstName");
    String lastName = Database.getMy("lastName");
    String phoneNumber = Database.getMy("phone");
    String sex = Database.getMy("sex");
    String birthDate = Database.getMy("birthDate");
    String email = Database.getMy("email");
    String address = Database.getMy("address");
    String preferredDoctorID = Database.getMy("preferredDoctorID");
    String doctorFullName = Database.getMyDoctor();
    String bloodType = Database.getMy("bloodType");
    String height = Database.getMy("height");
    String weight = Database.getMy("weight");
    String race = Database.getMy("race");
    String insuranceProvider = Database.getMy("insuranceProvider");
    String insuranceID = Database.getMy("insuranceID");
    String emergencyContactName = Database.getMy("emergencyContactName");
    String emergencyContactPhone = Database.getMy("emergencyContactPhone");
    String motherFirstName = Database.getMy("motherFirstName");
    String motherLastName = Database.getMy("motherLastName");
    String fatherFirstName = Database.getMy("fatherFirstName");
    String fatherLastName = Database.getMy("fatherLastName");
    
    
    
//    @FXML public Button button1;
//	@FXML public Label usernameLabel;
//	@FXML public Label firstNameLabel;
//	@FXML public Label lastNameLabel;
//	@FXML public Label fatherLabel;
//	@FXML public Label motherLabel;
//	@FXML public Label dobLabel;
//	@FXML public Label medsLabel;
//	@FXML public Label phoneLabel;
//	@FXML public Label insuranceLabel;
//	@FXML public Label addressLabel;
//	@FXML public Label pharmLabel;
//	@FXML public Label doctorLabel;
    System.out.println(firstName);
    usernameLabel.setText(Database.getMy("username"))  ;
    emailLabel.setText(Database.getMy("email"))  ;
    firstNameLabel.setText(Database.getMy("firstName"))  ;
    lastNameLabel.setText(Database.getMy("lastName"))  ;
   fatherLabel.setText(Database.getMy("fatherFirstName") + Database.getMy("fatherLastName"))  ;
    motherLabel.setText(Database.getMy("motherFirstName") + Database.getMy("motherLastName"))  ;
   dobLabel.setText(Database.getMy("birthDate"))  ;
   //medsLabel.setText(Database.getMy("birthDate"))  ;
   phoneLabel.setText(Database.getMy("phone"))  ;
    insuranceLabel.setText(Database.getMy("insuranceID"))  ;
   addressLabel.setText(Database.getMy("address"))  ;
   // pharmLabel.setText(Database.getMy("address"))  ;
   doctorLabel.setText(Database.getMyDoctor())  ;
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