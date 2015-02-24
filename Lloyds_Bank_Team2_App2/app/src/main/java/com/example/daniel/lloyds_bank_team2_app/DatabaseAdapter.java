package com.example.daniel.lloyds_bank_team2_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

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

        String[] columns = {SqlValues.FIELD_PASSWORD};
        String query = SqlValues.FIELD_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlValues.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                String pass= cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_PASSWORD));
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
        String[] columns = SqlValues.ACCOUNTS_COLUMNS;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlValues.FIELD_OWNERID + " = '" + ownerid + "'";
        Cursor c = db.query(SqlValues.ACCOUNTS_TABLE_NAME,columns,query,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                for(int i = 0; i < c.getCount(); i++){
                    int id = c.getInt(c.getColumnIndex(SqlValues.FIELD_ACCOUNT_ID));
                    String accountType = c.getString(c.getColumnIndex(SqlValues.FIELD_ACCOUNTTYPE));
                    double accountBalance = c.getDouble(c.getColumnIndex(SqlValues.FIELD_BALANCE));
                    double availableBalance = c.getDouble(c.getColumnIndex(SqlValues.FIELD_AVAILABLE_BALANCE));
                    int ownerId = c.getInt(c.getColumnIndex(SqlValues.FIELD_OWNERID));
                    String accountName = c.getString(c.getColumnIndex(SqlValues.FIELD_ACCOUNTNAME));

                    if (!accountType.equals("Subaccount")){
                        int accountNumber = c.getInt(c.getColumnIndex(SqlValues.FIELD_ACCOUNTNO));
                        String sortCode = c.getString(c.getColumnIndex(SqlValues.FIELD_SORTCODE));
                        double overdraft = c.getDouble(c.getColumnIndex(SqlValues.FIELD_OVERDRAFT_LIMIT));

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
        String[] columnsT = SqlValues.TRANSACTIONS_COLUMNS;
        String queryT = SqlValues.FIELD_ACCOUNT_ID_REFERENCE + " = '" + id + "'";
        String order =  SqlValues.FIELD_DATE+" DESC";

        Cursor t = db.query(SqlValues.TRANSACTIONS_TABLE_NAME,columnsT,queryT,null,null,null,null);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put(SqlValues.FIELD_DATE, t.getString(t.getColumnIndex(SqlValues.FIELD_DATE)));
                    temp.put(SqlValues.FIELD_DESCRIPTION, t.getString(t.getColumnIndex(SqlValues.FIELD_DESCRIPTION)));
                    temp.put(SqlValues.FIELD_TYPE, t.getString(t.getColumnIndex(SqlValues.FIELD_TYPE)));
                    temp.put(SqlValues.FIELD_IN, t.getString(t.getColumnIndex(SqlValues.FIELD_IN)));
                    temp.put(SqlValues.FIELD_OUT, t.getString(t.getColumnIndex(SqlValues.FIELD_OUT)));
                    temp.put(SqlValues.FIELD_BALANCE, t.getString(t.getColumnIndex(SqlValues.FIELD_BALANCE)));
                    list.add(temp);

                    t.moveToNext();
                }
            }
        }
        db.close();
        return list;
    }

    public void transfer(Account source, Account destination, Double balance) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Time now = new Time();
        now.setToNow();
        String date = now.year + "-" + now.month + "-" + now.monthDay;

        String descSource = "To " + destination.getAccountName();
        String descDesctination = "From " + source.getAccountName();

        //Source transaction
        ContentValues sourceTransaction = new ContentValues();
        sourceTransaction.put(SqlValues.FIELD_DATE, date);
        sourceTransaction.put(SqlValues.FIELD_DESCRIPTION, descSource);
        sourceTransaction.put(SqlValues.FIELD_TYPE, "User Transaction");
        sourceTransaction.put(SqlValues.FIELD_IN, 0.00);
        sourceTransaction.put(SqlValues.FIELD_OUT, (0 - balance));
        sourceTransaction.put(SqlValues.FIELD_BALANCE, source.getAccountBalance() - balance);
        sourceTransaction.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE, source.getAccountID());
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,sourceTransaction);

        //Update Source
        ContentValues sourceAccount = new ContentValues();
        sourceAccount.put(SqlValues.FIELD_BALANCE, source.getAccountBalance() - balance);
        sourceAccount.put(SqlValues.FIELD_AVAILABLE_BALANCE, source.getAvailableBalance() - balance);
        db.update(SqlValues.ACCOUNTS_TABLE_NAME,sourceAccount,SqlValues.FIELD_ACCOUNT_ID +"="+source.getAccountID(),null);

        //Destination transaction
        ContentValues destinationTransaction = new ContentValues();
        destinationTransaction.put(SqlValues.FIELD_DATE, date);
        destinationTransaction.put(SqlValues.FIELD_DESCRIPTION, descDesctination);
        destinationTransaction.put(SqlValues.FIELD_TYPE, "User Transaction");
        destinationTransaction.put(SqlValues.FIELD_IN, balance);
        destinationTransaction.put(SqlValues.FIELD_OUT, 0.00);
        destinationTransaction.put(SqlValues.FIELD_BALANCE, source.getAccountBalance() + balance);
        destinationTransaction.put(SqlValues.FIELD_ACCOUNT_ID_REFERENCE, destination.getAccountID());
        db.insert(SqlValues.TRANSACTIONS_TABLE_NAME,null,destinationTransaction);

        //Update destination
        ContentValues destinationAccount = new ContentValues();
        destinationAccount.put(SqlValues.FIELD_BALANCE, destination.getAccountBalance() + balance);
        destinationAccount.put(SqlValues.FIELD_AVAILABLE_BALANCE, destination.getAvailableBalance() + balance);
        db.update(SqlValues.ACCOUNTS_TABLE_NAME,destinationAccount,SqlValues.FIELD_ACCOUNT_ID +"="+destination.getAccountID(),null);

        db.close();
    }


    public int getId(String userID){
        userID.replace("'","\'");
        String[] columns = {SqlValues.FIELD_CUSTOMER_ID};
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlValues.FIELD_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlValues.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(SqlValues.FIELD_CUSTOMER_ID));
                db.close();
                return id;
            }
        }
        db.close();
        return -1;
    }

    public Customer getCustomer(int id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = SqlValues.CUSTOMER_COLLUMNS;
        String query = SqlValues.FIELD_CUSTOMER_ID + " = '" + id + "'";
        Cursor cursor = db.query(SqlValues.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String firstName = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_SURNAME));
                String addressOne = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_ADDRESSONE));
                String addressTwo = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_ADDRESSTWO));
                String postcode = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_POSTCODE));
                String userId = cursor.getString(cursor.getColumnIndex(SqlValues.FIELD_USERID));

                Customer cust = new Customer(id,firstName,surname,addressOne,addressTwo,postcode,userId);

                db.close();
                return cust;
            }
        }

        db.close();
        return null;
    }


}
