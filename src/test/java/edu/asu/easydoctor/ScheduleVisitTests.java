package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.asu.easydoctor.Database.CreationType;

public class ScheduleVisitTests {
    public static final int DOCTOR_ID = 225;
    public static final int PATIENT_ID = 222;
    public static ResultSet doctorData;
    public static ResultSet patientData;

    @BeforeAll
    public static void setUp() throws Exception {
        Database.connectAsAdmin();
        PreparedStatement statement;

        statement = Database.connection.prepareStatement("SELECT * FROM users JOIN employees ON users.ID = employees.ID WHERE users.ID = ?;");
        statement.setInt(1, DOCTOR_ID);
        doctorData = statement.executeQuery();
        doctorData.next();

        statement = Database.connection.prepareStatement("SELECT * FROM users JOIN patients ON users.ID = patients.ID WHERE users.ID = ?;");
        statement.setInt(1, PATIENT_ID);
        patientData = statement.executeQuery();
        patientData.next();

        statement = Database.connection.prepareStatement("DELETE FROM visits WHERE patientID = ?");
        statement.setInt(1, PATIENT_ID);
        statement.executeUpdate();
        statement.close();
    }

    public void deleteVisits() throws Exception {
        PreparedStatement statement = Database.connection.prepareStatement("DELETE FROM visits WHERE patientID = ?");
        statement.setInt(1, PATIENT_ID);
        statement.executeUpdate();
        statement.close();
    }

    @Test
    @DisplayName("scheduleVisit")
    public void scheduleVisit() throws Exception {
        CreationType creationType = CreationType.ONLINE;
        String reason = "Checkup";
        String description = "Mental health checkup";
        String date = LocalDate.now().plusYears(10).toString();
        String time = "10:30:00";
    
        Database.insertVisitFor(PATIENT_ID, creationType, DOCTOR_ID, reason, description, date, time);
        ResultSet visit = Database.getUpcomingVisitFor(PATIENT_ID);

        assertTrue(visit.next());
        assertTrue(visit.getDate("localdate").toLocalDate().equals(LocalDate.parse(date)));
        assertTrue(visit.getTime("localtime").toString().equals(time));

        visit.close();
        deleteVisits();
    }

    @Test
    @DisplayName("getVisitTimes")
    public void getVisitTimes() throws Exception {
        LocalTime visitTimes[] = Database.getVisitTimes();
        assertTrue(visitTimes.length > 0);
        assertTrue(visitTimes[0] == Database.VISIT_START_TIME);
        assertTrue(visitTimes[visitTimes.length - 1] == Database.VISIT_END_TIME);

        for (int i = 1; i < visitTimes.length; i++) {
            Duration.between(visitTimes[i], visitTimes[i - 1]).equals(Database.VISIT_DURATION);
            assertTrue(visitTimes[i].isAfter(visitTimes[i - 1]));
            assertTrue(Duration.between(visitTimes[i - 1], visitTimes[i]).toMinutes() == Database.VISIT_DURATION.toMinutes());
        }

    }
}


