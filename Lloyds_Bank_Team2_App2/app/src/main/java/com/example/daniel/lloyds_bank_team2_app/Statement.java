package com.example.daniel.lloyds_bank_team2_app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class Statement extends Activity {
    private static DatabaseAdapter dbadapter;
    private Customer customer;
    private Account account;

    //List of Transactions
    private ArrayList<HashMap<String,String>> list;

    //UI references
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountName;
    private TextView accountBalance;
    private TextView accountAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        accountType = (TextView) findViewById(R.id.account_type);
        accountNumber = (TextView) findViewById(R.id.account_number);
        accountSortCode = (TextView) findViewById(R.id.account_sort_code);
        accountName = (TextView) findViewById(R.id.account_name);
        accountBalance = (TextView) findViewById(R.id.account_balance);
        accountAvailable = (TextView) findViewById(R.id.account_available);
        ListView transactions = (ListView) findViewById(R.id.transactions_list);

        dbadapter = new DatabaseAdapter(this);
        Customer customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        Account account = dbadapter.getAccounts(customer.getId()).get(0);

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());

        list = account.getTransactions();

        ListViewAdapter adapter = new ListViewAdapter(this,list);
        transactions.setAdapter(adapter);

    }


}
