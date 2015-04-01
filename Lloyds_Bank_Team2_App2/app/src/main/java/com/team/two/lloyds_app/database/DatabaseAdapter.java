package com.team.two.lloyds_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by Daniel on 01/02/2015.
 */
public class DatabaseAdapter {

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private final int MAX_TRACKBACK_DAYS = 50;
    private final double FALLBACK_BALANCE = 0;
    private final DbHelp helper;

    public DatabaseAdapter(Context context){
        helper = new DbHelp(context);
    }

    /** Return true if password stored in database is same as the one passed as parameter

     This method will access the dummy database table Customers that stores customer details.
     It well then query the database to find a customer with given userID is in the database
     If the query is successful cursor object won't be null and its size (count) will be larger than 1
     Then the cursor will check if passed matches the password stored in database
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean login(String userID, String password){
        //Apostrophe is part of SQLite syntax. These lines prevent app from crashing if user entered values contain apostrophes
        userID.replace("'","\'");
        password.replace("'","\'");

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {SqlCons.CUSTOMER_PASSWORD};
        String query = SqlCons.CUSTOMER_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                String pass= cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_PASSWORD));
                if (password.equals(pass)){
                    db.close();
                    return true;
                }
            }
        }
        db.close();
        return false;
    }

    public ArrayList<Account> getAccounts(int ownerId){
        ArrayList<Account> accounts = new ArrayList<>();
        String[] columns = SqlCons.ACCOUNT_COLUMNS;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlCons.ACCOUNT_OWNERID + " = '" + ownerId + "'";
        Cursor c = db.query(SqlCons.ACCOUNTS_TABLE_NAME,columns,query,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                for(int i = 0; i < c.getCount(); i++){
                    int id = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_ID));
                    String accountType = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_TYPE));
                    double accountBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_BALANCE));
                    double availableBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_AVAILABLEBALANCE));
                    String accountName = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_NAME));

                    if (!accountType.equals("Subaccount")){
                        int accountNumber = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_NUMBER));
                        String sortCode = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_SORTCODE));
                        double overdraft = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_OVERDRAFTLIMIT));

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
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columnsT = SqlCons.TRANSACTION_COLUMNS;
        String queryT = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + id + "'";
        String order =  SqlCons.TRANSACTION_DATE +" DESC, " + SqlCons.TRANSACTION_TIME + " DESC";

        Cursor t = db.query(SqlCons.TRANSACTIONS_TABLE_NAME,columnsT,queryT,null,null,null,order);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put(SqlCons.TRANSACTION_DATE, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_DATE)));
                    temp.put(SqlCons.TRANSACTION_DESCRIPTION, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_DESCRIPTION)));
                    temp.put(SqlCons.TRANSACTION_TYPE, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_TYPE)));
                    temp.put(SqlCons.TRANSACTION_IN, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_IN)));
                    temp.put(SqlCons.TRANSACTION_OUT, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_OUT)));
                    temp.put(SqlCons.TRANSACTION_BALANCE, t.getString(t.getColumnIndex(SqlCons.TRANSACTION_BALANCE)));
                    list.add(temp);

                    t.moveToNext();
                }
            }
        }
        db.close();
        return list;
    }

    public void transfer(Account source, Account destination, Double balance) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Time now = new Time();
        now.setToNow();
        long time = System.currentTimeMillis() / 1000L;
        String date = now.year + "-" + now.month + "-" + now.monthDay;

        String descSource = "To " + destination.getAccountName();
        String descDesctination = "From " + source.getAccountName();

        //Source transaction
        ContentValues sourceTransaction = new ContentValues();
        sourceTransaction.put(SqlCons.TRANSACTION_DATE, date);
        sourceTransaction.put(SqlCons.TRANSACTION_TIME, time);
        sourceTransaction.put(SqlCons.TRANSACTION_DESCRIPTION, descSource);
        sourceTransaction.put(SqlCons.TRANSACTION_TYPE, "User Transaction");
        sourceTransaction.put(SqlCons.TRANSACTION_IN, 0.00);
        sourceTransaction.put(SqlCons.TRANSACTION_OUT, (balance));
        sourceTransaction.put(SqlCons.TRANSACTION_BALANCE, source.getAccountBalance() - balance);
        sourceTransaction.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN, source.getAccountID());
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,sourceTransaction);

        //Update Source
        ContentValues sourceAccount = new ContentValues();
        sourceAccount.put(SqlCons.ACCOUNT_BALANCE, source.getAccountBalance() - balance);
        sourceAccount.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, source.getAvailableBalance() - balance);
        db.update(SqlCons.ACCOUNTS_TABLE_NAME,sourceAccount, SqlCons.ACCOUNT_ID +"="+source.getAccountID(),null);

        //Destination transaction
        ContentValues destinationTransaction = new ContentValues();
        destinationTransaction.put(SqlCons.TRANSACTION_DATE, date);
        destinationTransaction.put(SqlCons.TRANSACTION_TIME, time);
        destinationTransaction.put(SqlCons.TRANSACTION_DESCRIPTION, descDesctination);
        destinationTransaction.put(SqlCons.TRANSACTION_TYPE, "User Transaction");
        destinationTransaction.put(SqlCons.TRANSACTION_IN, balance);
        destinationTransaction.put(SqlCons.TRANSACTION_OUT, 0.00);
        destinationTransaction.put(SqlCons.TRANSACTION_BALANCE, destination.getAccountBalance() + balance);
        destinationTransaction.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN, destination.getAccountID());
        db.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,destinationTransaction);

        //Update destination
        ContentValues destinationAccount = new ContentValues();
        destinationAccount.put(SqlCons.ACCOUNT_BALANCE, destination.getAccountBalance() + balance);
        destinationAccount.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, destination.getAvailableBalance() + balance);
        db.update(SqlCons.ACCOUNTS_TABLE_NAME,destinationAccount, SqlCons.ACCOUNT_ID +"="+destination.getAccountID(),null);

        db.close();
    }

    public void payment(Account source, Recipient destination, Double balance, String description) {
        SQLiteDatabase dba = helper.getWritableDatabase();
        Time now = new Time();
        now.setToNow();
        String date = now.year + "-" + now.month + "-" + now.monthDay;

        //Source transaction
        ContentValues sourceTransaction = new ContentValues();
        sourceTransaction.put(SqlCons.TRANSACTION_DATE, date);
        sourceTransaction.put(SqlCons.TRANSACTION_DESCRIPTION, description);
        sourceTransaction.put(SqlCons.TRANSACTION_TYPE, "User Transaction");
        sourceTransaction.put(SqlCons.TRANSACTION_IN, 0.00);
        sourceTransaction.put(SqlCons.TRANSACTION_OUT, balance);
        sourceTransaction.put(SqlCons.TRANSACTION_BALANCE, source.getAccountBalance() - balance);
        sourceTransaction.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN, source.getAccountID());
        dba.insert(SqlCons.TRANSACTIONS_TABLE_NAME, null, sourceTransaction);

        //Update Source
        ContentValues sourceAccount = new ContentValues();
        sourceAccount.put(SqlCons.ACCOUNT_BALANCE, source.getAccountBalance() - balance);
        sourceAccount.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, source.getAvailableBalance() - balance);
        dba.update(SqlCons.ACCOUNTS_TABLE_NAME, sourceAccount, SqlCons.ACCOUNT_ID + "=" + source.getAccountID(), null);

        String[] columnsA = SqlCons.ACCOUNT_COLUMNS;
        String queryA = SqlCons.ACCOUNT_NUMBER + " = '" + destination.getAccountNumber() + "' AND " + SqlCons.ACCOUNT_SORTCODE + " = '" + destination.getSortCode() + "'";
        dba.close();

        dba = helper.getWritableDatabase();

        Cursor c = dba.query(SqlCons.ACCOUNTS_TABLE_NAME,columnsA,queryA,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                Account destinationAccount;
                c.moveToFirst();

                int id = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_ID));
                String accountType = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_TYPE));
                double accountBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_BALANCE));
                double availableBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_AVAILABLEBALANCE));
                String accountName = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_NAME));
                int accountNumber = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_NUMBER));
                String sortCode = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_SORTCODE));
                double overdraft = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_OVERDRAFTLIMIT));
                destinationAccount = new Account(id, accountNumber, sortCode, accountName, accountType, accountBalance, availableBalance, overdraft, id,getTransactions(id));

                dba.close();
                dba = helper.getWritableDatabase();

                //Destination transaction
                ContentValues destinationTransaction = new ContentValues();
                destinationTransaction.put(SqlCons.TRANSACTION_DATE, date);
                destinationTransaction.put(SqlCons.TRANSACTION_DESCRIPTION, description);
                destinationTransaction.put(SqlCons.TRANSACTION_TYPE, "User Transaction");
                destinationTransaction.put(SqlCons.TRANSACTION_IN, balance);
                destinationTransaction.put(SqlCons.TRANSACTION_OUT, 0.00);
                destinationTransaction.put(SqlCons.TRANSACTION_BALANCE, destinationAccount.getAccountBalance() + balance);
                destinationTransaction.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN, destinationAccount.getAccountID());
                dba.insert(SqlCons.TRANSACTIONS_TABLE_NAME,null,destinationTransaction);

                //Update destination
                ContentValues destinationAccountContent = new ContentValues();
                destinationAccountContent.put(SqlCons.ACCOUNT_BALANCE, destinationAccount.getAccountBalance() + balance);
                destinationAccountContent.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, destinationAccount.getAvailableBalance() + balance);
                dba.update(SqlCons.ACCOUNTS_TABLE_NAME,destinationAccountContent, SqlCons.ACCOUNT_ID +"="+destinationAccount.getAccountID(),null);


            }
        }

        dba.close();
    }

    public void addRecipient(int ownerId, String name, String sortCode, int accountNumber){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentRecipient = new ContentValues();
        contentRecipient.put(SqlCons.RECIPIENT_OWNER_ID,ownerId);
        contentRecipient.put(SqlCons.RECIPIENT_NAME, name);
        contentRecipient.put(SqlCons.RECIPIENT_ACCOUNTNUMBER,accountNumber);
        contentRecipient.put(SqlCons.RECIPIENT_SORTCODE,sortCode);
        db.insert(SqlCons.RECIPIENTS_TABLE_NAME,null,contentRecipient);

        db.close();
    }

    public ArrayList<Recipient> getRecipients(int ownerId){
        ArrayList<Recipient> temp = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columnsR = SqlCons.RECIPIENT_COLUMNS;
        String queryR = SqlCons.RECIPIENT_OWNER_ID + " = '" + ownerId + "'";

        Cursor t = db.query(SqlCons.RECIPIENTS_TABLE_NAME,columnsR,queryR,null,null,null,null);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    int id = t.getInt(t.getColumnIndex(SqlCons.RECIPIENT_ID));
                    String name = t.getString(t.getColumnIndex(SqlCons.RECIPIENT_NAME));
                    String sortCode = t.getString(t.getColumnIndex(SqlCons.RECIPIENT_SORTCODE));
                    int accountNumber = t.getInt(t.getColumnIndex(SqlCons.RECIPIENT_ACCOUNTNUMBER));

                    temp.add(new Recipient(id, name, sortCode, accountNumber, ownerId));

                    t.moveToNext();
                }

                db.close();
                return temp;
            }
        }

        db.close();
        return temp;
    }

    public Customer getCustomer(int id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = SqlCons.CUSTOMER_COLUMNS;
        String query = SqlCons.CUSTOMER_ID + " = '" + id + "'";
        Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String firstName = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_SURNAME));
                String addressOne = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_ADDRESSONE));
                String addressTwo = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_ADDRESSTWO));
                String postcode = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_POSTCODE));
                String userId = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_USERID));

                Customer cust = new Customer(id,firstName,surname,addressOne,addressTwo,postcode,userId);

                db.close();
                return cust;
            }
        }

        db.close();
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public int getId(String userID){
        userID.replace("'","\'");
        String[] columns = {SqlCons.CUSTOMER_ID};
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlCons.CUSTOMER_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(SqlCons.CUSTOMER_ID));
                db.close();
                return id;
            }
        }
        db.close();
        return -1;
    }

    public TreeMap<Calendar, Double> getBalanceDateMap(int customerID, Calendar startDate, int numDays){
        SQLiteDatabase db = helper.getReadableDatabase();
        String query;

        TreeMap<Calendar, Double> balanceByDate = new TreeMap<>();
        Calendar date = (Calendar) startDate.clone();
        Double balance = getPreviousBalance(startDate, customerID);

        for(int i = 0; i < numDays; i++){
            query = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + customerID + "' AND " + SqlCons.TRANSACTION_DATE + " = '" + df.format(date.getTime()) + "'";
            Cursor cursor = db.query(SqlCons.TRANSACTIONS_TABLE_NAME, SqlCons.TRANSACTION_COLUMNS, query, null, null, null, null);

            //If there was a transaction on this date, update the latest balance
            if(cursor.getCount() > 0){
                cursor.moveToLast();
                balance = cursor.getDouble(cursor.getColumnIndex(SqlCons.TRANSACTION_BALANCE));
            }

            //Use the latest balance and add to map
            balanceByDate.put(date, balance);
            date = (Calendar) date.clone();
            date.add(Calendar.DAY_OF_YEAR, 1);
            cursor.close();
        }

        return balanceByDate;
    }

    private double getPreviousBalance(Calendar beforeDate, int customerID){
        Calendar date = (Calendar) beforeDate.clone();
        SQLiteDatabase db = helper.getReadableDatabase();
        String query;

        for(int i = 0; i < MAX_TRACKBACK_DAYS;i++) {

            query = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + customerID + "' AND " + SqlCons.TRANSACTION_DATE + " = '" + df.format(date.getTime()) + "'";
            Cursor cursor = db.query(SqlCons.TRANSACTIONS_TABLE_NAME, SqlCons.TRANSACTION_COLUMNS, query, null, null, null, null);

            if(cursor.getCount() > 0){
                cursor.moveToLast();
                return cursor.getDouble(cursor.getColumnIndex(SqlCons.TRANSACTION_BALANCE));
            }
            cursor.close();
            date.add(Calendar.DAY_OF_YEAR, -1);
        }
        return FALLBACK_BALANCE;
    }

    public TreeMap<Calendar, Double> getSpendingDateMap(int customerID, Calendar startDate, int numDays){
        TreeMap<Calendar, Double> spendingDateMap = new TreeMap<>();

        Calendar date = (Calendar) startDate.clone();
        SQLiteDatabase db = helper.getReadableDatabase();
        String query;

        for(int i = 0; i < numDays; i++) {

            query = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + customerID + "' AND " + SqlCons.TRANSACTION_DATE + " = '" + df.format(date.getTime()) + "'";
            Cursor cursor = db.query(SqlCons.TRANSACTIONS_TABLE_NAME, SqlCons.TRANSACTION_COLUMNS, query, null, null, null, null);
            double amount = 0;

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                boolean rowsRemain = true;
                while(rowsRemain) {
                    amount = amount + cursor.getDouble(cursor.getColumnIndex(SqlCons.TRANSACTION_OUT));
                    rowsRemain = cursor.moveToNext();
                }
            }

            cursor.close();
            spendingDateMap.put(date, amount);
            date = (Calendar) date.clone();
            date.add(Calendar.DAY_OF_YEAR, -1);
        }

        return spendingDateMap;
    }



}
