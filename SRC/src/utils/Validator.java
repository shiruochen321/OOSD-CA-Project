package utils;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isPositiveNumeric(String str) {
        if (str == null) return false;
        try {
            double d = Double.parseDouble(str);
            return d > 0;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
