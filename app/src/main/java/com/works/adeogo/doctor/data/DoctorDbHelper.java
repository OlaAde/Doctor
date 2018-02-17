package com.works.adeogo.doctor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.works.adeogo.doctor.data.DoctorContract.DoctorProfileEntry;


/**
 * Created by Adeogo on 4/27/2017.
 */

public class DoctorDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "dokitariDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 0;
    public DoctorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE_DOCTOR = "CREATE TABLE "  + DoctorContract.DoctorProfileEntry.TABLE_NAME + " (" +
                DoctorProfileEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DoctorProfileEntry.COLUMN_DOCTOR_ID + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_PHONENUMBER + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_PICTURE_URL + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_SPECIALITY + " TEXT NOT NULL, " +
                DoctorProfileEntry.COLUMN_CONSULTATION_FEE + " TEXT NOT NULL, " +

                DoctorProfileEntry.COLUMN_SUNDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_MONDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_TUESDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_WEDNESDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_THURSDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_FRIDAY + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_SATURDAY + " INTEGER NOT NULL, " +

                DoctorProfileEntry.COLUMN_START_HOUR + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_START_MINUTE + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_END_HOUR + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_END_MINUTE + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_ONLINE + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_HOME + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_OFFICE + " INTEGER NOT NULL, " +
                DoctorProfileEntry.COLUMN_CLINIC + " INTEGER NOT NULL);";


        Log.v("Create_State,ent", CREATE_TABLE_DOCTOR);

        sqLiteDatabase.execSQL(CREATE_TABLE_DOCTOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DoctorProfileEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
