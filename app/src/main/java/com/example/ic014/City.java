/*
Assignment #14
File Name: City.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import java.io.Serializable;

public class City implements Serializable {

    String place_address;
    String place_id;

    public City() {
    }

    public City(String place_address, String place_id) {
        this.place_address = place_address;
        this.place_id = place_id;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}

