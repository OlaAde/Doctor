package com.works.adeogo.doctor.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.works.adeogo.doctor.MainActivity;
import com.works.adeogo.doctor.R;

import java.util.Map;

/**
 * Created by Adeogo on 11/25/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String USERNAME = "username";
    private static final String IMAGEURL = "imageUrl";
    private static final String EMAIL = "email";
    private static final String UID = "uid";
    private static final String TEXT = "text";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0)
        {
            Map<String, String> data = remoteMessage.getData();

            String username = data.get(USERNAME);
            String imageUrl = data.get(IMAGEURL);
            String email = data.get(EMAIL);
            String uid = data.get(UID);
            String text = data.get(TEXT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);

            mBuilder.setContentTitle(username);
            mBuilder.setContentText(text);

            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}