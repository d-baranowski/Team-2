package com.team.two.lloyds_app.objects;

/**
 * Created by Michael on 07/04/2015.
 */
public class Branch {
    String name;
    double longitude;
    double latitude;

    public Branch(String name, double latitude, double longitude){
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
