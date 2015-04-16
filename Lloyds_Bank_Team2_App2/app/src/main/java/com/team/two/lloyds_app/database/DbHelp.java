package com.team.two.lloyds_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.two.lloyds_app.R;

/**
 * Author: Daniel Baranowski
 * Date: 24/02/2015
 * Purpose: To store dummy customer data
 */
public class DbHelp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lloyds";
    private static final int DATABASE_VERSION = 51;
    private Context context;

    public DbHelp(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Executed when the database is created for the first time on the device
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create a table inside our database.
        db.execSQL(SqlCons.CREATE_CUSTOMER_TABLE);
        db.execSQL(SqlCons.CREATE_ACCOUNT_TABLE);
        db.execSQL(SqlCons.CREATE_TRANSACTIONS_TABLE);
        db.execSQL(SqlCons.CREATE_RECIPIENTS_TABLE);
        db.execSQL(SqlCons.CREATE_ACHIEVEMENTS_TABLE);
        db.execSQL(SqlCons.CREATE_CUSTOMER_ACHIEVEMENTS_TABLE);
        db.execSQL(SqlCons.CREATE_AVAILABLE_OFFERS_TABLE);
        db.execSQL(SqlCons.CREATE_ACTIVE_OFFERS_TABLE);
        populate(db);
    }

    //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //SQL query to delete customer table if it exists
        final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + SqlCons.CUSTOMER_TABLE_NAME;
        final String DROP_ACCOUNTS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.ACCOUNTS_TABLE_NAME;
        final String DROP_TRANSACTIONS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.TRANSACTIONS_TABLE_NAME;
        final String DROP_RECIPIENTS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.RECIPIENTS_TABLE_NAME;
        final String DROP_AVAILABLE_OFFERS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.AVAIL_OFFERS_TABLE_NAME;
        final String DROP_ACTIVE_OFFERS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.ACTIVE_OFFERS_TABLE_NAME;
        final String DROP_ACHIEVEMENTS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.ACHIEVEMENTS_TABLE_NAME;
        final String DROP_CUSTOMER_ACHIEVEMENTS_TABLE = "DROP TABLE IF EXISTS " + SqlCons.CUSTOMER_ACHIEVEMENTS_TABLE_NAME;

        db.execSQL(DROP_CUSTOMER_TABLE);
        db.execSQL(DROP_ACCOUNTS_TABLE);
        db.execSQL(DROP_TRANSACTIONS_TABLE);
        db.execSQL(DROP_RECIPIENTS_TABLE);
        db.execSQL(DROP_AVAILABLE_OFFERS_TABLE);
        db.execSQL(DROP_ACTIVE_OFFERS_TABLE);
        db.execSQL(DROP_RECIPIENTS_TABLE);
        db.execSQL(DROP_ACHIEVEMENTS_TABLE);
        db.execSQL(DROP_CUSTOMER_ACHIEVEMENTS_TABLE);
        onCreate(db);
    }

    private static void populate(SQLiteDatabase db){
        //Create sample dummy data and insert into database
        ContentValues contentValCustomers = new ContentValues();
        contentValCustomers.put(SqlCons.CUSTOMER_NAME, "Daniel");
        contentValCustomers.put(SqlCons.CUSTOMER_SURNAME, "Baranowski");
        contentValCustomers.put(SqlCons.CUSTOMER_STREET_ADDRESS, "123 Street Name");
        contentValCustomers.put(SqlCons.CUSTOMER_CITY, "Newcastle Upon Tyne");
        contentValCustomers.put(SqlCons.CUSTOMER_POSTCODE, "NE9 9HA");
        contentValCustomers.put(SqlCons.CUSTOMER_USERID, 123456789);
        contentValCustomers.put(SqlCons.CUSTOMER_PASSWORD, "password");
        contentValCustomers.put(SqlCons.CUSTOMER_OFFERS_POINTS, 1000);
        db.insert(SqlCons.CUSTOMER_TABLE_NAME,null,contentValCustomers);

        contentValCustomers = new ContentValues();
        contentValCustomers.put(SqlCons.CUSTOMER_NAME, "Matthew");
        contentValCustomers.put(SqlCons.CUSTOMER_SURNAME, "Selby");
        contentValCustomers.put(SqlCons.CUSTOMER_STREET_ADDRESS, "123 Street Name");
        contentValCustomers.put(SqlCons.CUSTOMER_CITY, "Newcastle Upon Tyne");
        contentValCustomers.put(SqlCons.CUSTOMER_POSTCODE, "NE7 4HA");
        contentValCustomers.put(SqlCons.CUSTOMER_USERID, 987654321);
        contentValCustomers.put(SqlCons.CUSTOMER_PASSWORD, "password");
        db.insert(SqlCons.CUSTOMER_TABLE_NAME,null,contentValCustomers);

        ContentValues contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlCons.ACCOUNT_NUMBER,1111111); //7 Digit account number
        contentValAccounts.put(SqlCons.ACCOUNT_SORTCODE,"30-11-11");
        contentValAccounts.put(SqlCons.ACCOUNT_BALANCE,992.49);
        contentValAccounts.put(SqlCons.ACCOUNT_AVAILABLEBALANCE,1492.49);
        contentValAccounts.put(SqlCons.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlCons.ACCOUNT_OVERDRAFTLIMIT,500.00);
        contentValAccounts.put(SqlCons.ACCOUNT_TYPE,"Student");
        db.insert(SqlCons.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlCons.ACCOUNT_NUMBER,2222222); //7 Digit account number
        contentValAccounts.put(SqlCons.ACCOUNT_SORTCODE,"30-11-11");
        contentValAccounts.put(SqlCons.ACCOUNT_BALANCE,0);
        contentValAccounts.put(SqlCons.ACCOUNT_AVAILABLEBALANCE,500);
        contentValAccounts.put(SqlCons.ACCOUNT_OWNERID,2);
        contentValAccounts.put(SqlCons.ACCOUNT_OVERDRAFTLIMIT,500.00);
        contentValAccounts.put(SqlCons.ACCOUNT_TYPE,"Student");
        db.insert(SqlCons.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlCons.ACCOUNT_NAME, "Bills");
        contentValAccounts.put(SqlCons.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlCons.ACCOUNT_TYPE,"Subaccount");
        contentValAccounts.put(SqlCons.ACCOUNT_AVAILABLEBALANCE,0.00);
        contentValAccounts.put(SqlCons.ACCOUNT_BALANCE,0.00);
        db.insert(SqlCons.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        contentValAccounts = new ContentValues();
        contentValAccounts.put(SqlCons.ACCOUNT_NAME, "Holidays");
        contentValAccounts.put(SqlCons.ACCOUNT_BALANCE,100.00);
        contentValAccounts.put(SqlCons.ACCOUNT_AVAILABLEBALANCE,100.00);
        contentValAccounts.put(SqlCons.ACCOUNT_OWNERID,1);
        contentValAccounts.put(SqlCons.ACCOUNT_TYPE,"Subaccount");
        db.insert(SqlCons.ACCOUNTS_TABLE_NAME,null,contentValAccounts);

        ContentValues contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-06");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 51.83);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,992.49);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 15.14);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1044.32);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 10.99);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1059.46);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-05");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"BOOTS/0680 CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 9.99);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1070.45);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-03");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"Amazon UK Retail CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 2.82);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1080.44);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"W M MORRISON PLC CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 7.84);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1083.26);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-02-02");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"NPOWER 00000000000000");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Direct Debit");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 111.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1091.10);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"LNK NCASTLE BARAS CD");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Cashpoint");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 10.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1202.10);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-01-27");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"UNIVERSITY OF NEWC CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 987.84);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,1212.10);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-01-26");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"VIRGIN MEDIA PYMTS CD 7835");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 29.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,2199.94);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.TRANSACTION_DATE,"2015-01-20");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TIME,System.currentTimeMillis() / 1000L);
        contentValuesTransactions.put(SqlCons.TRANSACTION_DESCRIPTION,"MUCH LUV MUM");
        contentValuesTransactions.put(SqlCons.TRANSACTION_TYPE,"Debit Card");
        contentValuesTransactions.put(SqlCons.TRANSACTION_IN, 2228.94);
        contentValuesTransactions.put(SqlCons.TRANSACTION_OUT, 0.00);
        contentValuesTransactions.put(SqlCons.TRANSACTION_BALANCE,2228.94);
        contentValuesTransactions.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN,1);
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

        // Add all achievements
        ContentValues contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "£200 Incoming transaction");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You must receive an incoming transaction of at least £200 to complete this achievement.");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 50);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_1);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "£200 Outgoing transaction");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You must make a transfer of at least £200 to complete this achievement.");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 50);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_2);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "Transfer £10,000 in total");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You must make a total of £10,000 worth of outgoing transfers to any account to gain this achievement.");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 500);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_3);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "£5000 Balance");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You must have a main account balance of at least £5000");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 100);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_4);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "5 Logins");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You need to have logged in to the app at least 5 times to gain this achievement.");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 50);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_5);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        contentValAchievements = new ContentValues();
        contentValAchievements.put(SqlCons.ACHIEVEMENT_TITLE, "50 Logins");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_DESCRIPTION, "You need to have logged in to the app at least 50 times to gain this achievement.");
        contentValAchievements.put(SqlCons.ACHIEVEMENT_POINTS, 150);
        contentValAchievements.put(SqlCons.ACHIEVEMENT_ICON_ID, R.drawable.achievement_6);
        db.insert(SqlCons.ACHIEVEMENTS_TABLE_NAME, null, contentValAchievements);

        // Add achievements achieved by a customer
        contentValuesTransactions = new ContentValues();
        contentValuesTransactions.put(SqlCons.CUSTOMER_ACHIEVEMENT_ID, 1);
        contentValuesTransactions.put(SqlCons.CUSTOMER_ACHIEVEMENT_CUSTOMER_ID, 1);

        ContentValues availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.morrisons_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Morrisons Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 5% off certain products at Morrisons Shops");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,500);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.tesco_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Tesco Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 5% off certain products at Tesco Shops");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,600);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.mc_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Mc Donald's Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 10% cheaper Cheeseburgers !");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,300);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.sports_direct_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Sports Direct Socks");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 50% off socks at Sports Direct exclusive socks");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,500);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.waitrose_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Waitrose Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 5% off certain products at Waitrose Shops");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,800);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.subway_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Subway Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 15% off Steak & Cheese sandwich");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,350);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.lidl_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Lidl Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 5% off certain products at Lidl Shops");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,500);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

        availableOfferContentValue = new ContentValues();
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ICON, R.drawable.sainsburys_logo);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_NAME, "Sainsbury Saver");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_DESCRIPTION, "Get 5% off certain products at Sainsbury Shops");
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_PRICE,500);
        availableOfferContentValue.put(SqlCons.AVAIL_OFFER_ACTIVE, 0);
        db.insert(SqlCons.AVAIL_OFFERS_TABLE_NAME,null,availableOfferContentValue);

    }
}
