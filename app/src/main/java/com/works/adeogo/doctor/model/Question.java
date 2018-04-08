package com.works.adeogo.doctor.model;

import java.util.Date;

/**
 * Created by Adeogo on 10/28/2017.
 */

public class Question {
    private String text;
    private String name;
    private String photoUrl;
    private int you;
    private long messageTime;

    public Question(){}


    public Question(String text, String name, int you, String photoUrl){
        this.text = text;
        this.name = name;
        this.you = you;
        this.photoUrl = photoUrl;
        messageTime = new Date().getTime();
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public int getYou() {
        return you;
    }

    public void setYou(int you) {
        this.you = you;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
