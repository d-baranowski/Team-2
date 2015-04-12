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
    private String addressOne;
    private String addressTwo;
    private String postCode;
    private final String userId;

    public Customer(int id, String firstName, String surname, String addressOne, String addressTwo, String postCode, String userId){
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.addressOne = addressOne;
        this.addressTwo = addressTwo;
        this.postCode = postCode;
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

    public String getAddressOne() {
        return addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getUserId() {
        return userId;
    }
}
