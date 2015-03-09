package com.example.daniel.lloyds_bank_team2_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Created by danielbaranowski on 24/02/15.
*/
class DbHelp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lloyds";
    private static final int DATABASE_VERSION = 16;


    public DbHelp(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Executed when the database is created for the first time on the device
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create a table inside our database.
        db.execSQL(SqlValues.CREATE_CUSTOMER_TABLE);
        db.execSQL(SqlValues.CREATE_ACCOUNTS_TABLE);
        db.execSQL(SqlValues.CREATE_TRANSACTIONS_TABLE);

        //Create sample dummy data and instert into database
        ContentValues contentValCustomers = new ContentValues();
        contentValCustomers.put(SqlValues.FIELD_NAME, "Daniel");
        contentValCustomers.put(SqlValues.FIELD_SURNAME, "Baranowski");
        contentValCustomers.put(SqlValues.FIELD_ADDRESSONE, "123 Street Name");
        contentValCustomers.put(SqlValues.FIELD_ADDRESSTWO, "Newcastle Upon Tyne");
        contentValCustomers.put(SqlValues.FIELD_POSTCODE, "NE9 9HA");
        contentValCustomers.put(SqlValues.FIELD_USERID, 123456789);
        contentValCustomers.put(SqlValues.FIELD_PASSWORD, "password");
        db.insert(SqlValues.CUSTOMER_TABLE_NAME,null,contentValCustomers);

        ContentValues contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlValues.FIELD_ACCOUNTNO,1111111); //7 Digit account number
        contentValAccounts.put(SqlValues.FIELD_SORTCODE,"30-11-11");
        contentValAccounts.put(SqlValues.FIELD_BALANCE,992.49);
        contentValAccounts.put(SqlValues.FIELD_AVAILABLE_BALANCE,1492.49);
        contentValAccounts.put(SqlValues.FIELD_OWNERID,1);
        contentValAccounts.put(SqlValues.FIELD_OVERDRAFT _LIMIT,500.00);
        contentValAccounts.put(SqlValues.FIELD_TYPE,"Student");
        db.insert(SqlValues.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlValues.FIELD_ACCOUNTNAME, "Bills");
        contentValAccounts.put(SqlValues.FIELD_OWNERID,1);
        contentValAccounts.put(SqlValues.FIELD_TYPE,"Subaccount");
        contentValAccounts.put(SqlValues.FIELD_AVAILABLE_BALANCE,0.00);
        contentValAccounts.put(SqlValues.FIELD_BALANCE,0.00);
        db.insert(SqlValues.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlValues.FIELD_ACCOUNTNAME, "Holidays");
        contentValAccounts.put(SqlValues.FIELD_BALANCE,100.00);
        contentValAccounts.put(SqlValues.FIELD_AVAILABLE_BALANCE,100.00);
        contentValAccounts.put(SqlValues.FIELD_OWNERID,1);
        contentValAccounts.put(SqlValues.FIELD_TYPE,"Subaccount");
        db.insert(SqlValues.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        ContentValues contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-06");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 51.83);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,992.49);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 15.14);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1044.32);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 10.99);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1059.46);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"BOOTS/0680 CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 9.99);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1070.45);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-03");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 2.82);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1080.44);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 7.84);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1083.26);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"NPOWER 00000000000000");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Direct Debit");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 111.00);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1091.10);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"LNK NCASTLE BARAS CD");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Cashpoint");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 10.00);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1202.10);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"UNIVERSITY OF NEWC CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 987.84);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,1212.10);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-01-26");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"VIRGIN MEDIA PYMTS CD 7835");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 29.00);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,2199.94);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlValues.FIELD_DATE,"2015-01-20");
        contentValuesTransactions.put(SqlValues.FIELD_DESCRIPTION,"MUCH LUV MUM");
        contentValuesTransactions.put(SqlValues.FIELD_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlValues.FIELD_IN, 2228.94);
        contentValuesTransactions.put(SqlValues.FIELD_OUT, 0.00);
        contentValuesTransactions.put(SqlValues.FIELD_BALANCE,2228.94);
        contentValuesTransactions.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE,1);
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);
    }

    //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //SQL query to delete customer table if it exists
        final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + SqlValues.CUSTOMER_TABLE_NAME;
        final String DROP_ACCOUNTS_TABLE = "DROP TABLE IF EXISTS " + SqlValues.ACCOUNTS_TABLE_NAME;
        final String DROP_TRANSACTIONS_TABLE = "DROP TABLE IF EXISTS " + SqlValues.TRANSACTIONS_TABLE_NAME;
        db.execSQL(DROP_CUSTOMER_TABLE);
        db.execSQL(DROP_ACCOUNTS_TABLE);
        db.execSQL(DROP_TRANSACTIONS_TABLE);
        onCreate(db);
    }
}
