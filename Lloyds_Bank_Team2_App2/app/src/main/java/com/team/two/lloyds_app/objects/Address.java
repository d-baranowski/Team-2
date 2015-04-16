package com.team.two.lloyds_app.objects;

import java.util.Arrays;

/**
 * Author: Daniel Smith
 * Date: 12/04/2015
 * Purpose: to store address data for easy inputting in other classes
 */

public class Address
{
	//private fields for the object
	private String streetAddress;
	private String town;
	private String city;
	private String postcode;

	//Two constructors for the address object. The first one is for if a town name is given/needed...
	public Address(String streetAddress, String town, String city, String postcode)
	{
		this.streetAddress = streetAddress;
		this.town = town;
		this.city = city;
		this.postcode = postcode;
	}
	//and the second for if a town name is not needed
	public Address(String streetAddress, String city, String postcode)
	{
		this.streetAddress = streetAddress;
		this.city = city;
		this.postcode = postcode;
	}

	/*getters, no setters are needed as the address should not be changed. Or if it is, it shouldn't
	be changed using the app*/
	public String getStreetAddress()
	{
		return streetAddress;
	}

	public String getTown()
	{
		return town;
	}

	public String getCity()
	{
		return city;
	}

	public String getPostcode()
	{
		return postcode;
	}

	//method to convert the object to an array for other classes to use.
	public String[] toStringArray()
	{
		/*an array of length 4 (the max length needed) is created and the first
		attribute assigned*/
		String[] addressString = new String[4];
		addressString[0] = getStreetAddress();
		/*If a town isn't given then the city and postcode are filled and a copy is created with
		1 less length*/
		if(getTown() == null)
		{
			addressString[1] = getCity();
			addressString[2] = getPostcode();
			addressString = Arrays.copyOf(addressString, 3);
			return addressString;
		}else
		{
			//otherwise all the attributes are filled.
			addressString[1] = getTown();
			addressString[2] = getCity();
			addressString[3] = getPostcode();
			return addressString;
		}
	}
}