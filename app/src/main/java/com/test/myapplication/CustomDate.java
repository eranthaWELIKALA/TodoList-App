package com.test.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CustomDate {
    private static java.util.Date date = null;
    public static java.util.Date GetDate(String dateString) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            date = df.parse(dateString);
        }
        catch (Exception ignored) {
        }
        return date;
    }
    public static String getStringDate(java.util.Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int monthInt = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        
        String monthString = "" + monthInt;
        if(monthInt < 10){
            monthString = "0" + monthString; //add leading zero to the month
        }
        String dayString = "" + dayOfMonth;
        if(dayOfMonth < 10){
            dayString = "0" + dayOfMonth; //add leading zero to the date
        }
        return String.format(Locale.ENGLISH,"%s-%s-%d", dayString, monthString, year);
    }
}
