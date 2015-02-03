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
        private static final int DATABASE_VERSION = 1;

        private static final String CUSTOMER_TABLE_NAME = "Customers";
        private static final String FIELD_ACCOUNTNO = "AccountNumber";
        private static final String FIELD_NAME = "FirstNAme";
        private static final String FIELD_SURNAME = "Surname";
        private static final String FIELD_ADDRESSONE = "AddressLine1";
        private static final String FIELD_ADDRESSTWO = "AddressLine2";
        private static final String FIELD_POSTCODE = "Postcode";
        private static final String FIELD_USERID = "UserID";
        private static final String FIELD_PASSWORD = "Password";

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        //Executed when the database is created for the first time on the device
        @Override
        public void onCreate(SQLiteDatabase db) {
            //SQL query to create a table inside our database.
            final String CREATE_CUSTOMER_TABLE =
                    "CREATE TABLE "+
                            CUSTOMER_TABLE_NAME + " (_id	INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            FIELD_ACCOUNTNO+"	INTEGER NOT NULL UNIQUE,"+
                            FIELD_NAME+"   TEXT NOT NULL,"+
                            FIELD_SURNAME+"	TEXT NOT NULL,"+
                            FIELD_ADDRESSONE+"	TEXT NOT NULL,"+
                            FIELD_ADDRESSTWO+"	TEXT NOT NULL,"+
                            FIELD_POSTCODE+"	TEXT NOT NULL,"+
                            FIELD_USERID+"	INTEGER NOT NULL UNIQUE ,"+
                            FIELD_PASSWORD+"	TEXT NOT NULL);";

            db.execSQL(CREATE_CUSTOMER_TABLE);

            //Create sample dummy data and instert into database
            ContentValues contentValues = new ContentValues();
            contentValues.put(FIELD_ACCOUNTNO, 12345678);
            contentValues.put(FIELD_NAME, "Daniel");
            contentValues.put(FIELD_SURNAME, "Baranowski");
            contentValues.put(FIELD_ADDRESSONE,"123 Streen Name");
            contentValues.put(FIELD_ADDRESSTWO,"Newcastle Upon Tyne");
            contentValues.put(FIELD_POSTCODE,"NE9 9HA");
            contentValues.put(FIELD_USERID,123456789);
            contentValues.put(FIELD_PASSWORD,"password");

            db.insert(CUSTOMER_TABLE_NAME,null,contentValues);
        }

        //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //SQL query to delete customer table if it exists
            final String DROP_CUSTOMER_TABLE = "DROP TABLE" + CUSTOMER_TABLE_NAME + "IF EXISTS";
            db.execSQL(DROP_CUSTOMER_TABLE);
            onCreate(db);
        }
    }

}
