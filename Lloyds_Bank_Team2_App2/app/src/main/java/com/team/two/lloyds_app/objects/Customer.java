package com.team.two.lloyds_app.objects;

/**
 * Author: Daniel Baranowski
 * Date: 06/02/2015
 * Purpose: To store data about a particular customer
 */
public class Customer {
    private final int id;
    private final String firstName;
    private final String surname;
	private	final Address address;
    private final String userId;

    public Customer(int id, String firstName, String surname, Address address, String userId){
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
		this.address = address;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public Address getAddress() {
        return address;
    }

    public String getUserId() {
        return userId;
    }
}
