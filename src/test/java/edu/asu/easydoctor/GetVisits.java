package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetVisits {
    @BeforeAll   
    public static void setUp() throws Exception {
        Database.connect();
        Database.signIn("john123", "john123");
    }

    @Test
    @DisplayName("getVisitByID")
    public void getVisitByID() throws Exception {
        int visitID = 20;

        ResultSet visit = Database.getVisit(visitID);
        assertTrue(visit.next());
        int retrievedVisitID = visit.getInt("ID");
        assertEquals(visitID, retrievedVisitID);
    }
}
