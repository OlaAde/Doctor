package com.works.adeogo.doctor.model;

/**
 * Created by ademi on 23/03/2018.
 */

import com.google.firebase.database.Exclude;

import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
public class Message implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType, com.stfalcon.chatkit.commons.models.IMessage /*and this one is for custom content type (in this case - voice message)*/ {

    private String id;
    private String text;
    private Date createdAt;
    private User user;
    private Image image;
    private Voice voice;

    public Message(String id, User patientData, String text) {
        this(id, patientData, text, new Date());
    }

    public Message(String id, String text, Date createdAt, User user, Image image, Voice voice) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
        this.image = image;
        this.voice = voice;
    }

    public Message(String id, User patientData, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = patientData;
        this.createdAt = createdAt;
    }

    public Message() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Exclude
    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public Voice getVoice() {
        return voice;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public static class Voice {

        private String url;
        private int duration;

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public int getDuration() {
            return duration;
        }
    }
}