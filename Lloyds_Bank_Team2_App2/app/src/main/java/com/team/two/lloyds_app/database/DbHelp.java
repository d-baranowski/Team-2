package com.team.two.lloyds_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.two.lloyds_app.database.SqlConst;

/**
* Created by danielbaranowski on 24/02/15.
*/
class DbHelp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lloyds";
    private static final int DATABASE_VERSION = 21;


    public DbHelp(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Executed when the database is created for the first time on the device
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create a table inside our database.
        db.execSQL(SqlConst.CREATE_CUSTOMER_TABLE);
        db.execSQL(SqlConst.CREATE_ACCOUNT_TABLE);
        db.execSQL(SqlConst.CREATE_TRANSACTIONS_TABLE);
        db.execSQL(SqlConst.CREATE_RECIPIENTS_TABLE);
        populate(db);
    }

    //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //SQL query to delete customer table if it exists
        final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + SqlConst.CUSTOMER_TABLE_NAME;
        final String DROP_ACCOUNTS_TABLE = "DROP TABLE IF EXISTS " + SqlConst.ACCOUNTS_TABLE_NAME;
        final String DROP_TRANSACTIONS_TABLE = "DROP TABLE IF EXISTS " + SqlConst.TRANSACTIONS_TABLE_NAME;
        final String DROP_RECIPMENTS_TABLE = "DROP TABLE IF EXISTS " + SqlConst.RECIPIENTS_TABLE_NAME;
        db.execSQL(DROP_CUSTOMER_TABLE);
        db.execSQL(DROP_ACCOUNTS_TABLE);
        db.execSQL(DROP_TRANSACTIONS_TABLE);
        db.execSQL(DROP_RECIPMENTS_TABLE);
        onCreate(db);
    }

    private static void populate(SQLiteDatabase db){
        //Create sample dummy data and instert into database
        ContentValues contentValCustomers = new ContentValues();
        contentValCustomers.put(SqlConst.CUSTOMER_NAME, "Daniel");
        contentValCustomers.put(SqlConst.CUSTOMER_SURNAME, "Baranowski");
        contentValCustomers.put(SqlConst.CUSTOMER_ADDRESSONE, "123 Street Name");
        contentValCustomers.put(SqlConst.CUSTOMER_ADDRESSTWO, "Newcastle Upon Tyne");
        contentValCustomers.put(SqlConst.CUSTOMER_POSTCODE, "NE9 9HA");
        contentValCustomers.put(SqlConst.CUSTOMER_USERID, 123456789);
        contentValCustomers.put(SqlConst.CUSTOMER_PASSWORD, "password");
        db.insert(SqlConst.CUSTOMER_TABLE_NAME,null,contentValCustomers);

        contentValCustomers = new ContentValues();
        contentValCustomers.put(SqlConst.CUSTOMER_NAME, "Matthew");
        contentValCustomers.put(SqlConst.CUSTOMER_SURNAME, "Selby");
        contentValCustomers.put(SqlConst.CUSTOMER_ADDRESSONE, "123 Street Name");
        contentValCustomers.put(SqlConst.CUSTOMER_ADDRESSTWO, "Newcastle Upon Tyne");
        contentValCustomers.put(SqlConst.CUSTOMER_POSTCODE, "NE7 4HA");
        contentValCustomers.put(SqlConst.CUSTOMER_USERID, 987654321);
        contentValCustomers.put(SqlConst.CUSTOMER_PASSWORD, "password");
        db.insert(SqlConst.CUSTOMER_TABLE_NAME,null,contentValCustomers);

        ContentValues contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlConst.ACCOUNT_NUMBER,1111111); //7 Digit account number
        contentValAccounts.put(SqlConst.ACCOUNT_SORTCODE,"30-11-11");
        contentValAccounts.put(SqlConst.ACCOUNT_BALANCE,992.49);
        contentValAccounts.put(SqlConst.ACCOUNT_AVAILABLEBALANCE,1492.49);
        contentValAccounts.put(SqlConst.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlConst.ACCOUNT_OVERDRAFTLIMIT,500.00);
        contentValAccounts.put(SqlConst.ACCOUNT_TYPE,"Student");
        db.insert(SqlConst.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlConst.ACCOUNT_NUMBER,2222222); //7 Digit account number
        contentValAccounts.put(SqlConst.ACCOUNT_SORTCODE,"30-11-11");
        contentValAccounts.put(SqlConst.ACCOUNT_BALANCE,0);
        contentValAccounts.put(SqlConst.ACCOUNT_AVAILABLEBALANCE,500);
        contentValAccounts.put(SqlConst.ACCOUNT_OWNERID,2);
        contentValAccounts.put(SqlConst.ACCOUNT_OVERDRAFTLIMIT,500.00);
        contentValAccounts.put(SqlConst.ACCOUNT_TYPE,"Student");
        db.insert(SqlConst.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlConst.ACCOUNT_NAME, "Bills");
        contentValAccounts.put(SqlConst.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlConst.ACCOUNT_TYPE,"Subaccount");
        contentValAccounts.put(SqlConst.ACCOUNT_AVAILABLEBALANCE,0.00);
        contentValAccounts.put(SqlConst.ACCOUNT_BALANCE,0.00);
        db.insert(SqlConst.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlConst.ACCOUNT_NAME, "Holidays");
        contentValAccounts.put(SqlConst.ACCOUNT_BALANCE,100.00);
        contentValAccounts.put(SqlConst.ACCOUNT_AVAILABLEBALANCE,100.00);
        contentValAccounts.put(SqlConst.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlConst.ACCOUNT_TYPE,"Subaccount");
        db.insert(SqlConst.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        ContentValues contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-06");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 51.83);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,992.49);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 15.14);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1044.32);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 10.99);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1059.46);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"BOOTS/0680 CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 9.99);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1070.45);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-03");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 2.82);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1080.44);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 7.84);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1083.26);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"NPOWER 00000000000000");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Direct Debit");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 111.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1091.10);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"LNK NCASTLE BARAS CD");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Cashpoint");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 10.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1202.10);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"UNIVERSITY OF NEWC CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 987.84);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,1212.10);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-01-26");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"VIRGIN MEDIA PYMTS CD 7835");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 29.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,2199.94);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlConst.TRANSACTION_DATE,"2015-01-20");
        contentValuesTransactions.put(SqlConst.TRANSACTION_DESCRIPTION,"MUCH LUV MUM");
        contentValuesTransactions.put(SqlConst.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlConst.TRANSACTION_IN, 2228.94);
        contentValuesTransactions.put(SqlConst.TRANSACTION_OUT, 0.00);
        contentValuesTransactions.put(SqlConst.TRANSACTION_BALANCE,2228.94);
        contentValuesTransactions.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);
    }
}
