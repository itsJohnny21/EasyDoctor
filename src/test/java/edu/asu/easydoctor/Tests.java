package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tests {
    @BeforeAll   
    public static void setUp() throws Exception {
        Database.connect();
        Database.signIn("john123", "john123");
    }

    @Test
    @DisplayName("getPatientByID")
    public void getPatientByID() throws Exception {
        int patientID = 2;
        
        ResultSet patient = Database.getPatientByID(patientID);
        assertTrue(patient.next());
        assertEquals(patientID, patient.getInt("ID"));
    }

    @Test
    @DisplayName("getPatientByFirstNameLastNameBirthDate")
    public void getPatientByFirstNameLastNameBirthDate() throws Exception {
        String patientFirstName = "Barbara";
        String patientLastName = "Williams";
        String patientBirthDate = "2000-01-01";

        int patientID = 2;
        
        ResultSet patient = Database.getPatientByFirstNameLastNameBirthDate(patientFirstName, patientLastName, patientBirthDate);
        assertTrue(patient.next());
        assertEquals(patientID, patient.getInt("ID"));
    }

    @Test
    @DisplayName("getPatientByUsername")
    public void getPatientByUsername() throws Exception {
        String patientUsername = "barb123";

        int patientID = 2;
        
        ResultSet patient = Database.getPatientByUsername(patientUsername);
        assertTrue(patient.next());
        assertEquals(patientID, patient.getInt("ID"));
    }

    @Test
    @DisplayName("getPatientByEmail")
    public void getPatientByEmail() throws Exception {
        String patientEmail = "barb123@gmail.com";

        int patientID = 2;
        
        ResultSet patient = Database.getPatientByEmail(patientEmail);
        assertTrue(patient.next());
        assertEquals(patientID, patient.getInt("ID"));
    }

    @Test
    @DisplayName("getPatientByPhoneNumber")
    public void getPatientByPhoneNumber() throws Exception {
        String patientPhoneNumber = "1234567890";

        int patientID = 2;
        
        ResultSet patient = Database.getPatientByPhoneNumber(patientPhoneNumber);
        assertTrue(patient.next());
        assertEquals(patientID, patient.getInt("ID"));
    }
}
