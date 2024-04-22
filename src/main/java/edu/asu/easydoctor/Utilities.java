package edu.asu.easydoctor;

import java.sql.Date;
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
    public static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{0,}$"; //! Changed
    public static final String DIGITS_ONLY_REGEX = "^[0-9]+$";

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
        if (textField.getText() == null || textField.getText().isBlank() || !textField.getText().matches(regex)) {
            textField.requestFocus();
            textField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public static boolean validate(TextField textField, String regex, int min, int max) { //! maybe remove
        if (textField.getText() == null || textField.getText().isBlank() || textField.getText().length() < min || textField.getText().length() > max || !textField.getText().matches(regex)) {
            textField.requestFocus();
            textField.getStyleClass().add("error");
            return false;
        }

        textField.getStyleClass().remove("error");
        return true;
    }

    public static boolean validate(TextArea textArea, String regex) { //! maybe remove
        if (textArea.getText() == null || textArea.getText().isBlank() || !textArea.getText().matches(regex)) {
            textArea.requestFocus();
            textArea.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    public static boolean validate(String text, String regex) { //! maybe remove
        if (text == null || text.isBlank() || !text.matches(regex)) {
            return false;
        }

        return true;
    }

    public static boolean validate(String text, String regex, int min, int max) {
        if (text == null || text.isBlank() || text.length() < min || text.length() > max || !text.matches(regex)) {
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

    public static String getVisitStatusWorkPortal(LocalDate date, LocalTime time, boolean completed) { //!! remove me use database instead
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (completed) {
            return "Completed";
        } else if (LocalDateTime.now().isAfter(dateTime.plusMinutes(15))) {
            return "Missed";
        } else if (LocalDateTime.now().isAfter(dateTime.minusMinutes(15))) {
            return "Pending";
        } else {
            return "Upcoming";
        }
    }

    public static LocalDate convertUTCtoLocal(Date date) { //! remove
        return date.toLocalDate().minusDays(LocalDate.now(ZoneId.of("UTC")).getDayOfYear() - LocalDate.now().getDayOfYear());
    }

    public static LocalTime convertUTCtoLocal(Time time) { //! remove
        return time.toLocalTime().minusHours(LocalTime.now(ZoneId.of("UTC")).getHour() - LocalTime.now().getHour());
    }

    public static LocalDateTime convertUTCtoLocal(Timestamp timestamp) { //! remove
        return timestamp.toLocalDateTime().minusHours(LocalDateTime.now(ZoneId.of("UTC")).getHour() - LocalDateTime.now().getHour());
    }

    public static String prettyDate(LocalDate localDate) {
        return localDate.format(dateFormatter);
    }

    public static String prettyDate(Date date) { //! remove
        LocalDate localDate = convertUTCtoLocal(date);
        return prettyDate(localDate);
    }

    public static String prettyTime(LocalTime localTime) {
        return localTime.format(timeFormatter);
    }

    public static String prettyTime(Time time) { //! remove
        LocalTime localTime = convertUTCtoLocal(time);
        return prettyTime(localTime);
    }

    public static String prettyDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String prettyDateTime(Timestamp tiemstamp) { //! remove
        LocalDateTime localDateTime = convertUTCtoLocal(tiemstamp);
        return prettyDateTime(localDateTime);
    }
}


