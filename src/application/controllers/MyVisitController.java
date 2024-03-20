package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import application.Database;
import application.FormBuilder.Form;
import application.FormBuilder.FormData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MyVisitController extends Controller {

    @FXML public AnchorPane mainPane;

    public void initialize() throws Exception {
        title = "My Visit";

        preferredWindowWidth = 700;
        preferredWindowHeight = 700;

        windowMinWidth = 700;
        windowMinHeight = 700;

        windowMaxWidth = 700;
        windowMaxHeight = 700;

        resizeable = false;

        PreparedStatement preparedStatement = Database.connection.prepareStatement("SELECT * FROM visits WHERE ID = ?");
        preparedStatement.setInt(1, PatientPortalController.currentVisitID);
    
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
    
        ArrayList<String[]> fields = new ArrayList<String[]>();
        fields.add(new String[] {"ID", String.valueOf(PatientPortalController.currentVisitID), "blank", "blank"}); //! FIX ME
        fields.add(new String[] {"Date", resultSet.getString("Date"), "blank", "blank"});
        fields.add(new String[] {"patientID", resultSet.getString("patientID"), "blank", "blank"});
        fields.add(new String[] {"creationTime", resultSet.getString("creationTime"), "blank", "blank"});
    
        FormData visitData = new FormData("Visit", fields);
        Form visitForm = new Form(visitData, windowMaxWidth, 50, "-fx-background-color: #cc7878", "-fx-background-color: #cc7878", null);
        visitForm.generate();
        mainPane.getChildren().add(visitForm);
    }


    @FXML public void handleCloseButtonAction(ActionEvent event) {
        System.out.println("Close button clicked");
        PatientPortalController.currentVisitID = null;
        stage.close();
    }

    public String getTitle() {
        return title;
    }
}


