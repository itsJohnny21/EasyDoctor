module edu.asu.easydoctor {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    requires java.prefs;

    opens edu.asu.easydoctor.controllers to javafx.fxml;
    exports edu.asu.easydoctor;
}