package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 11/16/2017.
 */

public class ChatHead {
    private String UserId;
    private String UserName;

    public ChatHead() {
    }

    public ChatHead(String userId, String userName) {
        UserId = userId;
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
