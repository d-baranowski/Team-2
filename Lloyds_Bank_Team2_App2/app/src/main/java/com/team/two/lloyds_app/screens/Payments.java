package com.team.two.lloyds_app.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;
import com.team.two.lloyds_app.screens.activities.AddRecipient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//NOT USED ANYMORE

public class Payments extends Activity {
    //UI references
    private Spinner from;
    private Spinner recipient;
    private EditText amountText;
    private EditText descriptionText;

    private static DatabaseAdapter dbadapter;

    private Customer customer;

    private ArrayList<Account> accounts;
    private List<String> accountNames;
    private HashMap<String,Account> mapAccounts;

    private ArrayList<Recipient> recipients;
    private List<String>  recipientsNames;
    private HashMap<String,Recipient> mapRecipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payments);

        from = (Spinner) findViewById(R.id.spinner_transfer_from);
        recipient = (Spinner) findViewById(R.id.spinner_transfer_recipient);
        amountText = (EditText) findViewById(R.id.payment_amount_text);
        descriptionText = (EditText) findViewById(R.id.description_text);

        dbadapter = new DatabaseAdapter(this);
        customer = dbadapter.getCustomer(getIntent().getExtras().getInt("customerId"));
        accounts = dbadapter.getAccounts(customer.getId());
        recipients = dbadapter.getRecipments(customer.getId());
        mapAccounts();
        mapRecipients();

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, accountNames);
        from.setAdapter(fromAdapter);
        from.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<String> recipientAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,recipientsNames);
        recipient.setAdapter(recipientAdapter);
        recipient.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    protected void onResume(){
        super.onResume();

        accounts = dbadapter.getAccounts(customer.getId());
        recipients = dbadapter.getRecipments(customer.getId());
        mapAccounts();
        mapRecipients();

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, accountNames);
        from.setAdapter(fromAdapter);
        from.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<String> recipientAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,recipientsNames);
        recipient.setAdapter(recipientAdapter);
        recipient.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private void mapRecipients() {
        recipientsNames = new ArrayList<>();
        mapRecipients = new HashMap<>();
        for (Recipient r: recipients){
            String name = r.getName();
            recipientsNames.add(name);
            mapRecipients.put(name,r);
        }
    }

    private void mapAccounts(){
        accountNames = new ArrayList<>();
        mapAccounts = new HashMap<>();
        for (Account a: accounts){
            String name = a.getAccountName();
            accountNames.add(name);
            mapAccounts.put(name, a);
        }
    }

    public void payment(View view){
        Account source = mapAccounts.get((String)from.getSelectedItem());
        Recipient destination = mapRecipients.get((String)recipient.getSelectedItem());
        Double balance = Double.parseDouble(amountText.getText().toString());
        String description = descriptionText.getText().toString();
        CharSequence result = "Hello toast!";

        if (balance > 0){
            if (balance <= source.getAvailableBalance()){
                result = "Successful Payment";
                dbadapter.payment(source,destination, balance,description);
            } else {
                result = "Not enough available balance";
            }

        } else {
            result = "Balance can't be below 0";
        }

        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, result, duration);
        toast.show();
    }

    public void addRecipient(View view){
        Intent i = new Intent(this, AddRecipient.class);
        i.putExtra("ownerId", customer.getId());
        startActivity(i);
    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
