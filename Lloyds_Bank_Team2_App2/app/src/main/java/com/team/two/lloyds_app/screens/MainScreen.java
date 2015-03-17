package com.team.two.lloyds_app.screens;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;


public class MainScreen extends Activity {
    private static DatabaseAdapter dbadapter;

    //UI references
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountName;
    private TextView accountBalance;
    private TextView accountAvailable;

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        accountType = (TextView) findViewById(R.id.account_type);
        accountNumber = (TextView) findViewById(R.id.account_number);
        accountSortCode = (TextView) findViewById(R.id.account_sort_code);
        accountName = (TextView) findViewById(R.id.account_name);
        accountBalance = (TextView) findViewById(R.id.account_balance);
        accountAvailable = (TextView) findViewById(R.id.account_available);

        dbadapter = new DatabaseAdapter(this);
        Customer customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        account = dbadapter.getAccounts(customer.getId()).get(0);

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        dbadapter = new DatabaseAdapter(this);
        Customer customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        account = dbadapter.getAccounts(customer.getId()).get(0);

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());
    }

    public void openStatement(View view){
        Intent i = new Intent(this, Statement.class);
        i.putExtra("customerId", getIntent().getExtras().getInt("customerId"));
        startActivity(i);
    }

    public void openTransfers(View view){
        Intent i = new Intent(this, Payments.class);
        i.putExtra("customerId", getIntent().getExtras().getInt("customerId"));
        startActivity(i);
    }
}
