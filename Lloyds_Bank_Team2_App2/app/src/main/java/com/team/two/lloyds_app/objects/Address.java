package com.team.two.lloyds_app.objects;

import java.util.Arrays;

/**
 * Author: Daniel Smith
 * Date: 12/04/2015
 * Purpose: to store address data for easy inputting in other classes
 */
public class Address{
	private String streetAddress;
	private String town;
	private String city;
	private String postcode;

	public Address(String streetAddress, String town, String city, String postcode){
		this.streetAddress = streetAddress;
		this.town = town;
		this.city = city;
		this.postcode = postcode;
	}

	public Address(String streetAddress, String city, String postcode)
	{
		this.streetAddress = streetAddress;
		this.city = city;
		this.postcode = postcode;
	}

	public String getStreetAddress(){
		return streetAddress;
	}

	public String getTown(){
		return town;
	}

	public String getCity(){
		return city;
	}

	public String getPostcode(){
		return postcode;
	}

	public String[] toStringArray()
	{
		String[] addressString = new String[4];
		addressString[0] = getStreetAddress();
		addressString[1] = getTown();
		if(getCity() == null)
		{
			addressString[2] = getPostcode();
			addressString = Arrays.copyOf(addressString, 3);
			return addressString;
		}else
		{
			addressString[2] = getCity();
			addressString[3] = getPostcode();
			return addressString;
		}
	}
}