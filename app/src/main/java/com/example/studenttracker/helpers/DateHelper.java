package com.example.studenttracker.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateHelper {

    public static DateFormat MONTH_DAY_YEAR_FORMATTER = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    public static DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
}
