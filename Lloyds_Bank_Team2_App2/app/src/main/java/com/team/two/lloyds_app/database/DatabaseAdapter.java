package com.team.two.lloyds_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;

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

    /** Return true if password stored in database is same as the one passed as parameter

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

        String[] columns = {SqlConst.CUSTOMER_PASSWORD};
        String query = SqlConst.CUSTOMER_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlConst.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                String pass= cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_PASSWORD));
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
        String[] columns = SqlConst.ACCOUNT_COLUMNS;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlConst.ACCOUNT_OWNERID + " = '" + ownerId + "'";
        Cursor c = db.query(SqlConst.ACCOUNTS_TABLE_NAME,columns,query,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                for(int i = 0; i < c.getCount(); i++){
                    int id = c.getInt(c.getColumnIndex(SqlConst.ACCOUNT_ID));
                    String accountType = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_TYPE));
                    double accountBalance = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_BALANCE));
                    double availableBalance = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_AVAILABLEBALANCE));
                    String accountName = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_NAME));

                    if (!accountType.equals("Subaccount")){
                        int accountNumber = c.getInt(c.getColumnIndex(SqlConst.ACCOUNT_NUMBER));
                        String sortCode = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_SORTCODE));
                        double overdraft = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_OVERDRAFTLIMIT));

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
        String[] columnsT = SqlConst.TRANSACTION_COLUMNS;
        String queryT = SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + id + "'";
        String order =  SqlConst.TRANSACTION_DATE +" DESC";

        Cursor t = db.query(SqlConst.TRANSACTIONS_TABLE_NAME,columnsT,queryT,null,null,null,null);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put(SqlConst.TRANSACTION_DATE, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_DATE)));
                    temp.put(SqlConst.TRANSACTION_DESCRIPTION, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_DESCRIPTION)));
                    temp.put(SqlConst.TRANSACTION_TYPE, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_TYPE)));
                    temp.put(SqlConst.TRANSACTION_IN, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_IN)));
                    temp.put(SqlConst.TRANSACTION_OUT, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_OUT)));
                    temp.put(SqlConst.TRANSACTION_BALANCE, t.getString(t.getColumnIndex(SqlConst.TRANSACTION_BALANCE)));
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
        String date = now.year + "-" + now.month + "-" + now.monthDay;

        String descSource = "To " + destination.getAccountName();
        String descDesctination = "From " + source.getAccountName();

        //Source transaction
        ContentValues sourceTransaction = new ContentValues();
        sourceTransaction.put(SqlConst.TRANSACTION_DATE, date);
        sourceTransaction.put(SqlConst.TRANSACTION_DESCRIPTION, descSource);
        sourceTransaction.put(SqlConst.TRANSACTION_TYPE, "User Transaction");
        sourceTransaction.put(SqlConst.TRANSACTION_IN, 0.00);
        sourceTransaction.put(SqlConst.TRANSACTION_OUT, (balance));
        sourceTransaction.put(SqlConst.TRANSACTION_BALANCE, source.getAccountBalance() - balance);
        sourceTransaction.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN, source.getAccountID());
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,sourceTransaction);

        //Update Source
        ContentValues sourceAccount = new ContentValues();
        sourceAccount.put(SqlConst.ACCOUNT_BALANCE, source.getAccountBalance() - balance);
        sourceAccount.put(SqlConst.ACCOUNT_AVAILABLEBALANCE, source.getAvailableBalance() - balance);
        db.update(SqlConst.ACCOUNTS_TABLE_NAME,sourceAccount, SqlConst.ACCOUNT_ID +"="+source.getAccountID(),null);

        //Destination transaction
        ContentValues destinationTransaction = new ContentValues();
        destinationTransaction.put(SqlConst.TRANSACTION_DATE, date);
        destinationTransaction.put(SqlConst.TRANSACTION_DESCRIPTION, descDesctination);
        destinationTransaction.put(SqlConst.TRANSACTION_TYPE, "User Transaction");
        destinationTransaction.put(SqlConst.TRANSACTION_IN, balance);
        destinationTransaction.put(SqlConst.TRANSACTION_OUT, 0.00);
        destinationTransaction.put(SqlConst.TRANSACTION_BALANCE, destination.getAccountBalance() + balance);
        destinationTransaction.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN, destination.getAccountID());
        db.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,destinationTransaction);

        //Update destination
        ContentValues destinationAccount = new ContentValues();
        destinationAccount.put(SqlConst.ACCOUNT_BALANCE, destination.getAccountBalance() + balance);
        destinationAccount.put(SqlConst.ACCOUNT_AVAILABLEBALANCE, destination.getAvailableBalance() + balance);
        db.update(SqlConst.ACCOUNTS_TABLE_NAME,destinationAccount, SqlConst.ACCOUNT_ID +"="+destination.getAccountID(),null);

        db.close();
    }

    public void payment(Account source, Recipient destination, Double balance, String description) {
        SQLiteDatabase dba = helper.getWritableDatabase();
        Time now = new Time();
        now.setToNow();
        String date = now.year + "-" + now.month + "-" + now.monthDay;

        //Source transaction
        ContentValues sourceTransaction = new ContentValues();
        sourceTransaction.put(SqlConst.TRANSACTION_DATE, date);
        sourceTransaction.put(SqlConst.TRANSACTION_DESCRIPTION, description);
        sourceTransaction.put(SqlConst.TRANSACTION_TYPE, "User Transaction");
        sourceTransaction.put(SqlConst.TRANSACTION_IN, 0.00);
        sourceTransaction.put(SqlConst.TRANSACTION_OUT, balance);
        sourceTransaction.put(SqlConst.TRANSACTION_BALANCE, source.getAccountBalance() - balance);
        sourceTransaction.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN, source.getAccountID());
        dba.insert(SqlConst.TRANSACTIONS_TABLE_NAME, null, sourceTransaction);

        //Update Source
        ContentValues sourceAccount = new ContentValues();
        sourceAccount.put(SqlConst.ACCOUNT_BALANCE, source.getAccountBalance() - balance);
        sourceAccount.put(SqlConst.ACCOUNT_AVAILABLEBALANCE, source.getAvailableBalance() - balance);
        dba.update(SqlConst.ACCOUNTS_TABLE_NAME, sourceAccount, SqlConst.ACCOUNT_ID + "=" + source.getAccountID(), null);

        String[] columnsA = SqlConst.ACCOUNT_COLUMNS;
        String queryA = SqlConst.ACCOUNT_NUMBER + " = '" + destination.getAccountNumber() + "' AND " + SqlConst.ACCOUNT_SORTCODE + " = '" + destination.getSortCode() + "'";
        dba = helper.getWritableDatabase();
        Cursor c = dba.query(SqlConst.ACCOUNTS_TABLE_NAME,columnsA,queryA,null,null,null,null);

        if (c != null) {
            if (c.getCount() > 0) {
                Account destinationAccount;
                c.moveToFirst();

                int id = c.getInt(c.getColumnIndex(SqlConst.ACCOUNT_ID));
                String accountType = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_TYPE));
                double accountBalance = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_BALANCE));
                double availableBalance = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_AVAILABLEBALANCE));
                String accountName = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_NAME));
                int accountNumber = c.getInt(c.getColumnIndex(SqlConst.ACCOUNT_NUMBER));
                String sortCode = c.getString(c.getColumnIndex(SqlConst.ACCOUNT_SORTCODE));
                double overdraft = c.getDouble(c.getColumnIndex(SqlConst.ACCOUNT_OVERDRAFTLIMIT));
                destinationAccount = new Account(id, accountNumber, sortCode, accountName, accountType, accountBalance, availableBalance, overdraft, id,getTransactions(id));


                //Destination transaction
                ContentValues destinationTransaction = new ContentValues();
                destinationTransaction.put(SqlConst.TRANSACTION_DATE, date);
                destinationTransaction.put(SqlConst.TRANSACTION_DESCRIPTION, description);
                destinationTransaction.put(SqlConst.TRANSACTION_TYPE, "User Transaction");
                destinationTransaction.put(SqlConst.TRANSACTION_IN, balance);
                destinationTransaction.put(SqlConst.TRANSACTION_OUT, 0.00);
                destinationTransaction.put(SqlConst.TRANSACTION_BALANCE, destinationAccount.getAccountBalance() + balance);
                destinationTransaction.put(SqlConst.TRANSACTION_ACCOUNT_ID_FOREIGN, destinationAccount.getAccountID());
                dba.insert(SqlConst.TRANSACTIONS_TABLE_NAME,null,destinationTransaction);

                //Update destination
                ContentValues destinationAccountContent = new ContentValues();
                destinationAccountContent.put(SqlConst.ACCOUNT_BALANCE, destinationAccount.getAccountBalance() + balance);
                destinationAccountContent.put(SqlConst.ACCOUNT_AVAILABLEBALANCE, destinationAccount.getAvailableBalance() + balance);
                dba.update(SqlConst.ACCOUNTS_TABLE_NAME,destinationAccountContent, SqlConst.ACCOUNT_ID +"="+destinationAccount.getAccountID(),null);


            }
        }

        dba.close();
    }

    public void addRecipient(int ownerId, String name, String sortCode, int accountNumber){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentRecipient = new ContentValues();
        contentRecipient.put(SqlConst.RECIPIENT_OWNER_ID,ownerId);
        contentRecipient.put(SqlConst.RECIPIENT_NAME, name);
        contentRecipient.put(SqlConst.RECIPIENT_ACCOUNTNUMBER,accountNumber);
        contentRecipient.put(SqlConst.RECIPIENT_SORTCODE,sortCode);
        db.insert(SqlConst.RECIPIENTS_TABLE_NAME,null,contentRecipient);
    }
    public ArrayList<Recipient> getRecipments(int ownerId){
        ArrayList<Recipient> temp = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columnsR = SqlConst.RECIPIENT_COLUMNS;
        String queryR = SqlConst.RECIPIENT_OWNER_ID + " = '" + ownerId + "'";

        Cursor t = db.query(SqlConst.RECIPIENTS_TABLE_NAME,columnsR,queryR,null,null,null,null);

        if (t != null) {
            if (t.getCount() > 0) {
                t.moveToFirst();

                for (int j = 0; j < t.getCount(); j++) {
                    int id = t.getInt(t.getColumnIndex(SqlConst.RECIPIENT_ID));
                    String name = t.getString(t.getColumnIndex(SqlConst.RECIPIENT_NAME));
                    String sortCode = t.getString(t.getColumnIndex(SqlConst.RECIPIENT_SORTCODE));
                    int accountNumber = t.getInt(t.getColumnIndex(SqlConst.RECIPIENT_ACCOUNTNUMBER));

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
        String[] columns = SqlConst.CUSTOMER_COLUMNS;
        String query = SqlConst.CUSTOMER_ID + " = '" + id + "'";
        Cursor cursor = db.query(SqlConst.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String firstName = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_NAME));
                String surname = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_SURNAME));
                String addressOne = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_ADDRESSONE));
                String addressTwo = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_ADDRESSTWO));
                String postcode = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_POSTCODE));
                String userId = cursor.getString(cursor.getColumnIndex(SqlConst.CUSTOMER_USERID));

                Customer cust = new Customer(id,firstName,surname,addressOne,addressTwo,postcode,userId);

                db.close();
                return cust;
            }
        }

        db.close();
        return null;
    }

    public int getId(String userID){
        userID.replace("'","\'");
        String[] columns = {SqlConst.CUSTOMER_ID};
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = SqlConst.CUSTOMER_USERID + " = '" + userID + "'";
        Cursor cursor = db.query(SqlConst.CUSTOMER_TABLE_NAME,columns,query,null,null,null,null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(SqlConst.CUSTOMER_ID));
                db.close();
                return id;
            }
        }
        db.close();
        return -1;
    }




}
