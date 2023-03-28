package com.municipal.complaintsapp.util;

import java.util.Date;

public class DateUtils {

    public static String getDateString(Date date){
        return android.text.format.DateFormat.format("dd/MM/yyyy",date).toString();
    }


    public static String getDateTimeString(Date date){
        return android.text.format.DateFormat.format("dd/MM/yyyy HH:MM:ss",date).toString();
    }
}
