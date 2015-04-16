package com.team.two.lloyds_app.objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Daniel Baranowski
 * Date: 06/02/2015
 * Purpose: To store data about a particular account
 */
public class Account{
	private final int accountID;
	private int accountNumber;
	private String sortCode;
	private final String accountName;
	private final String accountType;
	private double accountBalance;
	private double availableBalance;
	private double overdraft;
	private final int ownerId;
	private ArrayList<HashMap<String, String>> transactions;

	//Constructors. One takes all the information about an account and the other simply takes the bare minimum.
	public Account(int id, int accountNumber, String sortCode, String accountName, String accountType, double accountBalance, double availableBalance, double overdraft, int ownerId, ArrayList<HashMap<String, String>> transactions){
		this.accountID = id;
		this.accountNumber = accountNumber;
		this.sortCode = sortCode;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
		this.availableBalance = availableBalance;
		this.overdraft = overdraft;
		this.ownerId = ownerId;
		this.transactions = transactions;
	}

	public Account(int id, String accountName, String accountType, double accountBalance, double availableBalance, int ownerId, ArrayList<HashMap<String, String>> transactions){
		this.accountID = id;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
		this.availableBalance = availableBalance;
		this.ownerId = ownerId;
		this.transactions = transactions;
	}

	//getters for all fields.
	public int getOwnerId(){
		return ownerId;
	}

	public double getOverdraft(){
		return overdraft;
	}

	public double getAvailableBalance(){
		return availableBalance;
	}

	public double getAccountBalance(){
		return accountBalance;
	}

	public String getAccountType(){
		return accountType;
	}

	public String getAccountName(){
		return accountName;
	}

	public String getSortCode(){
		return sortCode;
	}

	public int getAccountNumber(){
		return accountNumber;
	}

	public ArrayList<HashMap<String, String>> getTransactions(){
		return transactions;
	}

	public int getAccountID(){
		return accountID;
	}
}
