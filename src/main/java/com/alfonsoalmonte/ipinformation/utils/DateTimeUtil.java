package com.alfonsoalmonte.ipinformation.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String DEFAULT_PATTERN = "HH:mm:ss";

    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String getCurrentArgentinaTimeFormatted() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires");
        ZonedDateTime nowArgentina = ZonedDateTime.now(argentinaZone);
        LocalDateTime nowLocal = nowArgentina.toLocalDateTime();
        return formatLocalDateTime(nowLocal, DEFAULT_PATTERN);
    }
}
