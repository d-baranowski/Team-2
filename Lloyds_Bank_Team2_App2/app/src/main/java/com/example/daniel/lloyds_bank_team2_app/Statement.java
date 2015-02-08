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

        //Populate for test

        list = new ArrayList<HashMap<String,String>>();
        for(int i = 0; i < 50; i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("Date", "1/1/1/1");
            temp.put("Description", "Hello");
            temp.put("Type", "World");
            temp.put("In", "1000");
            temp.put("Out", " ");
            temp.put("Balance", "99999");
            list.add(temp);
        }

        ListViewAdapter adapter = new ListViewAdapter(this,list);
        transactions.setAdapter(adapter);

    }


}
