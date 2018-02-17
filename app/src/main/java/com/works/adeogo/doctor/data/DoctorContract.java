package com.works.adeogo.doctor.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adeogo on 4/26/2017.
 */

public class DoctorContract {

    private DoctorContract(){
    }
    public static final String AUTHORITY = "com.upload.adeogo.dokita";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_DOCTOR = "doctor";


    public static final class DoctorProfileEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOCTOR).build();
        public static final String TABLE_NAME = "doctor";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DOCTOR_ID = "doctor_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONENUMBER = "phone_number";
        public static final String COLUMN_PICTURE_URL = "image_url";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_SPECIALITY = "speciality";
        public static final String COLUMN_CONSULTATION_FEE = "consultation_fee";
        public static final String COLUMN_SUNDAY = "sunday";
        public static final String COLUMN_MONDAY = "monday";
        public static final String COLUMN_TUESDAY = "tuesday";
        public static final String COLUMN_WEDNESDAY = "wednesday";
        public static final String COLUMN_THURSDAY = "thursday";
        public static final String COLUMN_FRIDAY = "friday";
        public static final String COLUMN_SATURDAY = "saturday";
        public static final String COLUMN_START_HOUR = "start_hour";
        public static final String COLUMN_START_MINUTE = "start_minute";
        public static final String COLUMN_END_HOUR = "end_hour";
        public static final String COLUMN_END_MINUTE = "end_minute";
        public static final String COLUMN_ONLINE = "online";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_OFFICE = "office";
        public static final String COLUMN_CLINIC = "clinic";

    }

}


