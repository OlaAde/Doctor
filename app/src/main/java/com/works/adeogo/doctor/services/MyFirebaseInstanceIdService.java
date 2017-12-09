package com.works.adeogo.doctor.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

/**
 * Created by Adeogo on 11/26/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private String userId;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            userId = user.getUid();

            FirebaseMessaging.getInstance().subscribeToTopic("Notifications/" + userId);
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications/" );
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            reference.child("token").setValue(refreshedToken);
        }

    }
}