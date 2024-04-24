package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.asu.easydoctor.Database.Role;
import edu.asu.easydoctor.exceptions.ExpiredResetPasswordTokenException;
import edu.asu.easydoctor.exceptions.InvalidResetPasswordTokenException;

public class ResetPasswordTests {

    public static final int USER_ID = 225;
    public static final String OLD_PASSWORD_RAW = "itsJohnny21!";
    public static String oldPassword;
    public static String newPassword;
    public static ResultSet userData;

    @BeforeAll
    public static void setUp() throws Exception {
        Database.connectAsAdmin();
        PreparedStatement statement = Database.connection.prepareStatement("SELECT * FROM users JOIN employees ON users.ID = employees.ID WHERE users.ID = ?;");
        statement.setInt(1, USER_ID);
        userData = statement.executeQuery();
        userData.next();

        oldPassword = userData.getString("password");
        newPassword = oldPassword == Encrypter.SHA256(OLD_PASSWORD_RAW) ? Encrypter.SHA256(OLD_PASSWORD_RAW + "!") : Encrypter.SHA256(OLD_PASSWORD_RAW);
    }

    @AfterEach
    public void tearDownEach() throws Exception {
        PreparedStatement statement;
        statement = Database.connection.prepareStatement("DELETE FROM resetPasswordTokens WHERE userID = ?");
        statement.setInt(1, USER_ID);
        statement.executeUpdate();
    }

    @Test
    @DisplayName("generateResetPasswordToken")
    public void generateResetPasswordToken() throws Exception {
        for (int i = 0; i < 100; i++) {
            int token = Database.generateRandomToken();
    
            assertTrue(token > 0);
            assertTrue(token <= 999999);
        }
    }

    @Test
    @DisplayName("insertResetPasswordToken")
    public void insertResetPasswordToken() throws Exception {
        PreparedStatement statement;

        Database.insertResetPasswordToken(userData.getString("email"), Role.valueOf(userData.getString("role")));
        statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens WHERE userID = ? ORDER BY creationTime DESC LIMIT 1");
        statement.setInt(1, USER_ID);

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        result.close();
    }

    @Test
    @DisplayName("expiredResetToken")
    public void expiredResetToken() throws Exception {
        PreparedStatement statement;

        int userID = 225;
        Database.insertResetPasswordToken(userData.getString("email"), Role.DOCTOR);

        statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens WHERE userID = ? ORDER BY creationTime DESC LIMIT 1");
        statement.setInt(1, userID);

        ResultSet result = statement.executeQuery();
        result.next();

        int token = result.getInt("token");
        result.close();

        statement = Database.connection.prepareStatement("UPDATE resetPasswordTokens SET creationTime = DATE_SUB(NOW(3), INTERVAL " + String.valueOf(Database.TOKEN_LIFESPAN.toMinutes() + 1) + " MINUTE) WHERE token = ?");
        statement.setInt(1, token);
        statement.executeUpdate();

        assertThrows(ExpiredResetPasswordTokenException.class, () -> {
            Database.resetPassword(token, newPassword);
        });
    }

    @Test
    @DisplayName("invalidResetToken")
    public void invalidResetToken() throws Exception {
        PreparedStatement statement;

        int userID = 225;
        Database.insertResetPasswordToken(userData.getString("email"), Role.DOCTOR);

        statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens WHERE userID = ? ORDER BY creationTime DESC LIMIT 1");
        statement.setInt(1, userID);

        ResultSet result = statement.executeQuery();
        result.next();

        int token = result.getInt("token");
        result.close();
        int wrongToken = (token + 1 > 999999) ? token - 1 : token + 1;
        
        assertThrows(InvalidResetPasswordTokenException.class, () -> {
            Database.resetPassword(wrongToken, newPassword);
        });
    }

    @Test
    @DisplayName("resetPassword")
    public void resetPassword() throws Exception {
        PreparedStatement statement;

        int userID = 225;
        Database.insertResetPasswordToken(userData.getString("email"), Role.DOCTOR);

        statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens WHERE userID = ? ORDER BY creationTime DESC LIMIT 1");
        statement.setInt(1, userID);

        ResultSet result = statement.executeQuery();
        result.next();

        int token = result.getInt("token");
        result.close();

        Database.resetPassword(token, newPassword);

        statement = Database.connection.prepareStatement("SELECT password FROM users WHERE password = SHA2(?, 256)");
        statement.setString(1, newPassword);

        result = statement.executeQuery();

        assertTrue(result.next());
        result.close();
    }

    @Test
    @DisplayName("sendResetPasswordEmail")
    public void sendResetPasswordEmail() throws Exception {
        PreparedStatement statement;
        
        int token = Database.insertResetPasswordToken(userData.getString("email"), Role.valueOf(userData.getString("role")));
        statement = Database.connection.prepareStatement("SELECT token FROM resetPasswordTokens WHERE userID = ? AND token = ? ORDER BY creationTime DESC LIMIT 1");
        statement.setInt(1, USER_ID);
        statement.setInt(2, token);

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        result.close();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        Database.connection.close();
        userData.close();
    }
}
