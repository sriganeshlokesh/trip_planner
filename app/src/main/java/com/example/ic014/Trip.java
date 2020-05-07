/*
Assignment #14
File Name: Trip.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    String place_address;
    Double trip_latitude;
    Double trip_longitude;
    String place_id;
    String trip_name;
    ArrayList<Place> placeArrayList;

    public Trip() {
    }

    public Trip(String place_address, Double trip_latitude, Double trip_longitude, String place_id, String trip_name, ArrayList<Place> placeArrayList) {
        this.place_address = place_address;
        this.trip_latitude = trip_latitude;
        this.trip_longitude = trip_longitude;
        this.place_id = place_id;
        this.trip_name = trip_name;
        this.placeArrayList = placeArrayList;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public Double getTrip_latitude() {
        return trip_latitude;
    }

    public void setTrip_latitude(Double trip_latitude) {
        this.trip_latitude = trip_latitude;
    }

    public Double getTrip_longitude() {
        return trip_longitude;
    }

    public void setTrip_longitude(Double trip_longitude) {
        this.trip_longitude = trip_longitude;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public ArrayList<Place> getPlaceArrayList() {
        return placeArrayList;
    }

    public void setPlaceArrayList(ArrayList<Place> placeArrayList) {
        this.placeArrayList = placeArrayList;
    }
}
