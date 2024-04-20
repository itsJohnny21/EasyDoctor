// package edu.asu.easydoctor;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// public class UpdatePatientInfoTest {
//     @BeforeAll   
//     public static void setUp() throws Exception {
//         Database.connect();
//         Database.signIn("updatePatientInfoTest", "updatePatientInfoTest");
//     }
    
//     @Test
//     @DisplayName("updatePreferredDoctorID")
//     public void updatePreferredDoctorID() {
//         int oldPreferredDoctorID = Integer.parseInt(Database.getMy("preferredDoctorID"));
//         Integer newPreferredDoctorID = null;

//         if (oldPreferredDoctorID == 3) {
//             newPreferredDoctorID = 23;
//         } else {
//             newPreferredDoctorID = 3;
//         }


//         Database.YOUR_METHOD_HERE(newPreferredDoctorID);

//         assertEquals(newPreferredDoctorID, Integer.parseInt(Database.getMy("preferredDoctorID")));
//     }
// }