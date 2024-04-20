package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetPatientAsDoctorTest {
    @BeforeAll   
    public static void setUp() throws Exception {
        Database.connect();
        Database.signIn("john123", "john123");
    }

    @Test
    @DisplayName("getPatientIDByFirstNameLastNameBirthDate")
    public void getPatientIDByFirstNameLastNameBirthDate() throws Exception {
        String patientFirstName = "Barbara";
        String patientLastName = "Williams";
        String patientBirthDate = "2000-01-01";
        int patientID = 2;
        
        int retrievedPatientID = Database.getPatientIDByFirstNameLastNameBirthDate(patientFirstName, patientLastName, patientBirthDate);
        assertEquals(patientID, retrievedPatientID);
    }

    @Test
    @DisplayName("getPatientIDByUsername")
    public void getPatientIDByUsername() throws Exception {
        String patientUsername = "barb123";
        int patientID = 2;
        
        int retrievedPatientID = Database.getPatientIDByUsername(patientUsername);
        assertEquals(patientID, retrievedPatientID);
    }

    @Test
    @DisplayName("getPatientIDByEmail")
    public void getPatientIDByEmail() throws Exception {
        String patientEmail = "barb123@gmail.com";
        int patientID = 2;
        
        int retrievedPatientID = Database.getPatientIDByEmail(patientEmail);
        assertEquals(patientID, retrievedPatientID);
    }

    @Test
    @DisplayName("getPatientIDByPhoneNumber")
    public void getPatientIDByPhoneNumber() throws Exception {
        String patientPhoneNumber = "1234567890";

        int patientID = 2;
        
        int retrievedPatientID = Database.getPatientIDByPhoneNumber(patientPhoneNumber);
        assertEquals(patientID, retrievedPatientID);
    }
}
