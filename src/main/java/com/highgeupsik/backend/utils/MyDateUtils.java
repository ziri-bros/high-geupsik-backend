package com.highgeupsik.backend.utils;

import java.time.LocalDate;

public class MyDateUtils {

    private MyDateUtils() {
    }

    public static String[] getWeekDates() {
        LocalDate now = LocalDate.now();
        String[] dates = new String[6];
        for (int i = 0; i < 7; i++) {
            int day = now.getDayOfWeek().getValue();
            if (day < 6) {
                dates[day] = now.toString().replaceAll("-", "");
            }
            now = now.plusDays(1);
        }
        return dates;
    }
}
