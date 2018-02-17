package com.works.adeogo.doctor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Adeogo on 10/25/2017.
 */

public class NetworkUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static long getUnixTime(){
        Calendar cal = Calendar.getInstance();

        TimeZone timeZone =  cal.getTimeZone();


        Date cals =    Calendar.getInstance(TimeZone.getDefault()).getTime();

        long milliseconds =   cals.getTime();

        milliseconds = milliseconds + timeZone.getOffset(milliseconds);


        long unixTimeStamp = milliseconds / 1000L;

        return unixTimeStamp;

    }

}
