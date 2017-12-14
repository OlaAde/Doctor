package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 11/14/2017.
 */

public class User {
    private String email, password, name, phone, photoUrl;

    public User(){}

    public User(String email, String password, String name, String phone, String photoUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}