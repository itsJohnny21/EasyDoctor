module edu.asu.easydoctor {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    requires java.prefs;
    requires javax.mail;
    requires javafx.graphics;
    requires java.desktop;
    // requires java.junit.jupiter;

    opens edu.asu.easydoctor.controllers to javafx.fxml;
    exports edu.asu.easydoctor;
}