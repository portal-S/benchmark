package org.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z");

    public long dateToTime(String date, ZoneId zoneId) {
        ZoneId.ofOffset("UTC", ZoneOffset.ofHours(10));
        var dateTime = LocalDateTime.parse(date, FORMATTER);
        return ZonedDateTime.of(dateTime, zoneId).toEpochSecond();
    }
}
