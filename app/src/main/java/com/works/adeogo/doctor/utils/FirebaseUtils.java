package com.works.adeogo.doctor.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Adeogo on 11/25/2017.
 */

public class FirebaseUtils {
    public static DatabaseReference getNotificationRef(String receiverId){
        return FirebaseDatabase.getInstance().getReference("Notifications").child(receiverId).push();
    }
}