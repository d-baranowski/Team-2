package com.team.two.lloyds_app.objects;

/**
 * Created by Michael on 07/04/2015.
 */

public class Branch {
    private String name;
    private double latitude;
    private double longitude;
    private String[] address;
    private String phoneNumber;
    private String[] openingTimes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String streetAddress, String city, String county, String postcode) {
        this.address = new String[]{streetAddress, city, county, postcode};
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.openingTimes = new String[]{monday, tuesday, wednesday, thursday, friday, saturday, sunday};
    }
}
