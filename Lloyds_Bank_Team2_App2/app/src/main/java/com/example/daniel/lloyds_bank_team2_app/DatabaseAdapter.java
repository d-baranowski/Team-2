package com.example.daniel.lloyds_bank_team2_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 01/02/2015.
 */
public class DatabaseAdapter {
    DatabaseHelper helper;

    public DatabaseAdapter(Context context){
        helper = new DatabaseHelper(context);
    }

    /*
        Return true if password stored in database is same as the one passed as parameter

        This method will access the dummy database table Customers that stores customer details.
         It well then query the database to find a customer with given userID is in the database
         If the query is successful cursor object won't be null and its size (count) will be larger than 1
         Then the cursor will check if passed matches the password stored in database
     */
    public boolean login(String userID, String password){
        //Apostrophe is part of SQLite syntax. These lines prevent app from crashing if user entered values contain apostrphes
        userID.replace("'","\'");
        password.replace("'","\'");

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DatabaseHelper.FIELD_PASSWORD};
        String query = DatabaseHelper.FIELD_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(DatabaseHelper.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                String pass= cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIELD_PASSWORD));
                if (password.equals(pass)){
                    db.close();
                    return true;
                }
            }
        }
        db.close();
        return false;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "lloydsdb";
        private static final int DATABASE_VERSION = 3;

        private static final String CUSTOMER_TABLE_NAME = "Customers";
        private static final String FIELD_NAME = "FirstNAme";
        private static final String FIELD_SURNAME = "Surname";
        private static final String FIELD_ADDRESSONE = "AddressLine1";
        private static final String FIELD_ADDRESSTWO = "AddressLine2";
        private static final String FIELD_POSTCODE = "Postcode";
        private static final String FIELD_USERID = "UserID";
        private static final String FIELD_PASSWORD = "Password";

        private static final String CREATE_CUSTOMER_TABLE =
                "CREATE TABLE "+
                        CUSTOMER_TABLE_NAME + " (_id	INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        FIELD_NAME+"   TEXT NOT NULL,"+
                        FIELD_SURNAME+"	TEXT NOT NULL,"+
                        FIELD_ADDRESSONE+"	TEXT NOT NULL,"+
                        FIELD_ADDRESSTWO+"	TEXT NOT NULL,"+
                        FIELD_POSTCODE+"	TEXT NOT NULL,"+
                        FIELD_USERID+"	INTEGER NOT NULL UNIQUE ,"+
                        FIELD_PASSWORD+"	TEXT NOT NULL);";

        private static final String ACCOUNTS_TABLE_NAME = "Accounts";
        private static final String FIELD_ACCOUNTNO = "AccountNumber";
        private static final String FIELD_SORTCODE = "SortCode";
        private static final String FIELD_ACCOUNTNAME = "Name";
        private static final String FIELD_ACCOUNTTYPE = "Type";
        private static final String FIELD_BALANCE = "Balance";
        private static final String FIELD_AVAILABLE_BALANCE = "Available";
        private static final String FIELD_OVERDRAFT_LIMIT = "Overdraft";
        private static final String FIELD_OWNERID = "Owner";

        private static final String CREATE_ACCOUNTS_TABLE =
                "CREATE TABLE " +
                        ACCOUNTS_TABLE_NAME + " (" +
                        FIELD_ACCOUNTNO + " INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                        FIELD_SORTCODE + " STRING NOT NULL," +
                        FIELD_ACCOUNTNAME + " STRING DEFAULT 'Account'," +
                        FIELD_ACCOUNTTYPE + " STRING," +
                        FIELD_BALANCE + "	REAL," +
                        FIELD_AVAILABLE_BALANCE + " REAL DEFAULT 0," +
                        FIELD_OVERDRAFT_LIMIT + " REAL DEFAULT 0," +
                        FIELD_OWNERID + "	INTEGER REFERENCES Customers(ID));";


        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        //Executed when the database is created for the first time on the device
        @Override
        public void onCreate(SQLiteDatabase db) {
            //SQL query to create a table inside our database.


            db.execSQL(CREATE_CUSTOMER_TABLE);
            db.execSQL(CREATE_ACCOUNTS_TABLE);

            //Create sample dummy data and instert into database
            ContentValues contentValCustomers = new ContentValues();
            contentValCustomers.put(FIELD_NAME, "Daniel");
            contentValCustomers.put(FIELD_SURNAME, "Baranowski");
            contentValCustomers.put(FIELD_ADDRESSONE, "123 Street Name");
            contentValCustomers.put(FIELD_ADDRESSTWO, "Newcastle Upon Tyne");
            contentValCustomers.put(FIELD_POSTCODE, "NE9 9HA");
            contentValCustomers.put(FIELD_USERID, 123456789);
            contentValCustomers.put(FIELD_PASSWORD, "password");

            ContentValues contentValAccounts = new ContentValues();
            contentValAccounts.put(FIELD_ACCOUNTNO,1111111); //7 Digit account number
            contentValAccounts.put(FIELD_SORTCODE,30-11-11);
            contentValAccounts.put(FIELD_BALANCE,100.0);
            contentValAccounts.put(FIELD_AVAILABLE_BALANCE,50.54);
            contentValAccounts.put(FIELD_OWNERID,1);

            db.insert(CUSTOMER_TABLE_NAME,null,contentValCustomers);
            db.insert(ACCOUNTS_TABLE_NAME,null,contentValAccounts);
        }

        //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //SQL query to delete customer table if it exists
            final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + CUSTOMER_TABLE_NAME;
            final String DROP_ACCOUNTS_TABLE = "DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME;
            db.execSQL(DROP_CUSTOMER_TABLE);
            db.execSQL(DROP_ACCOUNTS_TABLE);
            onCreate(db);
        }
    }

}
