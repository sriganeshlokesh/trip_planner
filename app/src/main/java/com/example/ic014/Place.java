/*
Assignment #14
File Name: Place.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import java.io.Serializable;

public class Place implements Serializable {

    String place_id;
    String place_name;
    double place_lat;
    double place_lng;
    String place_icon;

    public Place() {
    }

    public Place(String place_id, String place_name, double place_lat, double place_lng, String place_icon) {
        this.place_id = place_id;
        this.place_name = place_name;
        this.place_lat = place_lat;
        this.place_lng = place_lng;
        this.place_icon = place_icon;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public double getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(double place_lat) {
        this.place_lat = place_lat;
    }

    public double getPlace_lng() {
        return place_lng;
    }

    public void setPlace_lng(double place_lng) {
        this.place_lng = place_lng;
    }

    public String getPlace_icon() {
        return place_icon;
    }

    public void setPlace_icon(String place_icon) {
        this.place_icon = place_icon;
    }
}

