package com.montreal.wtm.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Location {

    @PropertyName("name")
    protected String name;

    @PropertyName("address")
    protected String address;

    @PropertyName("metro")
    protected String metro;

    @PropertyName("parking")
    protected String parking;

    @PropertyName("parking_url")
    protected String imageParkingUrl;

    public Location() {
    }

    public Location(String name, String address, String metro, String parking, String imageParkingUrl) {
        this.name = name;
        this.address = address;
        this.metro = metro;
        this.parking = parking;
        this.imageParkingUrl = imageParkingUrl;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getParking() {
        return parking;
    }

    public String getImageParkingUrl() {
        return imageParkingUrl;
    }

    public String getMetro() {
        return metro;
    }
}
