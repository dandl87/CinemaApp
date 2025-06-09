package com.delorenzo.Cinema.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalendarUtils {
    public static LocalDate findTheMondayOfTheWeek(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return switch (day) {
            case TUESDAY -> date.minusDays(1);
            case WEDNESDAY -> date.minusDays(2);
            case THURSDAY -> date.minusDays(3);
            case FRIDAY -> date.minusDays(4);
            case SATURDAY -> date.minusDays(5);
            case SUNDAY -> date.minusDays(6);
            default -> date;
        };
    }
}
