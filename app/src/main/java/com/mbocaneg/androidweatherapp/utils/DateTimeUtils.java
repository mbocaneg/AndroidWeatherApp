package com.mbocaneg.androidweatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Util class that provides helpul date/time related functions
 */
public final class DateTimeUtils {

    /**
     * Check if a Date object's time occurs around midday
     */
    public static Boolean isMidday(Date dateObject) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObject);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour == 12 || hour == 13 || hour == 14){
            return true;
        }

        return false;
    }

    /**
     * Format a Date object into a user readable string
     */
    public static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();

        timeFormat.setTimeZone(tz);
        calendar.setTime(dateObject);

        return timeFormat.format(calendar.getTime());

    }

    /**
     * Return a string corresponding to a Date object's Day
     */
    public static String extractDay(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();

        timeFormat.setTimeZone(tz);
        calendar.setTime(dateObject);

        return timeFormat.format(calendar.getTime());

    }
}
