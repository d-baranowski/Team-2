package com.example.daniel.lloyds_bank_team2_app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainScreen extends Activity {
    private static DatabaseAdapter dbadapter;

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
        setContentView(R.layout.activity_main_screen);

        accountType = (TextView) findViewById(R.id.account_type);
        accountNumber = (TextView) findViewById(R.id.account_number);
        accountSortCode = (TextView) findViewById(R.id.account_sort_code);
        accountName = (TextView) findViewById(R.id.account_name);
        accountBalance = (TextView) findViewById(R.id.account_balance);
        accountAvailable = (TextView) findViewById(R.id.account_available);

        dbadapter = new DatabaseAdapter(this);
        Customer customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        Account account = dbadapter.getAccounts(customer.getId()).get(0);

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
}
