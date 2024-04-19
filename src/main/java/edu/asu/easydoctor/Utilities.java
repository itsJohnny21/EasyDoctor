package edu.asu.easydoctor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Utilities {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, h:mm a");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
    public static final String MESSAGE_REGEX = "^[\\p{Print}]{1,255}$";
    public static final String NAME_REGEX = "^[a-zA-Z'\\-\\u00c0-\\u01ff]{2,}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PHONE_REGEX = "^[0-9]{10}$";
    public static final String BIRTH_DATE_REGEX = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{4,}$";

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

    public static boolean validate(TextArea textArea, String regex) { //! maybe remove
        if (textArea.getText().isBlank() || !textArea.getText().matches(regex)) {
            textArea.requestFocus();
            textArea.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public static boolean validate(String text, String regex) { //! maybe remove
        if (text.isBlank() || !text.matches(regex)) {
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

    public static String getVisitStatus(Date date, Time time, boolean completed) {
        LocalDateTime dateTime = LocalDateTime.of(convertUTCtoLocal(date), convertUTCtoLocal(time));
        if (completed) {
            return "Completed";
        } else if (LocalDateTime.now().isAfter(dateTime)) {
            return "Missed";
        } else {
            return "Upcoming";
        }
    }

    public static LocalDate convertUTCtoLocal(Date date) {
        return date.toLocalDate().minusDays(LocalDate.now(ZoneId.of("UTC")).getDayOfYear() - LocalDate.now().getDayOfYear());
    }

    public static LocalTime convertUTCtoLocal(Time time) {
        return time.toLocalTime().minusHours(LocalTime.now(ZoneId.of("UTC")).getHour() - LocalTime.now().getHour());
    }

    public static LocalDateTime convertUTCtoLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime().minusHours(LocalDateTime.now(ZoneId.of("UTC")).getHour() - LocalDateTime.now().getHour());
    }

    public static String prettyDate(LocalDate localDate) {
        return localDate.format(dateFormatter);
    }

    public static String prettyDate(Date date) {
        LocalDate localDate = convertUTCtoLocal(date);
        return prettyDate(localDate);
    }

    public static String prettyTime(LocalTime localTime) {
        return localTime.format(timeFormatter);
    }

    public static String prettyTime(Time time) {
        LocalTime localTime = convertUTCtoLocal(time);
        return prettyTime(localTime);
    }

    public static String prettyDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String prettyDateTime(Timestamp tiemstamp) {
        LocalDateTime localDateTime = convertUTCtoLocal(tiemstamp);
        return prettyDateTime(localDateTime);
    }

    public static String prettyName(ResultSet resultSet) throws SQLException {
        return resultSet.getString("firstName") + " " + resultSet.getString("lastName");
    }
}


