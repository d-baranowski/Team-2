package com.example.daniel.lloyds_bank_team2_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 01/02/2015.
 */
public class DatabaseAdapter {
    DbHelp helper;

    public DatabaseAdapter(Context context){
        helper = new DbHelp(context);
    }

    /*
        Return true if password stored in database is same as the one passed as parameter

        This method will access the dummy database table Customers that stores customer details.
         It well then query the database to find a customer with given userID is in the database
         If the query is successful cursor object won't be null and its size (count) will be larger than 1
         Then the cursor will check if passed matches the password stored in database
     */
    public boolean login(String userID, String password){
        //Apostrophe is part of SQLite syntax. These lines prevent app from crashing if user entered values contain apostrophes
        userID.replace("'","\'");
        password.replace("'","\'");

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {DbHelp.FIELD_PASSWORD};
        String query = DbHelp.FIELD_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(DbHelp.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                String pass= cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_PASSWORD));
                if (password.equals(pass)){
                    db.close();
                    return true;
                }
            }
        }
        db.close();
        return false;
    }

    public ArrayList<Account> getAccounts(int ownerid){
        ArrayList<Account> accounts = new ArrayList<>();
        String[] columns = DbHelp.ACCOUNTS_COLUMNS;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = DbHelp.FIELD_OWNERID + " = '" + ownerid + "'";
        Cursor c = db.query(DbHelp.ACCOUNTS_TABLE_NAME,columns,query,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                for(int i = 0; i < c.getCount(); i++){
                    int id = c.getInt(c.getColumnIndex(DbHelp.FIELD_ACCOUNT_ID));
                    String accountType = c.getString(c.getColumnIndex(DbHelp.FIELD_ACCOUNTTYPE));
                    double accountBalance = c.getDouble(c.getColumnIndex(DbHelp.FIELD_BALANCE));
                    double availableBalance = c.getDouble(c.getColumnIndex(DbHelp.FIELD_AVAILABLE_BALANCE));
                    int ownerId = c.getInt(c.getColumnIndex(DbHelp.FIELD_OWNERID));
                    String accountName = c.getString(c.getColumnIndex(DbHelp.FIELD_ACCOUNTNAME));

                    if (!accountType.equals("Subaccount")){
                        int accountNumber = c.getInt(c.getColumnIndex(DbHelp.FIELD_ACCOUNTNO));
                        String sortCode = c.getString(c.getColumnIndex(DbHelp.FIELD_SORTCODE));
                        double overdraft = c.getDouble(c.getColumnIndex(DbHelp.FIELD_OVERDRAFT_LIMIT));

                        accounts.add(new Account(id, accountNumber, sortCode, accountName, accountType, accountBalance, availableBalance, overdraft, ownerId,getTransactions(id)));
                    } else {
                        accounts.add(new Account(id, accountName, accountType, accountBalance, availableBalance,ownerId,getTransactions(id)));
                    }

                    c.moveToNext();
                }
                db.close();
            }
        }
        db.close();
        return accounts;
    }

    public ArrayList<HashMap<String,String>> getTransactions(int id){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columnsT = DbHelp.TRANSACTIONS_COLUMNS;
        String queryT = DbHelp.FIELD_ACCOUNT_ID_REFERENCE + " = '" + id + "'";
        String order =  DbHelp.FIELD_DATE+" DESC";

        Cursor t = db.query(DbHelp.TRANSACTIONS_TABLE_NAME,columnsT,queryT,null,null,null,null);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put(DbHelp.FIELD_DATE, t.getString(t.getColumnIndex(DbHelp.FIELD_DATE)));
                    temp.put(DbHelp.FIELD_DESCRIPTION, t.getString(t.getColumnIndex(DbHelp.FIELD_DESCRIPTION)));
                    temp.put(DbHelp.FIELD_TYPE, t.getString(t.getColumnIndex(DbHelp.FIELD_TYPE)));
                    temp.put(DbHelp.FIELD_IN, t.getString(t.getColumnIndex(DbHelp.FIELD_IN)));
                    temp.put(DbHelp.FIELD_OUT, t.getString(t.getColumnIndex(DbHelp.FIELD_OUT)));
                    temp.put(DbHelp.FIELD_BALANCE, t.getString(t.getColumnIndex(DbHelp.FIELD_BALANCE)));
                    list.add(temp);

                    t.moveToNext();
                }
            }
        }
        db.close();
        return list;
    }


    public int getId(String userID){
        userID.replace("'","\'");
        String[] columns = {DbHelp.FIELD_CUSTOMER_ID};
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = DbHelp.FIELD_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(DbHelp.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(DbHelp.FIELD_CUSTOMER_ID));
                db.close();
                return id;
            }
        }
        db.close();
        return -1;
    }

    public Customer getCustomer(int id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = DbHelp.CUSTOMER_COLLUMNS;
        String query = DbHelp.FIELD_CUSTOMER_ID + " = '" + id + "'";
        Cursor cursor = db.query(DbHelp.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String firstName = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_SURNAME));
                String addressOne = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_ADDRESSONE));
                String addressTwo = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_ADDRESSTWO));
                String postcode = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_POSTCODE));
                String userId = cursor.getString(cursor.getColumnIndex(DbHelp.FIELD_USERID));

                Customer cust = new Customer(id,firstName,surname,addressOne,addressTwo,postcode,userId);

                db.close();
                return cust;
            }
        }

        db.close();
        return null;
    }

    static class DbHelp extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "lloyds";
        private static final int DATABASE_VERSION = 15;


        //Customer Table SQL
        private static final String CUSTOMER_TABLE_NAME = "Customers";
        private static final String FIELD_CUSTOMER_ID = "_id";
        private static final String FIELD_NAME = "FirstNAme";
        private static final String FIELD_SURNAME = "Surname";
        private static final String FIELD_ADDRESSONE = "AddressLine1";
        private static final String FIELD_ADDRESSTWO = "AddressLine2";
        private static final String FIELD_POSTCODE = "Postcode";
        private static final String FIELD_USERID = "UserID";
        private static final String FIELD_PASSWORD = "Password";
        private static final String[] CUSTOMER_COLLUMNS = {FIELD_CUSTOMER_ID,FIELD_NAME,FIELD_SURNAME,FIELD_ADDRESSONE,FIELD_ADDRESSTWO,FIELD_POSTCODE,FIELD_USERID,FIELD_PASSWORD};

        private static final String CREATE_CUSTOMER_TABLE =
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

        //Accounts table SQL
        private static final String ACCOUNTS_TABLE_NAME = "Accounts";
        private static final String FIELD_ACCOUNT_ID = "account_id";
        private static final String FIELD_ACCOUNTNO = "AccountNumber";
        private static final String FIELD_SORTCODE = "SortCode";
        private static final String FIELD_ACCOUNTNAME = "Name";
        private static final String FIELD_ACCOUNTTYPE = "Type";
        private static final String FIELD_BALANCE = "Balance";
        private static final String FIELD_AVAILABLE_BALANCE = "Available";
        private static final String FIELD_OVERDRAFT_LIMIT = "Overdraft";
        private static final String FIELD_OWNERID = "Owner";
        private static final String[] ACCOUNTS_COLUMNS = {FIELD_ACCOUNT_ID,FIELD_ACCOUNTNO,FIELD_SORTCODE,FIELD_ACCOUNTNAME,FIELD_ACCOUNTTYPE,FIELD_BALANCE,FIELD_AVAILABLE_BALANCE,FIELD_OVERDRAFT_LIMIT,FIELD_OWNERID};

        private static final String CREATE_ACCOUNTS_TABLE =
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

        //Transactions table SQL
        private static final String TRANSACTIONS_TABLE_NAME = "Transactions";
        private static final String FIELD_TRANSACTION_ID = "transaction_id";
        private static final String FIELD_DATE = "Date";
        private static final String FIELD_DESCRIPTION = "Description";
        private static final String FIELD_TYPE = "Type";
        private static final String FIELD_IN = "Income";
        private static final String FIELD_OUT = "Outcome";
        private static final String FIELD_TRANSACTION_BALANCE = "Balance";
        private static final String FIELD_ACCOUNT_ID_REFERENCE = "Account";

        private static final String[] TRANSACTIONS_COLUMNS = {FIELD_TRANSACTION_ID,FIELD_DATE,FIELD_DESCRIPTION,FIELD_TYPE,FIELD_IN,FIELD_OUT,FIELD_TRANSACTION_BALANCE, FIELD_ACCOUNT_ID_REFERENCE};

        private static final String CREATE_TRANSACTIONS_TABLE =
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

        public DbHelp(Context context){
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        //Executed when the database is created for the first time on the device
        @Override
        public void onCreate(SQLiteDatabase db) {
            //SQL query to create a table inside our database.
            db.execSQL(CREATE_CUSTOMER_TABLE);
            db.execSQL(CREATE_ACCOUNTS_TABLE);
            db.execSQL(CREATE_TRANSACTIONS_TABLE);

            //Create sample dummy data and instert into database
            ContentValues contentValCustomers = new ContentValues();
            contentValCustomers.put(FIELD_NAME, "Daniel");
            contentValCustomers.put(FIELD_SURNAME, "Baranowski");
            contentValCustomers.put(FIELD_ADDRESSONE, "123 Street Name");
            contentValCustomers.put(FIELD_ADDRESSTWO, "Newcastle Upon Tyne");
            contentValCustomers.put(FIELD_POSTCODE, "NE9 9HA");
            contentValCustomers.put(FIELD_USERID, 123456789);
            contentValCustomers.put(FIELD_PASSWORD, "password");
            db.insert(CUSTOMER_TABLE_NAME,null,contentValCustomers);

            ContentValues contentValAccounts = new ContentValues();
            contentValAccounts.put(FIELD_ACCOUNTNO,1111111); //7 Digit account number
            contentValAccounts.put(FIELD_SORTCODE,"30-11-11");
            contentValAccounts.put(FIELD_BALANCE,992.49);
            contentValAccounts.put(FIELD_AVAILABLE_BALANCE,1492.49);
            contentValAccounts.put(FIELD_OWNERID,1);
            contentValAccounts.put(FIELD_OVERDRAFT_LIMIT,500.00);
            contentValAccounts.put(FIELD_TYPE,"Student");
            db.insert(ACCOUNTS_TABLE_NAME,null,contentValAccounts);

            contentValAccounts = new ContentValues();
            contentValAccounts.put(FIELD_ACCOUNTNAME, "Bills");
            contentValAccounts.put(FIELD_OWNERID,1);
            contentValAccounts.put(FIELD_TYPE,"Subaccount");
            db.insert(ACCOUNTS_TABLE_NAME,null,contentValAccounts);

            contentValAccounts = new ContentValues();
            contentValAccounts.put(FIELD_ACCOUNTNAME, "Holidays");
            contentValAccounts.put(FIELD_BALANCE,100.00);
            contentValAccounts.put(FIELD_AVAILABLE_BALANCE,100.00);
            contentValAccounts.put(FIELD_OWNERID,1);
            contentValAccounts.put(FIELD_TYPE,"Subaccount");
            db.insert(ACCOUNTS_TABLE_NAME,null,contentValAccounts);

            ContentValues contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-06");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 51.83);
            contentValuesTransactions.put(FIELD_BALANCE,992.49);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-05");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 15.14);
            contentValuesTransactions.put(FIELD_BALANCE,1044.32);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-05");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"Amazon UK Retail CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 10.99);
            contentValuesTransactions.put(FIELD_BALANCE,1059.46);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-05");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"BOOTS/0680 CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 9.99);
            contentValuesTransactions.put(FIELD_BALANCE,1070.45);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-03");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"Amazon UK Retail CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 2.82);
            contentValuesTransactions.put(FIELD_BALANCE,1080.44);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-02");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"W M MORRISON PLC CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 7.84);
            contentValuesTransactions.put(FIELD_BALANCE,1083.26);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-02-02");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"NPOWER 00000000000000");
            contentValuesTransactions.put(FIELD_TYPE,"Direct Debit");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 111.00);
            contentValuesTransactions.put(FIELD_BALANCE,1091.10);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-01-27");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"LNK NCASTLE BARAS CD");
            contentValuesTransactions.put(FIELD_TYPE,"Cashpoint");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 10.00);
            contentValuesTransactions.put(FIELD_BALANCE,1202.10);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-01-27");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"UNIVERSITY OF NEWC CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 987.84);
            contentValuesTransactions.put(FIELD_BALANCE,1212.10);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-01-26");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"VIRGIN MEDIA PYMTS CD 7835");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 0.00);
            contentValuesTransactions.put(FIELD_OUT, 29.00);
            contentValuesTransactions.put(FIELD_BALANCE,2199.94);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);

            contentValuesTransactions = new ContentValues();
            contentValuesTransactions.put(FIELD_DATE,"2015-01-20");
            contentValuesTransactions.put(FIELD_DESCRIPTION,"MUCH LUV MUM");
            contentValuesTransactions.put(FIELD_TYPE,"Debit Card");
            contentValuesTransactions.put(FIELD_IN, 2228.94);
            contentValuesTransactions.put(FIELD_OUT, 0.00);
            contentValuesTransactions.put(FIELD_BALANCE,2228.94);
            contentValuesTransactions.put(FIELD_ACCOUNT_ID_REFERENCE,1);
            db.insert(TRANSACTIONS_TABLE_NAME,null,contentValuesTransactions);
        }

        //Executed when the DATABASE_VERSION variable value is increased, when we change something about the database, create new tables or change columns etc...
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //SQL query to delete customer table if it exists
            final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + CUSTOMER_TABLE_NAME;
            final String DROP_ACCOUNTS_TABLE = "DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME;
            final String DROP_TRANSACTIONS_TABLE = "DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE_NAME;
            db.execSQL(DROP_CUSTOMER_TABLE);
            db.execSQL(DROP_ACCOUNTS_TABLE);
            db.execSQL(DROP_TRANSACTIONS_TABLE);
            onCreate(db);
        }
    }

}
