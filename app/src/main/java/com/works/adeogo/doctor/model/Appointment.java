package com.works.adeogo.doctor.model;

/**
 * Created by Adeogo on 10/24/2017.
 */


public class Appointment {
    private String UserId, DoctorId, Time, DoctorPhone, ClientPhone, DoctorName, ClientName, Location, Message;
    private int Year, Month, Day, Status;


    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Appointment(){
    }

    public Appointment(String userId, String doctorId, String time, int year, int month,
                       int day, String doctorPhone, String clientPhone, String doctorName, String clientName, String location, int status, String message){
        UserId = userId;
        DoctorId = doctorId;
        Time = time;
        Year = year;
        Month = month;
        Day = day;
        DoctorPhone = doctorPhone;
        ClientPhone = clientPhone;
        DoctorName = doctorName;
        ClientName = clientName;
        Location = location;
        Status = status;
        Message = message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setDay(int day) {
        Day = day;
    }

    public void setClientPhone(String clientPhone) {
        ClientPhone = clientPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        DoctorPhone = doctorPhone;
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

    public String getClientPhone() {
        return ClientPhone;
    }

    public String getDoctorPhone() {
        return DoctorPhone;
    }

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}
