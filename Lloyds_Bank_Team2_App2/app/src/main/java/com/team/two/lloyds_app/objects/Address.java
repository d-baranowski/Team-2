package com.team.two.lloyds_app.objects;

/**
 * Author: Daniel Smith
 * Date: 12/04/2015
 * Purpose: to store address data for easy inputting in other classes
 */
public class Address{
	private String streetNumber;
	private String street;
	private String town;
	private String city;
	private String postcode;

	public Address(String streetNumber, String street, String town, String city, String postcode){
		this.streetNumber = streetNumber;
		this.street = street;
		this.town = town;
		this.city = city;
		this.postcode = postcode;
	}

	public String getStreetNumber(){
		return streetNumber;
	}

	public String getStreet(){
		return street;
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
}