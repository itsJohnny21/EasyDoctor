package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexTests {
    public final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    public final Random RANDOM = new Random();

    @Test
    @DisplayName("usernamevalidRegexTest")
    public void usernamevalidRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            assertTrue(line.trim().matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithLetterRegexTest")
    public void usernameStartsWithLetterRegexTest() throws Exception {
        StringBuilder username = new StringBuilder("_username123");
        
        for (int i = 0; i < ALPHABET.length(); i++) {
            username.setCharAt(0, ALPHABET.charAt(i));
            assertTrue((username.toString()).matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithDigitRegexTest")
    public void usernameStartsWithDigitRegexTest() throws Exception {
        StringBuilder username = new StringBuilder("__username123");
        
        for (int i = 0; i < ALPHABET.length(); i++) {
            username.setCharAt(0, Character.forDigit(i, 10));
            assertFalse((username.toString()).matches(Utilities.USERNAME_REGEX));
            
            username.setCharAt(1, Character.forDigit(i, 10));
            assertFalse((username.toString()).matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithUnderscoreRegexTest")
    public void usernameStartsWithUnderscoreRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = "_" + line.trim();
            assertFalse(username.matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithSpecialCharacterRegexTest")
    public void usernameStartsWithSpecialCharacterRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            for (int i = 0; i < SPECIAL_CHARACTERS.length(); i++) {
                String username = SPECIAL_CHARACTERS.charAt(i) + line;
                assertFalse(username.matches(Utilities.USERNAME_REGEX));
            }
        }
    }

    @Test
    @DisplayName("usernameLessThanFourCharactersRegexTest")
    public void usernameLessThanFourCharactersRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = line.trim().substring(0, Math.abs(RANDOM.nextInt()) % 3 + 1);
            assertFalse(username.matches(Utilities.USERNAME_REGEX));
        }
    }   

    @Test
    @DisplayName("usernameEndsWithSpecialCharacterRegexTest")
    public void usernameEndsWithSpecialCharacterRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            for (int i = 0; i < SPECIAL_CHARACTERS.length(); i++) {
                if (SPECIAL_CHARACTERS.charAt(i) == '_') continue;

                String username = line + SPECIAL_CHARACTERS.charAt(i);
                assertFalse(username.matches(Utilities.USERNAME_REGEX));
            }
        }
    }

    @Test
    @DisplayName("usernameEndsWithUnderscoreRegexTest")
    public void usernameEndsWithUnderscoreRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = line.trim() + "_";
            assertTrue(username.matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameEndsWithDigitRegexTest")
    public void usernameEndsWithDigitRegexTest() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < 10; i++) {
                String username = line.trim() + Character.forDigit(i, 10);
                assertTrue(username.matches(Utilities.USERNAME_REGEX));
            }
        }
    }

    @Test
    @DisplayName("usernamesInDatabaseRegexTest")
    public void usernamesInDatabaseRegexTest() throws Exception {
        Database.connectAsAdmin();
        PreparedStatement statement = Database.connection.prepareStatement("SELECT username FROM users");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            assertTrue(username.matches(Utilities.USERNAME_REGEX));
        }
    }
}
