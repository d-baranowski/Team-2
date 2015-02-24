package com.example.daniel.lloyds_bank_team2_app;

/**
 * Created by danielbaranowski on 24/02/15.
 */
public class SqlValues {
    //Customer Table SQL
    static final String CUSTOMER_TABLE_NAME = "Customers";
    static final String FIELD_CUSTOMER_ID = "_id";
    static final String FIELD_NAME = "FirstNAme";
    static final String FIELD_SURNAME = "Surname";
    static final String FIELD_ADDRESSONE = "AddressLine1";
    static final String FIELD_ADDRESSTWO = "AddressLine2";
    static final String FIELD_POSTCODE = "Postcode";
    static final String FIELD_USERID = "UserID";
    static final String FIELD_PASSWORD = "Password";
    static final String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE "+
                    CUSTOMER_TABLE_NAME + " (" +
                    FIELD_CUSTOMER_ID + "	INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FIELD_NAME + "   TEXT NOT NULL," +
                    FIELD_SURNAME + "	TEXT NOT NULL," +
                    FIELD_ADDRESSONE + "	TEXT NOT NULL," +
                    FIELD_ADDRESSTWO + "	TEXT NOT NULL," +
                    FIELD_POSTCODE + "	TEXT NOT NULL," +
                    FIELD_USERID + "	INTEGER NOT NULL UNIQUE ," +
                    FIELD_PASSWORD + "	TEXT NOT NULL);";
    static final String[] CUSTOMER_COLLUMNS = {FIELD_CUSTOMER_ID,FIELD_NAME,FIELD_SURNAME,FIELD_ADDRESSONE,FIELD_ADDRESSTWO,FIELD_POSTCODE,FIELD_USERID,FIELD_PASSWORD};

    //Accounts table SQL
    static final String ACCOUNTS_TABLE_NAME = "Accounts";
    static final String FIELD_ACCOUNT_ID = "account_id";
    static final String FIELD_ACCOUNTNO = "AccountNumber";
    static final String FIELD_SORTCODE = "SortCode";
    static final String FIELD_ACCOUNTNAME = "Name";
    static final String FIELD_ACCOUNTTYPE = "Type";
    static final String FIELD_BALANCE = "Balance";
    static final String FIELD_AVAILABLE_BALANCE = "Available";
    static final String FIELD_OVERDRAFT_LIMIT = "Overdraft";
    static final String FIELD_OWNERID = "Owner";
    static final String CREATE_ACCOUNTS_TABLE =
            "CREATE TABLE " +
                    ACCOUNTS_TABLE_NAME + " (" +
                    FIELD_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_ACCOUNTNO + " INTEGER ," +
                    FIELD_SORTCODE + " STRING," +
                    FIELD_ACCOUNTNAME + " STRING DEFAULT 'Account'," +
                    FIELD_ACCOUNTTYPE + " STRING," +
                    FIELD_BALANCE + "	REAL DEFAULT 0.00," +
                    FIELD_AVAILABLE_BALANCE + " REAL DEFAULT 0.00," +
                    FIELD_OVERDRAFT_LIMIT + " REAL DEFAULT 0.00," +
                    FIELD_OWNERID + "	INTEGER REFERENCES "+CUSTOMER_TABLE_NAME+"("+FIELD_CUSTOMER_ID+"));";
    static final String[] ACCOUNTS_COLUMNS = {FIELD_ACCOUNT_ID,FIELD_ACCOUNTNO,FIELD_SORTCODE,FIELD_ACCOUNTNAME,FIELD_ACCOUNTTYPE,FIELD_BALANCE,FIELD_AVAILABLE_BALANCE,FIELD_OVERDRAFT_LIMIT,FIELD_OWNERID};

    //Transactions table SQL
    static final String TRANSACTIONS_TABLE_NAME = "Transactions";
    private static final String FIELD_TRANSACTION_ID = "transaction_id";
    static final String FIELD_DATE = "Date";
    static final String FIELD_DESCRIPTION = "Description";
    static final String FIELD_TYPE = "Type";
    static final String FIELD_IN = "Income";
    static final String FIELD_OUT = "Outcome";
    private static final String FIELD_TRANSACTION_BALANCE = "Balance";
    static final String FIELD_ACCOUNT_ID_REFERENCE = "Account";
    static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " +
                    TRANSACTIONS_TABLE_NAME + " (" +
                    FIELD_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FIELD_DATE + " STRING NOT NULL, " +
                    FIELD_DESCRIPTION + " STRING," +
                    FIELD_TYPE + " STRING," +
                    FIELD_IN + " REAL DEFAULT 0.00," +
                    FIELD_OUT + " REAL DEFAULT 0.00," +
                    FIELD_TRANSACTION_BALANCE + " REAL," +
                    FIELD_ACCOUNT_ID_REFERENCE + " INTEGER REFERENCES "+ACCOUNTS_TABLE_NAME+"("+FIELD_ACCOUNT_ID+"));";
    static final String[] TRANSACTIONS_COLUMNS = {FIELD_TRANSACTION_ID,FIELD_DATE,FIELD_DESCRIPTION,FIELD_TYPE,FIELD_IN,FIELD_OUT,FIELD_TRANSACTION_BALANCE, FIELD_ACCOUNT_ID_REFERENCE};
}
