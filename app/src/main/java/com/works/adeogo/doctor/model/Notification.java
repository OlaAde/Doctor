package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 11/25/2017.
 */

public class Notification {
    private String username, imageUrl, email, uid, text, topic,  type;


    public Notification(){}

    public Notification(String username, String uid, String text, String topic, String type){
        this.username = username;
        this.uid = uid;
        this.text = text;
        this.topic = topic;
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getTopic() {
        return topic;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }
}
