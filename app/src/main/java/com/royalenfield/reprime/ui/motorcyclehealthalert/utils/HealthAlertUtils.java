package com.royalenfield.reprime.ui.motorcyclehealthalert.utils;

import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class HealthAlertUtils {
    public static final String YEAR_PATTERN = "yyyy";

    public static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMM");

    public static final DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("d,");


    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d,");
    public static final DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yyyy");

    public static final DateTimeFormatter DATE_FORMAT_FOR_SOCKET = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DATE_FORMAT_FOR_TRIP_LISTING = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm");
}
