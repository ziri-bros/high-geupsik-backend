package com.highgeupsik.backend.utils;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyDateUtils {

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
