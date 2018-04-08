package com.works.adeogo.doctor.model;

import java.util.Date;

/**
 * Created by ademi on 04/04/2018.
 */

public class TempMessage {
    private String id;
    private String text;
    private Date createdAt;
    private Message.Image image;
    private Message.Voice voice;

    private String userId;
    private String name;
    private String avatar;
    private boolean online;


    public TempMessage(String id, String userId, String name, String avatar, boolean online, String text) {
        this(id, userId, name, avatar, online, text, new Date());
    }

    public TempMessage(String id, String text, Date createdAt, String userId, String name, String avatar, boolean online, Message.Image image, Message.Voice voice) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.image = image;
        this.voice = voice;
    }

    public TempMessage(String id, String userId, String name, String avatar, boolean online, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.createdAt = createdAt;
    }

    public TempMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Message.Image getImage() {
        return image;
    }

    public void setImage(Message.Image image) {
        this.image = image;
    }

    public Message.Voice getVoice() {
        return voice;
    }

    public void setVoice(Message.Voice voice) {
        this.voice = voice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
