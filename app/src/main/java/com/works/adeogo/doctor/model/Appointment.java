package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 10/24/2017.
 */

public class Appointment {
    private String UserId;
    private String DoctorId;
    private String Time;
    private int AcceptanceStatus;
    private int CompletionStatus;
    private int Year;
    private int Month;
    private int Day;
    private String Description;
    private String DoctorName;
    private String ClientName;
    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Appointment(){
    }

    public Appointment(String location) {

    }

    public Appointment(String userId, String doctorId, String time, int acceptanceStatus, int completionStatus, int year, int month,
                       int day, String description, String doctorName, String clientName, String location){
        UserId = userId;
        DoctorId = doctorId;
        Time = time;
        AcceptanceStatus = acceptanceStatus;
        CompletionStatus = completionStatus;
        Year = year;
        Month = month;
        Day = day;
        Description = description;
        DoctorName = doctorName;
        ClientName = clientName;
        Location = location;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public void setAcceptanceStatus(int acceptanceStatus) {
        AcceptanceStatus = acceptanceStatus;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setCompletionStatus(int completionStatus) {
        CompletionStatus = completionStatus;
    }

    public void setDay(int day) {
        Day = day;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getAcceptanceStatus() {
        return AcceptanceStatus;
    }

    public int getCompletionStatus() {
        return CompletionStatus;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public String getUserId() {
        return UserId;
    }

    public int getDay() {
        return Day;
    }

    public int getMonth() {
        return Month;
    }

    public String getTime() {
        return Time;
    }

    public int getYear() {
        return Year;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getDescription() {
        return Description;
    }
}
