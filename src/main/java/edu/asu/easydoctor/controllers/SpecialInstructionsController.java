package edu.asu.easydoctor.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class SpecialInstructionsController extends DialogController {
    
    @FXML public AnchorPane rootPane;
    @FXML public Button cancelButton;
    @FXML public Button doneButton;
    @FXML public TextArea specialInstructionsTextArea;

    public static SpecialInstructionsController instance = null;
    public final static String TITLE = "Special Instructions";
    public final static boolean RESIZABLE = false;
    public final static String VIEW_FILENAME = "SpecialInstructionsView";
    public final static String STYLE_FILENAME = "PatientPortalView";

    private SpecialInstructionsController() {
        title = TITLE;
        resizable = RESIZABLE;
        viewFilename = VIEW_FILENAME;
        styleFilename = STYLE_FILENAME;
    }

    public static SpecialInstructionsController getInstance() {
        if (instance == null) {
            instance = new SpecialInstructionsController();
        }

        return instance;
    }

    public void loadDialogHelper(HashMap<String, Object> data) throws SQLException, IOException {}

    public void closeAndNullify() {
        instance = null;
        close();
    }
}
