package org.heynerr.logging;

public final class LogSanitizer {

    private LogSanitizer() {
        // Empêche l’instanciation
    }

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("[\\n\\r]", "_");
    }
}
