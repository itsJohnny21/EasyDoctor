package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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

        deleteVisits();
    }

    public static void deleteVisits() throws Exception {
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
        
        deleteVisits();
        Database.insertVisitFor(PATIENT_ID, creationType, DOCTOR_ID, reason, description, date, time);
        ResultSet visit = Database.getUpcomingVisitFor(PATIENT_ID);

        assertTrue(visit.next());
        assertTrue(visit.getDate("localdate").toLocalDate().equals(LocalDate.parse(date)));
        assertTrue(visit.getTime("localtime").toString().equals(time));

        visit.close();
        deleteVisits();
    }

    @Test
    @DisplayName("scheduleVisitForThePast")
    public void scheduleVisitForThePast() throws Exception {
        CreationType creationType = CreationType.ONLINE;
        String reason = "Checkup";
        String description = "Mental health checkup";
        String date = LocalDate.now().minusDays(1).toString();
        String time = "10:30:00";
        
        deleteVisits();
        assertThrows(SQLException.class, () -> Database.insertVisitFor(PATIENT_ID, creationType, DOCTOR_ID, reason, description, date, time));
        deleteVisits();
    }

    @Test
    @DisplayName("scheduleVisitWhenPatientAlreadyHasUpcomingVisit")
    public void scheduleVisitWhenPatientAlreadyHasUpcomingVisit() throws Exception {
        CreationType creationType = CreationType.ONLINE;
        String reason = "Checkup";
        String description = "Mental health checkup";
        String date = LocalDate.now().plusYears(10).toString();
        String time = "10:30:00";
        
        deleteVisits();
        Database.insertVisitFor(PATIENT_ID, creationType, DOCTOR_ID, reason, description, date, time);
        
        CreationType creationType2 = CreationType.ONLINE;
        String reason2 = "Checkup";
        String description2 = "Mental health checkup";
        String date2 = LocalDate.now().plusYears(10).toString();
        String time2 = "10:30:00";
        
        assertThrows(SQLException.class, () -> Database.insertVisitFor(PATIENT_ID, creationType2, DOCTOR_ID, reason2, description2, date2, time2));

        ResultSet upcomingVisit = Database.getUpcomingVisitFor(PATIENT_ID);
        assertTrue(upcomingVisit.next());
        assertTrue(upcomingVisit.getDate("localdate").toLocalDate().equals(LocalDate.parse(date)));
        assertTrue(upcomingVisit.getTime("localtime").toString().equals(time));

        upcomingVisit.close();
        deleteVisits();
    }

    @Test
    @DisplayName("getVisitTimes")
    public void getVisitTimes() throws Exception {
        PreparedStatement statement = Database.connection.prepareStatement("SELECT startTime, endTime FROM visitTimes WHERE dayOfWeek = 'MONDAY';");
        ResultSet resultSet = statement.executeQuery();
        
        assertTrue(resultSet.next());

        LocalTime startTime = resultSet.getTime("startTime").toLocalTime();
        LocalTime endTime = resultSet.getTime("endTime").toLocalTime();

        ArrayList<LocalTime> visitTimes = new ArrayList<>();

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(Database.VISIT_DURATION.toMinutes())) {
            visitTimes.add(time);
        }
        visitTimes.add(endTime);

        assertTrue(visitTimes.get(0) == startTime);
        assertTrue(visitTimes.size() == Duration.ofHours(1).toMinutes() / Database.VISIT_DURATION.toMinutes() * Duration.between(startTime, endTime).toHours() + 1);
        assertTrue(visitTimes.get(visitTimes.size() - 1) == endTime);
    }
}


