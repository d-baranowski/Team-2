package com.team.two.lloyds_app.objects;

/**
 * Created by danielbaranowski on 25/02/15.
 */
public class Recipient {
    private final int id;
    private final String name;
    private final String sortCode;
    private final int accountNumber;
    private final int ownerId;

    public Recipient(int id, String name, String sortCode, int accountNumber, int ownerId) {
        this.id = id;
        this.name = name;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSortCode() {
        return sortCode;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
