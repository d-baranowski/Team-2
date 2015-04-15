package com.team.two.lloyds_app.database;

/**
 * Created by danielbaranowski on 24/02/15.
 */
public class SqlCons {
    //Customer Table SQL
    static final String CUSTOMER_TABLE_NAME = "Customers";
    static final String CUSTOMER_ID = "_id";
    static final String CUSTOMER_NAME = "FirstNAme";
    static final String CUSTOMER_SURNAME = "Surname";
    static final String CUSTOMER_ADDRESSONE = "AddressLine1";
    static final String CUSTOMER_ADDRESSTWO = "AddressLine2";
    static final String CUSTOMER_POSTCODE = "Postcode";
    static final String CUSTOMER_USERID = "UserID";
    static final String CUSTOMER_PASSWORD = "Password";
    static final String CUSTOMER_OFFERS_POINTS = "Points";

    static final String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE "+
                    CUSTOMER_TABLE_NAME + " (" +
                    CUSTOMER_ID + "	INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CUSTOMER_NAME + "   STRING NOT NULL," +
                    CUSTOMER_SURNAME + "	STRING NOT NULL," +
                    CUSTOMER_ADDRESSONE + "	STRING NOT NULL," +
                    CUSTOMER_ADDRESSTWO + "	STRING NOT NULL," +
                    CUSTOMER_POSTCODE + "	STRING NOT NULL," +
                    CUSTOMER_USERID + "	INTEGER NOT NULL UNIQUE ," +
                    CUSTOMER_PASSWORD + "	STRING NOT NULL," +
                    CUSTOMER_OFFERS_POINTS +"  INTEGER NOT NULL DEFAULT 0);";

    static final String[] CUSTOMER_COLUMNS = {CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_SURNAME, CUSTOMER_ADDRESSONE, CUSTOMER_ADDRESSTWO, CUSTOMER_POSTCODE, CUSTOMER_USERID, CUSTOMER_PASSWORD,CUSTOMER_OFFERS_POINTS};

    //Accounts table SQL
    static final String ACCOUNTS_TABLE_NAME = "Accounts";
    static final String ACCOUNT_ID = "account_id";
    static final String ACCOUNT_NUMBER = "AccountNumber";
    static final String ACCOUNT_SORTCODE = "SortCode";
    static final String ACCOUNT_NAME = "Name";
    static final String ACCOUNT_TYPE = "Type";
    static final String ACCOUNT_BALANCE = "Balance";
    static final String ACCOUNT_AVAILABLEBALANCE = "Available";
    static final String ACCOUNT_OVERDRAFTLIMIT = "Overdraft";
    static final String ACCOUNT_OWNERID = "Owner";

    static final String CREATE_ACCOUNT_TABLE =
            "CREATE TABLE " +
                    ACCOUNTS_TABLE_NAME + " (" +
                    ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACCOUNT_NUMBER + " INTEGER ," +
                    ACCOUNT_SORTCODE + " STRING," +
                    ACCOUNT_NAME + " STRING DEFAULT 'Account'," +
                    ACCOUNT_TYPE + " STRING," +
                    ACCOUNT_BALANCE + "	REAL DEFAULT 0.00," +
                    ACCOUNT_AVAILABLEBALANCE + " REAL DEFAULT 0.00," +
                    ACCOUNT_OVERDRAFTLIMIT + " REAL DEFAULT 0.00," +
                    ACCOUNT_OWNERID + "	INTEGER REFERENCES "+CUSTOMER_TABLE_NAME+"("+ CUSTOMER_ID +"));";

    static final String[] ACCOUNT_COLUMNS = {ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_SORTCODE, ACCOUNT_NAME, ACCOUNT_TYPE, ACCOUNT_BALANCE, ACCOUNT_AVAILABLEBALANCE, ACCOUNT_OVERDRAFTLIMIT, ACCOUNT_OWNERID};

    //Transactions table SQL
    static final String TRANSACTIONS_TABLE_NAME = "Transactions";
    private static final String TRANSACTION_ID = "transaction_id";
    static final String TRANSACTION_DATE = "Date";
    static final String TRANSACTION_TIME = "Time";
    static final String TRANSACTION_DESCRIPTION = "Description";
    static final String TRANSACTION_TYPE = "TransactionType";
    static final String TRANSACTION_IN = "Income";
    static final String TRANSACTION_OUT = "Outcome";
    static final String TRANSACTION_BALANCE = "TransactionBalance";
    static final String TRANSACTION_ACCOUNT_ID_FOREIGN = "AccountReference";


    static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " +
                    TRANSACTIONS_TABLE_NAME + " (" +
                    TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TRANSACTION_DATE + " STRING NOT NULL, " +
                    TRANSACTION_TIME + " INTEGER NOT NULL, " +
                    TRANSACTION_DESCRIPTION + " STRING," +
                    TRANSACTION_TYPE + " STRING," +
                    TRANSACTION_IN + " REAL DEFAULT 0.00," +
                    TRANSACTION_OUT + " REAL DEFAULT 0.00," +
                    TRANSACTION_BALANCE + " REAL," +
                    TRANSACTION_ACCOUNT_ID_FOREIGN + " INTEGER REFERENCES "+ACCOUNTS_TABLE_NAME+"("+ ACCOUNT_ID +"));";

    static final String[] TRANSACTION_COLUMNS = {TRANSACTION_ID, TRANSACTION_DATE, TRANSACTION_TIME, TRANSACTION_DESCRIPTION, TRANSACTION_TYPE, TRANSACTION_IN, TRANSACTION_OUT, TRANSACTION_BALANCE, TRANSACTION_ACCOUNT_ID_FOREIGN};

    //Recipients table SQL
    static final String RECIPIENTS_TABLE_NAME = "Recipients";
    static final String RECIPIENT_ID = "_id";
    static final String RECIPIENT_NAME = "Name";
    static final String RECIPIENT_SORTCODE = "sortcode";
    static final String RECIPIENT_ACCOUNTNUMBER = "accountnumber";
    static final String RECIPIENT_OWNER_ID = "owner_id";

    static final String CREATE_RECIPIENTS_TABLE =
            "CREATE TABLE " +
                    RECIPIENTS_TABLE_NAME + " (" +
                    RECIPIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RECIPIENT_NAME + " STRING," +
                    RECIPIENT_SORTCODE + " STRING," +
                    RECIPIENT_ACCOUNTNUMBER + " INTEGER," +
                    RECIPIENT_OWNER_ID + " INTEGER REFERENCES " + CUSTOMER_TABLE_NAME + "("+ CUSTOMER_ID  +"));";

    static final String[] RECIPIENT_COLUMNS = {RECIPIENT_ID,RECIPIENT_NAME,RECIPIENT_SORTCODE,RECIPIENT_ACCOUNTNUMBER,RECIPIENT_OWNER_ID};


    //Available Offers and Deals
    static final String AVAIL_OFFERS_TABLE_NAME = "AvailableOffers";
    static final String AVAIL_OFFER_ID = "_id";
    static final String AVAIL_OFFER_ICON = "Icon";
    static final String AVAIL_OFFER_NAME = "Name";
    static final String AVAIL_OFFER_DESCRIPTION = "Description";
    static final String AVAIL_OFFER_PRICE = "Price";
    static final String AVAIL_OFFER_ACTIVE = "Active";

    static final String CREATE_AVAILABLE_OFFERS_TABLE =
            "CREATE TABLE " +
                    AVAIL_OFFERS_TABLE_NAME + " (" +
                    AVAIL_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AVAIL_OFFER_ICON + " INTEGER," +
                    AVAIL_OFFER_NAME + " STRING," +
                    AVAIL_OFFER_DESCRIPTION + " STRING," +
                    AVAIL_OFFER_ACTIVE + " INTEGER DEFAULT 0," +
                    AVAIL_OFFER_PRICE + " INTEGER);";

    static final String[] AVAILABLE_OFFERS_COLUMNS = {AVAIL_OFFER_ID, AVAIL_OFFER_ICON, AVAIL_OFFER_NAME, AVAIL_OFFER_DESCRIPTION, AVAIL_OFFER_PRICE,AVAIL_OFFER_ACTIVE};

    static final String ACTIVE_OFFERS_TABLE_NAME = "ActiveOffers";
    static final String ACTIVE_OFFER_ID = "_id";
    static final String ACTIVE_OFFER_ICON = "Icon";
    static final String ACTIVE_OFFER_BARCODE = "Barcode";
    static final String ACTIVE_OFFER_NAME = "Name";
    static final String ACTIVE_OFFER_DESCRIPTION = "Description";
    static final String ACTIVE_OFFER_OWNER_ID = "OwnerId";

    static final String CREATE_ACTIVE_OFFERS_TABLE =
            "CREATE TABLE " +
                    ACTIVE_OFFERS_TABLE_NAME + " (" +
                    ACTIVE_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ACTIVE_OFFER_ICON + " INTEGER," +
                    ACTIVE_OFFER_BARCODE + " INTEGER," +
                    ACTIVE_OFFER_NAME + " STRING," +
                    ACTIVE_OFFER_DESCRIPTION + " STRING," +
                    ACTIVE_OFFER_OWNER_ID + " INTEGER REFERENCES " + CUSTOMER_TABLE_NAME + "("+ CUSTOMER_ID  +"));";

    static final String[] ACTIVE_OFFERS_COLUMN_NAMES = {ACTIVE_OFFER_ID,ACTIVE_OFFER_ICON,ACTIVE_OFFER_BARCODE,ACTIVE_OFFER_NAME,ACTIVE_OFFER_DESCRIPTION,ACTIVE_OFFER_OWNER_ID};
}
