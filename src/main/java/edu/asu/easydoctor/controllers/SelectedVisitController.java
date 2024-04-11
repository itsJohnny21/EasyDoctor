package edu.asu.easydoctor.controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.asu.easydoctor.App;
import edu.asu.easydoctor.DataRow;
import edu.asu.easydoctor.DataRow.Employee;
import edu.asu.easydoctor.DataRow.Visit;
import edu.asu.easydoctor.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SelectedVisitController extends Controller {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button closeButton;
    @FXML public Button cancelVisitButton;

    @FXML public Label doctorLabel;
    @FXML public Label dateLabel;
    @FXML public Label dayLabel;
    @FXML public Label timeLabel;
    @FXML public Label reasonLabel;
    @FXML public Label statusLabel;

    @FXML public TextArea descriptionTextArea;

    @FXML public Label addressLabel;

    
    public static SelectedVisitController instance = null;
    public final static String TITLE = "Visit";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "SelectedVisitView";
    public final static String STYLE_FILENAME = "PatientPortalView";

    public Integer rowID;
    public static boolean result;


    private SelectedVisitController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SelectedVisitController getInstance() {
        if (instance == null) {
            instance = new SelectedVisitController();
        }

        return instance;
    }

    public void initialize() throws Exception {
        addressLabel.setText("Address: 2601 E Roosevelt St Phoenix, AZ 85008 United States");

        stage.setOnCloseRequest(event -> {
            close2();
        });
    }

    @FXML public void handleCloseButtonAction(ActionEvent event) throws Exception {
        result = false;
        close2();
    }

    @FXML public void handleCancelVisitButtonAction(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Visit");
        alert.setHeaderText("Are you sure you want to cancel this visit?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();

        if (alert.getResult().getText().equals("OK")) {
            Database.deleteRow("visits", rowID);
            result = true;
            close2();
        }
    }

    public static boolean loadDialog(int rowID) throws Exception {
        if (instance != null) return false;

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(String.format("views/%s.fxml", VIEW_FILENAME)));
        SelectedVisitController controller = SelectedVisitController.getInstance();
        controller.setStage(stage);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(App.class.getResource(String.format("styles/%s.css", STYLE_FILENAME)).toExternalForm());

        controller.rowID = rowID;
        Visit visit = Visit.getFor(rowID);

        Employee doctor = DataRow.Employee.getFor(Integer.parseInt(visit.doctorID.originalValue));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(visit.date.originalValue, formatter);
        DayOfWeek day = dateTime.getDayOfWeek();
        Time time = Time.valueOf(dateTime.toLocalTime());
        Date date = Date.valueOf(dateTime.toLocalDate()); 

        controller.doctorLabel.setText(doctor.firstName.originalValue + " " + doctor.lastName.originalValue);
        controller.dayLabel.setText(day.toString());
        controller.timeLabel.setText(time.toString());
        controller.dateLabel.setText(date.toString());
        controller.reasonLabel.setText(visit.reason.originalValue);
        controller.statusLabel.setText(visit.completed.originalValue == "1" ? "Complete" : "Incomplete");

        controller.descriptionTextArea.setText(visit.description.originalValue);

        stage.showAndWait();
        return result;

    }

    public void close2() {
        stage.close();
        instance = null;
        scene = null;
    }
}