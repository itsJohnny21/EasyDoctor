module edu.asu.easydoctor {
    requires javafx.controls;
    requires java.sql;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.prefs;
    requires javax.mail;
    requires javafx.graphics;
    requires java.desktop;

    opens edu.asu.easydoctor.controllers to javafx.fxml;
    exports edu.asu.easydoctor;
}