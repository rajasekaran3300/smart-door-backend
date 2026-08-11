package com.smartdoor.security.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Formats timestamps the way the UI wants them: "27 July 2026" and "10:35 PM".
 * Kept out of controllers/services so the display format can change in one place.
 */
public final class DateTimeUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    private DateTimeUtil() {}

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_FORMAT);
    }

    public static String formatTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(TIME_FORMAT);
    }
}
