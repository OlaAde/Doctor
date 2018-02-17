package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 11/16/2017.
 */

public class ChatHead {
    private String UserId, UserName, PictureUrl;
    private int which;

    public long getUnixTime() {
        return UnixTime;
    }

    public void setUnixTime(long unixTime) {
        UnixTime = unixTime;
    }

    private long UnixTime;

    public ChatHead(String userId, String userName, String pictureUrl, long unixTime, int which) {
        UserId = userId;
        UserName = userName;
        PictureUrl = pictureUrl;
        UnixTime = unixTime;
        this.which = which;
    }

    public ChatHead() {

    }

    public int getWhich() {
        return which;
    }

    public void setWhich(int which) {
        this.which = which;
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

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }
}
