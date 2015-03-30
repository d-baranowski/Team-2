package com.team.two.lloyds_app.screens.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.team.two.lloyds_app.Exceptions.EmptyMandatoryFieldException;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionsScreenFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Transactions";
    private View root;

    //UI references transfers
    private Spinner transferFrom;
    private Spinner transferTo;
    private EditText transferAmountText;
    private Button transferButton;

    //UI references payments
    private Spinner paymentFrom;
    private Spinner paymentRecipient;
    private EditText paymentAmountText;
    private EditText descriptionText;
    private Button paymentButton;
    private Button addRecipmentButton;

    private Customer customer;
    private ArrayList<Account> accounts;
    private List<String> accountNames;
    private HashMap<String,Account> mapAccounts;
    private ArrayList<Recipient> recipients;
    private List<String>  recipientsNames;
    private HashMap<String,Recipient> mapRecipients;


    public static TransactionsScreenFragment newInstance() {
        TransactionsScreenFragment fragment = new TransactionsScreenFragment();
        return fragment;
    }

    public TransactionsScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customer =((MainActivity)getActivity()).getCustomer();
        accounts = ((MainActivity)getActivity()).getAccounts();

        root = inflater.inflate(R.layout.fragment_transfer, container, false);

        //Transfers
        mapAccounts();
        transferFrom = (Spinner) root.findViewById(R.id.spinner_transfer_from);
        transferTo = (Spinner)root.findViewById(R.id.spinner_transfer_recipient);
        transferAmountText = (EditText)root.findViewById(R.id.transfer_amount_text);
        transferButton = (Button) root.findViewById(R.id.button_transfer);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer();
            }
        });

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getListExcept(""));
        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, getListExcept(""));

        transferFrom.setAdapter(fromAdapter);
        transferFrom.setOnItemSelectedListener(new CustomOnItemSelectedListener(toAdapter));

        transferTo.setAdapter(toAdapter);
        transferTo.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        //Payments
        paymentFrom = (Spinner) root.findViewById(R.id.spinner_payment_from);
        paymentRecipient = (Spinner) root.findViewById(R.id.spinner_payment_recipient);
        paymentAmountText = (EditText) root.findViewById(R.id.payment_amount_text);
        descriptionText = (EditText) root.findViewById(R.id.description_text);
        paymentButton = (Button) root.findViewById(R.id.button_payment);
        addRecipmentButton = (Button) root.findViewById(R.id.button_new_recipient);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment();
            }
        });

        addRecipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipientDialog();
            }
        });

        recipients = ((MainActivity) getActivity()).getRecipients();
        mapRecipients();

        ArrayAdapter<String> paymentFromAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, accountNames);
        paymentFrom.setAdapter(paymentFromAdapter);
        paymentFrom.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<String> paymentRecipientAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,recipientsNames);
        paymentRecipient.setAdapter(paymentRecipientAdapter);
        paymentRecipient.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        return root;
    }

    public void transfer(){
        Account destination = mapAccounts.get((String)transferTo.getSelectedItem());
        Account source = mapAccounts.get((String)transferFrom.getSelectedItem());
        Double balance;

        try {
            ((MainActivity)getActivity()).checkEmptyField(transferAmountText, "Balance");
            balance = Double.parseDouble(transferAmountText.getText().toString());

            CharSequence result = "Hello toast!";

            if (balance > 0){
                if (balance <= source.getAvailableBalance()){
                    result = "Successful Transfer";
                    ((MainActivity)getActivity()).getAdapter().transfer(source,destination, balance);
                } else {
                    result = "Not enough available balance";
                }

            } else {
                result = "Balance can't be below 0";
            }

            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, result, duration);
            toast.show();

        } catch (NumberFormatException e){
            balance = 0.0;
            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Please enter a value you want to transfer", duration);
            toast.show();

        } catch (EmptyMandatoryFieldException e) {
            balance = 0.0;
            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Please enter a value you want to transfer", duration);
            toast.show();
        }


    }

    public void payment(){
        Account source = mapAccounts.get((String)paymentFrom.getSelectedItem());
        Recipient destination = mapRecipients.get((String)paymentRecipient.getSelectedItem());
        Double balance;

        try {
            balance = Double.parseDouble(paymentAmountText.getText().toString());
            ((MainActivity)getActivity()).checkEmptyField(descriptionText, "Description");
            String description = descriptionText.getText().toString();
            CharSequence result = "Hello toast!";

            if (balance > 0) {
                if (balance <= source.getAvailableBalance()) {
                    result = "Successful Payment";
                    ((MainActivity) getActivity()).getAdapter().payment(source, destination, balance, description);
                } else {
                    result = "Not enough available balance";
                }

            } else {
                result = "Balance can't be below 0";
            }

            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, result, duration);
            toast.show();

        } catch (NumberFormatException e){
            balance = 0.0;
            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Please enter a value you want to transfer", duration);
            toast.show();
        } catch (EmptyMandatoryFieldException e) {
            Context context = getActivity().getApplicationContext();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Make sure you filled all mandatory fields", duration);
            toast.show();
        }
    }

    public void addRecipientDialog(){
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.setContentView(R.layout.add_recipment_dialog);
        // Set dialog title
        dialog.setTitle("Add Recipient");

        //UI References
        final EditText name;
        final EditText accountNumber;
        final EditText sortCode;
        Button addRecipient;

        name = (EditText)dialog.findViewById(R.id.description_text);
        accountNumber = (EditText)dialog.findViewById(R.id.account_number_text);
        sortCode = (EditText)dialog.findViewById(R.id.sort_code_text);
        addRecipient = (Button)dialog.findViewById(R.id.add_recipient_button);

        addRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int accountNumberText;
                try {
                    ((MainActivity)getActivity()).checkEmptyField(name, "Name");
                    ((MainActivity)getActivity()).checkEmptyField(sortCode,"Sort Code");
                    String nameText = name.getText().toString();
                    String sortCodeText = sortCode.getText().toString();
                    accountNumberText = Integer.parseInt(accountNumber.getText().toString());
                    ((MainActivity)getActivity()).addRecipient(nameText,sortCodeText,accountNumberText);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), "Recipient Added", duration);
                    toast.show();
                    getParentFragment().onResume();
                    dialog.dismiss();
                } catch (NumberFormatException e){
                    accountNumberText = 0;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), "Please enter account number", duration);
                    toast.show();
                } catch (EmptyMandatoryFieldException e) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), "Please make sure you entered all mandatory data", duration);
                    toast.show();
                }

            }
        });

        addRecipient.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0add8e6, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        dialog.show();
    }

    private void mapAccounts(){
        accountNames = new ArrayList<>();
        mapAccounts = new HashMap<>();
        for (Account a: accounts){
            String name = a.getAccountName();
            accountNames.add(name);
            mapAccounts.put(a.getAccountName(),a);
        }
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

    private List<String> getListExcept(String except){
        List<String> list = new ArrayList<String>();
        for (Account a: accounts){
            if (!a.getAccountName().equals(except)){
                list.add(a.getAccountName());
            }
        }
        return list;
    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private ArrayAdapter<String> dependent;

        public CustomOnItemSelectedListener(ArrayAdapter<String> dependent){
            this.dependent = dependent;
        }

        public CustomOnItemSelectedListener(){
            dependent = null;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = (String)parent.getSelectedItem();
            if (dependent != null) {
                dependent.clear();
                dependent.addAll(getListExcept(selected));
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public void onResume() {
        accounts = ((MainActivity) getActivity()).getAccounts();
        recipients = ((MainActivity) getActivity()).getRecipients();
        mapAccounts();
        mapRecipients();

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, accountNames);
        paymentFrom.setAdapter(fromAdapter);
        paymentFrom.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<String> recipientAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,recipientsNames);
        paymentRecipient.setAdapter(recipientAdapter);
        paymentRecipient.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        super.onResume();
    }
}
