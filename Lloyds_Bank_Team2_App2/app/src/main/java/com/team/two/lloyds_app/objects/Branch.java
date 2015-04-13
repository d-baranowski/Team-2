package com.team.two.lloyds_app.objects;

/**
 * Author: Michael Edwards
 * Date: 07/04/2015
 * Purpose:
 * Modified By Daniel Smith on 12/04/2015
 */

public class Branch {
    private String name;
    private double latitude;
    private double longitude;
    private Address address;
    private String phoneNumber;
    private String[] openingTimes;

	public Branch(String name, double latitude, double longitude, Address address, String phoneNumber, String[] openingTimes)
	{
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.openingTimes = openingTimes;
	}

    public String getName() {
        return name;
    }

   /*	public void setName(String name) {
        this.name = name;
    }*/

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String[] getOpeningTimes() {
        return openingTimes;
    }
}
