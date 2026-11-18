package utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility functions for date calculations, formatters, and displays.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class DateUtils {

    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat DISPLAY_FORMATTER = new SimpleDateFormat("dd MMM yyyy (EEEE)");
    public static final SimpleDateFormat SHORT_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    public static Date today() {
        return Date.valueOf(LocalDate.now());
    }

    public static Date tomorrow() {
        return Date.valueOf(LocalDate.now().plusDays(1));
    }

    public static Date dayAfterTomorrow() {
        return Date.valueOf(LocalDate.now().plusDays(2));
    }

    public static Date addDays(Date date, int days) {
        if (date == null) return today();
        return Date.valueOf(date.toLocalDate().plusDays(days));
    }

    public static String formatDisplay(Date date) {
        if (date == null) return "N/A";
        return DISPLAY_FORMATTER.format(date);
    }

    public static String formatShort(Date date) {
        if (date == null) return "N/A";
        return SHORT_DATE_FORMATTER.format(date);
    }

    public static String getDayOfWeek(Date date) {
        if (date == null) return "";
        return date.toLocalDate().getDayOfWeek().toString();
    }
}
