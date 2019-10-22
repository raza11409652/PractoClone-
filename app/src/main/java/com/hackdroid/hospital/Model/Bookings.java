package com.hackdroid.hospital.Model;

public class Bookings {
    String id ,time , date , doctor , address ;

    public Bookings() {
    }

    public Bookings(String id, String time, String date, String doctor, String address) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.doctor = doctor;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
