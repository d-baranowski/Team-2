package com.team.two.lloyds_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Achievement;
import com.team.two.lloyds_app.objects.Address;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Offer;
import com.team.two.lloyds_app.objects.Recipient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Author: Daniel B
 * Date: 01/02/2015
 * Purpose: to pull data from the database and convert into a format that the app can read
 */

public class DatabaseAdapter{

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	private final int MAX_TRACKBACK_DAYS = 50;
	private final double FALLBACK_BALANCE = 0;
	private final DbHelp helper;

	public DatabaseAdapter(Context context){
		helper = new DbHelp(context);
	}

	/**
	 * Return true if password stored in database is same as the one passed as parameter
	 * <p/>
	 * This method will access the dummy database table Customers that stores customer details.
	 * It well then query the database to find a customer with given userID is in the database
	 * If the query is successful cursor object won't be null and its size (count) will be larger than 1
	 * Then the cursor will check if passed matches the password stored in database
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public boolean login(String userID, String password){
		//Apostrophe is part of SQLite syntax. These lines prevent app from crashing if user entered values contain apostrophes
		userID.replace("'", "\'");
		password.replace("'", "\'");

		SQLiteDatabase db = helper.getReadableDatabase();

		String[] columns = {SqlCons.CUSTOMER_PASSWORD};
		String query = SqlCons.CUSTOMER_USERID + " = '" + userID + "'";
		Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME, columns, query, null, null, null, null);

		if(cursor != null){
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				String pass = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_PASSWORD));
				if(password.equals(pass)){
					db.close();
					return true;
				}
			}
		}
		db.close();
		return false;
	}

	//This method returns an ArrayList of accounts unique to the ownerID specified.
	public ArrayList<Account> getAccounts(int ownerId){
		//It starts by querying the database
		ArrayList<Account> accounts = new ArrayList<>();
		String[] columns = SqlCons.ACCOUNT_COLUMNS;
		SQLiteDatabase db = helper.getReadableDatabase();
		String query = SqlCons.ACCOUNT_OWNERID + " = '" + ownerId + "'";
		Cursor c = db.query(SqlCons.ACCOUNTS_TABLE_NAME, columns, query, null, null, null, null);
		if(c != null){
			if(c.getCount() > 0){
				c.moveToFirst();
				//Then loops through and pulls the data from the database
				for(int i = 0; i < c.getCount(); i++){
					int id = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_ID));
					String accountType = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_TYPE));
					double accountBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_BALANCE));
					double availableBalance = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_AVAILABLEBALANCE));
					String accountName = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_NAME));
					/*If the account in question is a sub account then it pulls the data specific to that,
					as a sub account is treated differently*/
					if(!accountType.equals("Subaccount")){
						int accountNumber = c.getInt(c.getColumnIndex(SqlCons.ACCOUNT_NUMBER));
						String sortCode = c.getString(c.getColumnIndex(SqlCons.ACCOUNT_SORTCODE));
						double overdraft = c.getDouble(c.getColumnIndex(SqlCons.ACCOUNT_OVERDRAFTLIMIT));
						//Then a new account object is created and added to the list
						accounts.add(new Account(id, accountNumber, sortCode, accountName, accountType, accountBalance, availableBalance, overdraft, ownerId, getTransactions(id)));
					}else{
						accounts.add(new Account(id, accountName, accountType, accountBalance, availableBalance, ownerId, getTransactions(id)));
					}

					c.moveToNext();
				}
				db.close();
			}
		}
		db.close();
		return accounts;
	}

	/*Similarly to the getAccounts method, this method pulls the transactions information but instead
	puts it in a hashmap*/
	public ArrayList<HashMap<String, String>> getTransactions(int id){
		//The map is created and the database queried
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columnsT = SqlCons.TRANSACTION_COLUMNS;
		String queryT = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + id + "'";
		String order = SqlCons.TRANSACTION_DATE + " DESC, " + SqlCons.TRANSACTION_TIME + " DESC";

		Cursor t = db.query(SqlCons.TRANSACTIONS_TABLE_NAME, columnsT, queryT, null, null, null, order);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();
				//then the cursor loops through and fills the map with the relevant information
				for(int j = 0; j < t.getCount(); j++){
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

	//The method for transferring money
	public void transfer(Account source, Account destination, Double balance){
		SQLiteDatabase db = helper.getWritableDatabase();

		long time = System.currentTimeMillis() / 1000L;
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = format1.format(date1);

		String descSource = "To " + destination.getAccountName();
		String descDestination = "From " + source.getAccountName();

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
		db.insert(SqlCons.TRANSACTIONS_TABLE_NAME, null, sourceTransaction);

		//Update Source
		ContentValues sourceAccount = new ContentValues();
		sourceAccount.put(SqlCons.ACCOUNT_BALANCE, source.getAccountBalance() - balance);
		sourceAccount.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, source.getAvailableBalance() - balance);
		db.update(SqlCons.ACCOUNTS_TABLE_NAME, sourceAccount, SqlCons.ACCOUNT_ID + "=" + source.getAccountID(), null);

		//Destination transaction
		ContentValues destinationTransaction = new ContentValues();
		destinationTransaction.put(SqlCons.TRANSACTION_DATE, date);
		destinationTransaction.put(SqlCons.TRANSACTION_TIME, time);
		destinationTransaction.put(SqlCons.TRANSACTION_DESCRIPTION, descDestination);
		destinationTransaction.put(SqlCons.TRANSACTION_TYPE, "User Transaction");
		destinationTransaction.put(SqlCons.TRANSACTION_IN, balance);
		destinationTransaction.put(SqlCons.TRANSACTION_OUT, 0.00);
		destinationTransaction.put(SqlCons.TRANSACTION_BALANCE, destination.getAccountBalance() + balance);
		destinationTransaction.put(SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN, destination.getAccountID());
		db.insert(SqlCons.TRANSACTIONS_TABLE_NAME, null, destinationTransaction);

		//Update destination
		ContentValues destinationAccount = new ContentValues();
		destinationAccount.put(SqlCons.ACCOUNT_BALANCE, destination.getAccountBalance() + balance);
		destinationAccount.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, destination.getAvailableBalance() + balance);
		db.update(SqlCons.ACCOUNTS_TABLE_NAME, destinationAccount, SqlCons.ACCOUNT_ID + "=" + destination.getAccountID(), null);

		db.close();
	}

	public void payment(Account source, Recipient destination, Double balance, String description){
		SQLiteDatabase dba = helper.getWritableDatabase();
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = format1.format(date1);

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

		Cursor c = dba.query(SqlCons.ACCOUNTS_TABLE_NAME, columnsA, queryA, null, null, null, null);

		if(c != null){
			if(c.getCount() > 0){
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
				destinationAccount = new Account(id, accountNumber, sortCode, accountName, accountType, accountBalance, availableBalance, overdraft, id, getTransactions(id));

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
				dba.insert(SqlCons.TRANSACTIONS_TABLE_NAME, null, destinationTransaction);

				//Update destination
				ContentValues destinationAccountContent = new ContentValues();
				destinationAccountContent.put(SqlCons.ACCOUNT_BALANCE, destinationAccount.getAccountBalance() + balance);
				destinationAccountContent.put(SqlCons.ACCOUNT_AVAILABLEBALANCE, destinationAccount.getAvailableBalance() + balance);
				dba.update(SqlCons.ACCOUNTS_TABLE_NAME, destinationAccountContent, SqlCons.ACCOUNT_ID + "=" + destinationAccount.getAccountID(), null);


			}
		}

		dba.close();
	}

	//The method to add a person to the list of trusted recipients
	public void addRecipient(int ownerId, String name, String sortCode, int accountNumber){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentRecipient = new ContentValues();
		contentRecipient.put(SqlCons.RECIPIENT_OWNER_ID, ownerId);
		contentRecipient.put(SqlCons.RECIPIENT_NAME, name);
		contentRecipient.put(SqlCons.RECIPIENT_ACCOUNTNUMBER, accountNumber);
		contentRecipient.put(SqlCons.RECIPIENT_SORTCODE, sortCode);
		db.insert(SqlCons.RECIPIENTS_TABLE_NAME, null, contentRecipient);

		db.close();
	}

	/*Pulls the current trusted recipients from the database in the same way as the getAccounts and
	getTransactions methods*/
	public ArrayList<Recipient> getRecipients(int ownerId){
		ArrayList<Recipient> temp = new ArrayList<>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columnsR = SqlCons.RECIPIENT_COLUMNS;
		String queryR = SqlCons.RECIPIENT_OWNER_ID + " = '" + ownerId + "'";

		Cursor t = db.query(SqlCons.RECIPIENTS_TABLE_NAME, columnsR, queryR, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				for(int j = 0; j < t.getCount(); j++){
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

	//Similar to the other get methods, however this one puts the pulled info into a Customer object
	public Customer getCustomer(int id){
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = SqlCons.CUSTOMER_COLUMNS;
		String query = SqlCons.CUSTOMER_ID + " = '" + id + "'";
		Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME, columns, query, null, null, null, null);

		if(cursor != null){
			if(cursor.getCount() > 0){
				cursor.moveToFirst();

				String firstName = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_NAME));
				String surname = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_SURNAME));
				Address address = new Address(cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_STREET_ADDRESS)), cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_CITY)), cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_POSTCODE)));
				String userId = cursor.getString(cursor.getColumnIndex(SqlCons.CUSTOMER_USERID));
				int points = cursor.getInt(cursor.getColumnIndex(SqlCons.CUSTOMER_OFFERS_POINTS));

				Customer customer = new Customer(id, firstName, surname, address, userId, points);

				db.close();
				return customer;
			}
		}

		db.close();
		return null;
	}

	//Works the same as the other getters.
	public ArrayList<Offer> getAvailableOffers(){
		ArrayList<Offer> list = new ArrayList<>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = SqlCons.AVAILABLE_OFFERS_COLUMNS;
		String query = SqlCons.AVAIL_OFFER_ACTIVE + " = '" + 0 + "'";

		Cursor t = db.query(SqlCons.AVAIL_OFFERS_TABLE_NAME, columns, query, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				for(int i = 0; i < t.getCount(); i++){
					int id = t.getInt(t.getColumnIndex(SqlCons.AVAIL_OFFER_ID));
					int icon = t.getInt(t.getColumnIndex(SqlCons.AVAIL_OFFER_ICON));
					String name = t.getString(t.getColumnIndex(SqlCons.AVAIL_OFFER_NAME));
					String desc = t.getString(t.getColumnIndex(SqlCons.AVAIL_OFFER_DESCRIPTION));
					int price = t.getInt(t.getColumnIndex(SqlCons.AVAIL_OFFER_PRICE));

					list.add(new Offer(id, icon, icon, name, desc, price, false));

					t.moveToNext();
				}
				db.close();
			}
		}

		return list;
	}

	public ArrayList<Offer> getActiveOffers(int ownerId){
		ArrayList<Offer> list = new ArrayList<>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = SqlCons.ACTIVE_OFFERS_COLUMN_NAMES;

		Cursor t = db.query(SqlCons.ACTIVE_OFFERS_TABLE_NAME, columns, null, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				for(int i = 0; i < t.getCount(); i++){
					int id = t.getInt(t.getColumnIndex(SqlCons.ACTIVE_OFFER_ID));
					int icon = t.getInt(t.getColumnIndex(SqlCons.ACTIVE_OFFER_ICON));
					int barcode = R.drawable.barcode;
					String name = t.getString(t.getColumnIndex(SqlCons.ACTIVE_OFFER_NAME));
					String desc = t.getString(t.getColumnIndex(SqlCons.ACTIVE_OFFER_DESCRIPTION));

					list.add(new Offer(id, icon, barcode, name, desc, 0, true));
					t.moveToNext();
				}
				db.close();
			}
		}

		return list;
	}

	//Method to activate an offer that a user qualifies for.
	public void activateOffer(Offer offer, int ownerId){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = SqlCons.AVAILABLE_OFFERS_COLUMNS;
		String query = SqlCons.AVAIL_OFFER_ID + " = '" + offer.getId() + "'";
		Cursor t = db.query(SqlCons.AVAIL_OFFERS_TABLE_NAME, columns, query, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				ContentValues offerValues = new ContentValues();
				offerValues.put(SqlCons.AVAIL_OFFER_ACTIVE, 1);
				db.update(SqlCons.AVAIL_OFFERS_TABLE_NAME, offerValues, query, null);

				ContentValues activeOfferValues = new ContentValues();
				activeOfferValues.put(SqlCons.ACTIVE_OFFER_ICON, offer.getIcon());
				activeOfferValues.put(SqlCons.ACTIVE_OFFER_BARCODE, offer.getBarcode());
				activeOfferValues.put(SqlCons.ACCOUNT_NAME, offer.getName());
				activeOfferValues.put(SqlCons.ACTIVE_OFFER_DESCRIPTION, offer.getDescription());
				activeOfferValues.put(SqlCons.ACTIVE_OFFER_OWNER_ID, ownerId);
				db.insert(SqlCons.ACTIVE_OFFERS_TABLE_NAME, null, activeOfferValues);
			}
		}
		db.close();
	}

	public void spendOfferPoints(int customerId, int points){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = SqlCons.CUSTOMER_COLUMNS;
		String query = SqlCons.CUSTOMER_ID + " = '" + customerId + "'";

		Cursor t = db.query(SqlCons.CUSTOMER_TABLE_NAME, columns, query, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				int balance = t.getInt(t.getColumnIndex(SqlCons.CUSTOMER_OFFERS_POINTS));

				ContentValues customerValues = new ContentValues();
				customerValues.put(SqlCons.CUSTOMER_OFFERS_POINTS, balance - points);
				db.update(SqlCons.CUSTOMER_TABLE_NAME, customerValues, query, null);
			}
		}

		db.close();
	}

	public void gainOfferPoints(int customerId, int points){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = SqlCons.CUSTOMER_COLUMNS;
		String query = SqlCons.CUSTOMER_ID + " = '" + customerId + "'";

		Cursor t = db.query(SqlCons.CUSTOMER_TABLE_NAME, columns, query, null, null, null, null);

		if(t != null){
			if(t.getCount() > 0){
				t.moveToFirst();

				int balance = t.getInt(t.getColumnIndex(SqlCons.CUSTOMER_OFFERS_POINTS));

				ContentValues customerValues = new ContentValues();
				customerValues.put(SqlCons.CUSTOMER_OFFERS_POINTS, balance + points);
				db.update(SqlCons.CUSTOMER_TABLE_NAME, customerValues, query, null);
			}
		}

		db.close();
	}


	@SuppressWarnings("ResultOfMethodCallIgnored")
	public int getId(String userID){
		userID.replace("'", "\'");
		String[] columns = {SqlCons.CUSTOMER_ID};
		SQLiteDatabase db = helper.getReadableDatabase();
		String query = SqlCons.CUSTOMER_USERID + " = '" + userID + "'";
		Cursor cursor = db.query(SqlCons.CUSTOMER_TABLE_NAME, columns, query, null, null, null, null);

		if(cursor != null){
			if(cursor.getCount() > 0){
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

		for(int i = 0; i < MAX_TRACKBACK_DAYS; i++){

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

		for(int i = 0; i < numDays; i++){

			query = SqlCons.TRANSACTION_ACCOUNT_ID_FOREIGN + " = '" + customerID + "' AND " + SqlCons.TRANSACTION_DATE + " = '" + df.format(date.getTime()) + "'";
			Cursor cursor = db.query(SqlCons.TRANSACTIONS_TABLE_NAME, SqlCons.TRANSACTION_COLUMNS, query, null, null, null, null);
			double amount = 0;

			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				boolean rowsRemain = true;
				while(rowsRemain){
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

	public List<Achievement> getAchievements(){
		List<Achievement> achievements = new ArrayList<Achievement>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = SqlCons.ACHIEVEMENTS_COLUMNS;
		Cursor cursor = db.query(SqlCons.ACHIEVEMENTS_TABLE_NAME, columns, null, null, null, null, null);

		if(cursor != null){
			if(cursor.getCount() > 0){
				cursor.moveToFirst();

				for(int j = 0; j < cursor.getCount(); j++){
					int achievementId = cursor.getInt(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_ID));
					String achievementTitle = cursor.getString(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_TITLE));
					String achievementDescription = cursor.getString(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_DESCRIPTION));
					int achievementPoints = cursor.getInt(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_POINTS));
					int achievementIconId = cursor.getInt(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_ICON_ID));
					int achievementIsIncremental = cursor.getInt(cursor.getColumnIndex(SqlCons.ACHIEVEMENT_IS_INCREMENTAL));

					achievements.add(new Achievement(achievementId, achievementTitle, achievementDescription, achievementPoints, achievementIconId, achievementIsIncremental));
					cursor.moveToNext();
				}
			}
		}

		db.close();
		return achievements;
	}

    public boolean achievementAccomplished(int achievementId, int customerId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = SqlCons.CUSTOMER_ACHIEVEMENTS_COLUMNS;
        String query = SqlCons.CUSTOMER_ACHIEVEMENT_ID + " = '" + achievementId + "' AND " + SqlCons.CUSTOMER_ACHIEVEMENT_CUSTOMER_ID + " = " + customerId;
        Cursor cursor = db.query(SqlCons.CUSTOMER_ACHIEVEMENTS_TABLE_NAME,columns,query,null,null,null,null);

        int count = cursor.getCount();

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    public void addCompletedAchievement(int achievementId, int customerId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = SqlCons.CUSTOMER_ACHIEVEMENTS_COLUMNS;

        ContentValues newAchievementValues = new ContentValues();
        newAchievementValues.put(SqlCons.CUSTOMER_ACHIEVEMENT_ID, achievementId);
        newAchievementValues.put(SqlCons.CUSTOMER_ACHIEVEMENT_CUSTOMER_ID, customerId);
        db.insert(SqlCons.CUSTOMER_ACHIEVEMENTS_TABLE_NAME, null, newAchievementValues);
    }

    public void updateTransactionsTotal(int customerId, double updateAmount)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = SqlCons.CUSTOMER_STATISTICS_COLUMNS;
        String query = SqlCons.CUSTOMER_STATISTICS_CUSTOMER_ID + " = " + customerId + "";

        Cursor cursor = db.query(SqlCons.CUSTOMER_STATISTICS_TABLE_NAME,columns,query,null,null,null,null);
        int count = cursor.getCount();

        if(count == 1)
        {
            cursor.moveToFirst();

            double previousTotal = cursor.getDouble(cursor.getColumnIndex(SqlCons.CUSTOMER_TOTAL_TRANSACTIONS));
            double newTotal = updateAmount+previousTotal;

            ContentValues transactionValues = new ContentValues();
            transactionValues.put(SqlCons.CUSTOMER_TOTAL_TRANSACTIONS, newTotal);
            db.update(SqlCons.CUSTOMER_STATISTICS_TABLE_NAME, transactionValues, query, null);
        }
    }

    public double getTransactionsTotal(int customerId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = SqlCons.CUSTOMER_STATISTICS_COLUMNS;
        String query = SqlCons.CUSTOMER_STATISTICS_CUSTOMER_ID + " = '" + customerId + "'";

        Cursor cursor = db.query(SqlCons.CUSTOMER_STATISTICS_TABLE_NAME, columns, query, null, null, null, null);
        cursor.moveToFirst();

        return cursor.getDouble(cursor.getColumnIndex(SqlCons.CUSTOMER_TOTAL_TRANSACTIONS));
    }

    public void incrementLogin(int customerId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = SqlCons.CUSTOMER_ACHIEVEMENTS_COLUMNS;
        String query = SqlCons.CUSTOMER_STATISTICS_CUSTOMER_ID + " = " + customerId + "";

        Cursor cursor = db.query(SqlCons.CUSTOMER_STATISTICS_TABLE_NAME,columns,query,null,null,null,null);
        int count = cursor.getCount();

        if(count == 1)
        {

        }

    }

    public int getLoginsTotal(int customerId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = SqlCons.CUSTOMER_STATISTICS_COLUMNS;
        String query = SqlCons.CUSTOMER_STATISTICS_CUSTOMER_ID + " = '" + customerId + "'";

        Cursor cursor = db.query(SqlCons.CUSTOMER_STATISTICS_TABLE_NAME, columns, query, null, null, null, null);
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex(SqlCons.CUSTOMER_LOGINS));
    }
}
