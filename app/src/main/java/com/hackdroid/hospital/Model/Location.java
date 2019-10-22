package com.hackdroid.hospital.Model;

public class Location {
    String id , location ;

    public Location(String id, String location) {

        this.id = id;
        this.location = location;
    }

    public Location() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
