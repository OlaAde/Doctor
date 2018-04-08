package com.works.adeogo.doctor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by Adeogo on 10/25/2017.
 */

public class NetworkUtils {

    private static final String API_KEY = "886d15e19f6744a8adb6ff12f9e954f6";
    private static String UrlString = "https://newsapi.org/v2/top-headlines?sources=medical-news-today&apiKey=" + API_KEY;


    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static long getUnixTime() {
        Calendar cal = Calendar.getInstance();

        TimeZone timeZone = cal.getTimeZone();


        Date cals = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long milliseconds = cals.getTime();

        milliseconds = milliseconds + timeZone.getOffset(milliseconds);


        long unixTimeStamp = milliseconds / 1000L;

        return unixTimeStamp;

    }

    public static String convertUnix(long unixTime) {
// convert seconds to milliseconds
        Date date = new Date(unixTime * 1000L);
// the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    public static String getTimeFromUnix(long unixTime) {
// convert seconds to milliseconds
//        unixTime = unixTime + Calendar.getInstance().getTimeZone().getOffset(unixTime);
        Date date = new Date(unixTime * 1000L);
// the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
// give a timezone reference for formatting (see comment at the bottom)

//        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(UrlString).buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
