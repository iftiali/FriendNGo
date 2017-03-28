package com.friendngo.friendngo;


import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by krishna on 2017-01-30.
 */

public class ValidationClass {

    public final static boolean isValidEmail(CharSequence target) {

        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isNullCheck(String username, String password) {

        if (username.equals("") || password.equals("")) {
            return false;
        } else {
            return true;
        }
    }
    public static String getFormattedTime(Date date){
        if(date == null)
        {
        return null;}
        else{
            return new SimpleDateFormat("HH:mm a").format(date);
        }
    }

    public static String getFormattedDate(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day=cal.get(Calendar.DATE);

        if(!((day>10) && (day<19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("EEE d'st', yyyy").format(date);
                case 2:
                    return new SimpleDateFormat("EEE d'nd', yyyy").format(date);
                case 3:
                    return new SimpleDateFormat("EEE d'rd', yyyy").format(date);
                default:
                    return new SimpleDateFormat("EEE d'th', yyyy").format(date);
            }
        return new SimpleDateFormat("EEE d'th', yyyy").format(date);
    }
    public static String getCurrentTimezoneOffset() {

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
        Log.d("Time zone",offset);
        return offset;
    }

}
