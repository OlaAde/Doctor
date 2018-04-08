package com.works.adeogo.doctor.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.Serializable;

/**
 * Created by Adeogo on 11/3/2017.
 */

public class DoctorProfile implements SearchSuggestion {


    private String DoctorId, Name, DoctorPhoneNumber, PictureUrl, Email, Password, Country, City, Speciality, ConsultationFee, About;
    private Status Status;
    private int sunday, monday, tuesday, wednesday, thursday, firday, saturday, startHour, startMinute, endHour, endMinute, onlineConsult, homeVisit, officeVisit, clinic, sex;

    public DoctorProfile() {
    }

    public DoctorProfile(String doctorId, String name, String doctorPhoneNumber, String pictureUrl, String email, String password, String country, String city, String speciality, String consultationFee, String about, Status status, int sunday, int monday, int tuesday, int wednesday, int thursday, int firday, int saturday, int startHour, int startMinute, int endHour, int endMinute, int onlineConsult, int homeVisit, int officeVisit, int clinic, int sex) {
        DoctorId = doctorId;
        Name = name;
        DoctorPhoneNumber = doctorPhoneNumber;
        PictureUrl = pictureUrl;
        Email = email;
        Password = password;
        Country = country;
        City = city;
        Speciality = speciality;
        ConsultationFee = consultationFee;
        About = about;
        Status = status;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.firday = firday;
        this.saturday = saturday;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.onlineConsult = onlineConsult;
        this.homeVisit = homeVisit;
        this.officeVisit = officeVisit;
        this.clinic = clinic;
        this.sex = sex;
    }

    public Status getStatus() {
        return Status;
    }

    public void setStatus(Status status) {
        Status = status;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDoctorPhoneNumber() {
        return DoctorPhoneNumber;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        DoctorPhoneNumber = doctorPhoneNumber;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getConsultationFee() {
        return ConsultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        ConsultationFee = consultationFee;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFirday() {
        return firday;
    }

    public void setFirday(int firday) {
        this.firday = firday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getOnlineConsult() {
        return onlineConsult;
    }

    public void setOnlineConsult(int onlineConsult) {
        this.onlineConsult = onlineConsult;
    }

    public int getHomeVisit() {
        return homeVisit;
    }

    public void setHomeVisit(int homeVisit) {
        this.homeVisit = homeVisit;
    }

    public int getOfficeVisit() {
        return officeVisit;
    }

    public void setOfficeVisit(int officeVisit) {
        this.officeVisit = officeVisit;
    }

    public int getClinic() {
        return clinic;
    }

    public void setClinic(int clinic) {
        this.clinic = clinic;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
