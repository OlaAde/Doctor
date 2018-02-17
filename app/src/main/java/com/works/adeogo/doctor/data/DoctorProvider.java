package com.works.adeogo.doctor.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.works.adeogo.doctor.data.DoctorContract.DoctorProfileEntry;

/**
 * Created by Adeogo on 4/27/2017.
 */

public class DoctorProvider extends android.content.ContentProvider {
    public static final int DOCTOR  = 100;
    public static final int DOCTOR_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */

        uriMatcher.addURI(DoctorContract.AUTHORITY, DoctorContract.PATH_DOCTOR, DOCTOR);
        uriMatcher.addURI(DoctorContract.AUTHORITY, DoctorContract.PATH_DOCTOR + "/#", DOCTOR_WITH_ID);


        return uriMatcher;
    }

    private DoctorDbHelper mDoctorDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDoctorDbHelper = new DoctorDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db =  mDoctorDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case DOCTOR:
                retCursor =  db.query(DoctorProfileEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDoctorDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case DOCTOR:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(DoctorProfileEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(DoctorProfileEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

       /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case DOCTOR:
                numRowsDeleted = mDoctorDbHelper.getWritableDatabase().delete(
                        DoctorProfileEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int numOfRowsChanged;
        switch (match) {

            case DOCTOR:
                numOfRowsChanged = mDoctorDbHelper.getWritableDatabase().update(DoctorProfileEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

        if(numOfRowsChanged !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numOfRowsChanged;

    }

    @Override
    public int bulkInsert(@NonNull Uri uri,  ContentValues[] values) {
        final SQLiteDatabase db = mDoctorDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case DOCTOR:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DoctorProfileEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;




            default:
                return super.bulkInsert(uri, values);
        }
    }
}
