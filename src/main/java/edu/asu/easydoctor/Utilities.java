package edu.asu.easydoctor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class Utilities {

    public static String prettyCapitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }

    public static boolean validate(ChoiceBox<String> choiceBox) { //! maybe remove
        if (choiceBox.getValue() == null) {
            choiceBox.requestFocus();
            choiceBox.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public static boolean validate(TextField textField, String regex) { //! maybe remove
        if (textField.getText().isBlank() || !textField.getText().matches(regex)) {
            textField.requestFocus();
            textField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public static void addClass(Node node, String className) { //! Use this for validation in SignUp and ForgotPassword
        if (!node.getStyleClass().contains(className)) {
            node.getStyleClass().add(className);
        }
    }

    public static void removeClass(Node node, String className) {
        node.getStyleClass().remove(className);
    }

    public static long getCurrentTimeEpochMillis() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long timestampToEpochMillis(Timestamp timestamp) {
        return timestamp.getTime();
    }

    public static String getVisitStatus(LocalDateTime dateTime, boolean completed) {
        if (completed) {
            return "Completed";
        } else if (LocalDateTime.now().isAfter(dateTime)) {
            return "Missed";
        } else {
            return "Upcoming";
        }
    }
}
