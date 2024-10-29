package com.example.Mangxahoi.utils;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {
    public static  ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
    public static Instant getStartOfWeek() {
        ZonedDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return startOfWeek.toInstant();
    }

    public static Instant getEndOfWeek() {
        ZonedDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).minusSeconds(1);
        return endOfWeek.toInstant();
    }
}
