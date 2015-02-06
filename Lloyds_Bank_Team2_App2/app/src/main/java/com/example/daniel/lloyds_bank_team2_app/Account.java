package com.example.daniel.lloyds_bank_team2_app;

/**
 * Created by danielbaranowski on 06/02/15.
 */
public class Account {
    private int accountNumber;
    private String sortCode;
    private String accountName;
    private String accountType;
    private int accountBalance;
    private int availableBalance;
    private int overdraft;
    private int ownerId;

    public Account(int accountNumber, String sortCode, String accountName, String accountType, int accountBalance, int availableBalance, int overdraft, int ownerId) {
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.availableBalance = availableBalance;
        this.overdraft = overdraft;
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getOverdraft() {
        return overdraft;
    }

    public int getAvailableBalance() {
        return availableBalance;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getSortCode() {
        return sortCode;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}
