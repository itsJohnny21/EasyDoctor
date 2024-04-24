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
    @DisplayName("usernamevalidRegex")
    public void usernamevalidRegex() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            assertTrue(line.trim().matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithLetterRegex")
    public void usernameStartsWithLetterRegex() throws Exception {
        StringBuilder username = new StringBuilder("_username123");
        
        for (int i = 0; i < ALPHABET.length(); i++) {
            username.setCharAt(0, ALPHABET.charAt(i));
            assertTrue((username.toString()).matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithDigitRegex")
    public void usernameStartsWithDigitRegex() throws Exception {
        StringBuilder username = new StringBuilder("__username123");
        
        for (int i = 0; i < ALPHABET.length(); i++) {
            username.setCharAt(0, Character.forDigit(i, 10));
            assertFalse((username.toString()).matches(Utilities.USERNAME_REGEX));
            
            username.setCharAt(1, Character.forDigit(i, 10));
            assertFalse((username.toString()).matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithUnderscoreRegex")
    public void usernameStartsWithUnderscoreRegex() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = "_" + line.trim();
            assertFalse(username.matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameStartsWithSpecialCharacterRegex")
    public void usernameStartsWithSpecialCharacterRegex() throws Exception {
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
    @DisplayName("usernameLessThanFourCharactersRegex")
    public void usernameLessThanFourCharactersRegex() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = line.trim().substring(0, Math.abs(RANDOM.nextInt()) % 3 + 1);
            assertFalse(username.matches(Utilities.USERNAME_REGEX));
        }
    }   

    @Test
    @DisplayName("usernameEndsWithSpecialCharacterRegex")
    public void usernameEndsWithSpecialCharacterRegex() throws Exception {
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
    @DisplayName("usernameEndsWithUnderscoreRegex")
    public void usernameEndsWithUnderscoreRegex() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/valid_usernames.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;

        while ((line = reader.readLine()) != null) {
            String username = line.trim() + "_";
            assertTrue(username.matches(Utilities.USERNAME_REGEX));
        }
    }

    @Test
    @DisplayName("usernameEndsWithDigitRegex")
    public void usernameEndsWithDigitRegex() throws Exception {
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
    @DisplayName("usernamesInDatabaseRegex")
    public void usernamesInDatabaseRegex() throws Exception {
        Database.connectAsAdmin();
        PreparedStatement statement = Database.connection.prepareStatement("SELECT username FROM users");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            assertTrue(username.matches(Utilities.USERNAME_REGEX));
        }
    }
}
