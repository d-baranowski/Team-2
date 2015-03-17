package com.team.two.lloyds_app.objects;

/**
 * Created by danielbaranowski on 06/02/15.
 */
public class Customer {
    private static int id;
    private static String firstName;
    private static String surname;
    private static String addressOne;
    private static String addressTwo;
    private static String postCode;
    private static String userId;

    public Customer(int id, String firstName, String surname, String addressOne, String addressTwo, String postCode, String userId){
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.addressOne = addressOne;
        this.addressTwo = addressTwo;
        this.postCode = postCode;
        this.userId = userId;
    }

    public static int getId() {
        return id;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getSurname() {
        return surname;
    }

    public static String getAddressOne() {
        return addressOne;
    }

    public static String getAddressTwo() {
        return addressTwo;
    }

    public static String getPostCode() {
        return postCode;
    }

    public static String getUserId() {
        return userId;
    }
}
