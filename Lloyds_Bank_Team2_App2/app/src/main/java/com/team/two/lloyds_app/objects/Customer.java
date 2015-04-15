package com.team.two.lloyds_app.objects;

/**
 * Created by danielbaranowski on 06/02/15.
 */
public class Customer {
    private final int id;
    private final String firstName;
    private final String surname;
    private String addressOne;
    private String addressTwo;
    private String postCode;
    private int offerPoints;
    private final String userId;

    public Customer(int id, String firstName, String surname, String addressOne, String addressTwo, String postCode, String userId, int offerPoints){
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.addressOne = addressOne;
        this.addressTwo = addressTwo;
        this.postCode = postCode;
        this.userId = userId;
        this.offerPoints = offerPoints;
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

    public int getOfferPoints() { return offerPoints; }
}
