package edu.asu.easydoctor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FormattingTests {
        @Test
        @DisplayName("parsePrettyTime")
        public void parsePrettyTime() {
            String prettyTimeString = "12:30 AM";
            LocalTime expected = LocalTime.of(0, 30);

            LocalTime result = Utilities.parsePrettyTime(prettyTimeString);

            assertTrue(result.equals(expected));
            assertTrue(result.toString().equals(expected.toString()));
        }
    }