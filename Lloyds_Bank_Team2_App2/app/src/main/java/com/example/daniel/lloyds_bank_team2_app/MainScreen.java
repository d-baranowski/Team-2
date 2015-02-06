package com.example.daniel.lloyds_bank_team2_app;


import android.app.Activity;
import android.os.Bundle;
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

        accountType.setText(String.valueOf(account.getAccountBalance()));
        accountNumber.setText(String.valueOf(customer.getId()));
        accountSortCode.setText(customer.getFirstName());
    }
}
