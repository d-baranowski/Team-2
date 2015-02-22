package com.example.daniel.lloyds_bank_team2_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Transfer extends Activity {
    //UI references
    private Spinner from;
    private Spinner to;
    private EditText amountText;

    private static DatabaseAdapter dbadapter;
    private Customer customer;
    ArrayList<Account> accounts;
    private HashMap<String,Account> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        dbadapter = new DatabaseAdapter(this);
        customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        accounts = dbadapter.getAccounts(customer.getId());
        mapAcconts();

        from = (Spinner)findViewById(R.id.spinner_from);
        to = (Spinner)findViewById(R.id.spinner_to);
        amountText = (EditText)findViewById(R.id.amount_text);

        addItemsOnSpinners(getListExcept(""));

    }

    public void transfer(Account to, Account from, double ammount){

    }

    private void mapAcconts(){
        map = new HashMap<>();
        for (Account a: accounts){
            map.put(a.getAccountName(),a);
        }
    }

    private List<String> getListExcept(String except){
        List<String> list = new ArrayList<String>();
        for (Account a: accounts){
            if (!a.getAccountName().equals(except)){
                list.add(a.getAccountName());
            }
        }
        return list;
    }

    private void addItemsOnSpinners(List<String> list){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(dataAdapter);
        from.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        to.setAdapter(dataAdapter);
        to.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String accountName = (String)parent.getSelectedItem();
            addItemsOnSpinners(getListExcept(accountName));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}


