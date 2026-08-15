package com.smartdoor.security.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Central date/time utility for the Smart Door application.
 * All application-generated timestamps use India Standard Time.
 */
public final class DateTimeUtil {

    private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("hh:mm a");

    private DateTimeUtil() {}

    /**
     * Returns the current date/time in IST.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(INDIA_ZONE);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_FORMAT);
    }

    public static String formatTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(TIME_FORMAT);
    }
}