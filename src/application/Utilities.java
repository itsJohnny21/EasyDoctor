package application;

public class Utilities {

    public static String prettyCapitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }
}
