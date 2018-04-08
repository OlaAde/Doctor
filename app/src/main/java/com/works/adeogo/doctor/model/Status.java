package com.works.adeogo.doctor.model;

/**
 * Created by ademi on 22/03/2018.
 */

public class Status {
    private String text, imageUrl;

    public Status(String text, String imageUrl) {

        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
