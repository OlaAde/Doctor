package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 11/3/2017.
 */

public class DoctorProfile {
    private String DoctorId;
    private String Name;
    private String DoctorPhoneNumber;
    private String PictureUrl;
    private String Email;
    private String Password;
    private String Country;
    private String City;
    private String Speciality;

    private String HighestCost;
    private String LowestCost;
    private String SpecialCost;



    public DoctorProfile(){}

    public DoctorProfile(String doctorId, String email, String password, String name, String doctorPhoneNumber, String pictureUrl, String country, String city, String speciality, String highestCost, String lowestCost, String specialCost){
        DoctorId = doctorId;
        Name = name;
        DoctorPhoneNumber = doctorPhoneNumber;
        PictureUrl = pictureUrl;
        Country = country;
        City = city;
        Email = email;
        Password = password;
        Speciality = speciality;
        HighestCost = highestCost;
        LowestCost = lowestCost;
        SpecialCost = specialCost;
    }

    public void setHighestCost(String highestCost) {

        HighestCost = highestCost;
    }

    public void setLowestCost(String lowestCost) {
        LowestCost = lowestCost;
    }

    public void setSpecialCost(String specialCost) {
        SpecialCost = specialCost;
    }

    public String getHighestCost() {
        return HighestCost;
    }

    public String getLowestCost() {
        return LowestCost;
    }

    public String getSpecialCost() {
        return SpecialCost;
    }
    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        DoctorPhoneNumber = doctorPhoneNumber;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public String getDoctorPhoneNumber() {
        return DoctorPhoneNumber;
    }

    public String getCity() {
        return City;
    }

    public String getCountry() {
        return Country;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getName() {
        return Name;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public String getDoctorId() {
        return DoctorId;
    }
}
