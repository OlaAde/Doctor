package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 10/28/2017.
 */

public class Question {
    private String text;
    private String name;
    private int you;

    public Question(){}

    public Question(String text, String name, int you){
        this.text = text;
        this.name = name;
        this.you = you;
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
