package com.works.adeogo.doctor.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.works.adeogo.doctor.NavigationStartActivity;
import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.TestChatActivity;

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
    private static final String TYPE = "type";
    private static final String WHICH = "which";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            String username = data.get(USERNAME);
            String uid = data.get(UID);
            String text = data.get(TEXT);
            String type = data.get(TYPE);
            String which = data.get(WHICH);
            String imageUrl = data.get(IMAGEURL);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.icon_doctor);

            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            //LED
            mBuilder.setLights(Color.RED, 3000, 3000);
            mBuilder.setContentTitle(username);
            mBuilder.setContentText(text);

            Intent resultIntent = null;

            if (TextUtils.equals(type, "0")) {

                resultIntent = new Intent(this, TestChatActivity.class);
                resultIntent.putExtra("which", Integer.parseInt(which));
                resultIntent.putExtra("client_picture", imageUrl);
                resultIntent.putExtra("client_id", uid);
                resultIntent.putExtra("client_name", username);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification notification = mBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(0, notification);
            } else if (TextUtils.equals(type, "1")) {

                resultIntent = new Intent(this, NavigationStartActivity.class);
                resultIntent.putExtra("start", true);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Notification notification = mBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(0, notification);
            }


        }
    }
}
