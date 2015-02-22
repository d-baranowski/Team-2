package com.example.daniel.lloyds_bank_team2_app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Statement extends Activity {
    private static DatabaseAdapter dbadapter;
    private Customer customer;
    private Account account;

    //List of Transactions
    private ArrayList<HashMap<String,String>> list;

    //UI references
    private Spinner accountName;
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountBalance;
    private TextView accountAvailable;
    private ListView transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        accountName = (Spinner) findViewById(R.id.account_name);
        accountType = (TextView) findViewById(R.id.account_type);
        accountNumber = (TextView) findViewById(R.id.account_number);
        accountSortCode = (TextView) findViewById(R.id.account_sort_code);
        accountBalance = (TextView) findViewById(R.id.account_balance);
        accountAvailable = (TextView) findViewById(R.id.account_available);
        transactions = (ListView) findViewById(R.id.transactions_list);

        dbadapter = new DatabaseAdapter(this);
        Customer customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        Account account = dbadapter.getAccounts(customer.getId()).get(0);

        addItemsOnSpinner();
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());

        list = account.getTransactions();

        ListViewAdapter adapter = new ListViewAdapter(this,list);
        transactions.setAdapter(adapter);

    }

    public void updateAccount(int position){
        account = dbadapter.getAccounts(customer.getId()).get(position);
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());

        if (!account.getAccountType().equals("Subaccount")){
            accountNumber.setText(String.valueOf(account.getAccountNumber()));
            accountSortCode.setText(account.getSortCode());
        } else {
            accountNumber.setText("");
            accountSortCode.setText("");
        }


        list = account.getTransactions();

        ListViewAdapter adapter = new ListViewAdapter(this,list);
        transactions.setAdapter(adapter);
    }

    public void addItemsOnSpinner(){
        ArrayList<Account> accounts = dbadapter.getAccounts(customer.getId());
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < accounts.size(); i++){
            list.add(accounts.get(i).getAccountName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.statement_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountName.setAdapter(dataAdapter);
        accountName.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateAccount(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
