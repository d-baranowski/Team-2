package com.team.two.lloyds_app.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementScreenFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Statement";
    private View root;
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

    public static StatementScreenFragment newInstance() {
        StatementScreenFragment fragment = new StatementScreenFragment();
        return fragment;
    }

    public StatementScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Customer customer = ((MainActivity)getActivity()).getCustomer();
        Account account = ((MainActivity)getActivity()).getAccounts().get(0);

        root = inflater.inflate(R.layout.fragment_statement, container, false);

        accountName = (Spinner) root.findViewById(R.id.account_name);
        accountType = (TextView) root.findViewById(R.id.account_type);
        accountNumber = (TextView) root.findViewById(R.id.account_number);
        accountSortCode = (TextView) root.findViewById(R.id.account_sort_code);
        accountBalance = (TextView) root.findViewById(R.id.account_balance);
        accountAvailable = (TextView) root.findViewById(R.id.account_available);
        transactions = (ListView) root.findViewById(R.id.transactions_list);

        list = account.getTransactions();

        StatementListAdapter adapter = new StatementListAdapter(getActivity(),list);
        transactions.setAdapter(adapter);
        addItemsOnSpinner();

        return root;
    }


    public void addItemsOnSpinner(){
        ArrayList<Account> accounts = ((MainActivity)getActivity()).getAccounts();
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < accounts.size(); i++){
            list.add(accounts.get(i).getAccountName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.statement_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountName.setAdapter(dataAdapter);
        accountName.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void updateAccount(int position){
        account = ((MainActivity)getActivity()).getAccounts().get(position);
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

        StatementListAdapter adapter = new StatementListAdapter(getActivity(),list);
        transactions.setAdapter(adapter);
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
