package com.team.two.lloyds_app.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;

public class MainScreenFragment extends android.support.v4.app.Fragment {
    private static DatabaseAdapter dbadapter;

    //UI references
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountName;
    private TextView accountBalance;
    private TextView accountAvailable;

    Account account;


    public static MainScreenFragment newInstance(String param1, String param2) {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

    public MainScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountType = (TextView) getActivity().findViewById(R.id.account_type);
        accountNumber = (TextView) getActivity().findViewById(R.id.account_number);
        accountSortCode = (TextView) getActivity().findViewById(R.id.account_sort_code);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        accountBalance = (TextView) getActivity().findViewById(R.id.account_balance);
        accountAvailable = (TextView) getActivity().findViewById(R.id.account_available);

        dbadapter = new DatabaseAdapter(getActivity());
        Customer customer = dbadapter.getCustomer(getActivity().getIntent().getExtras().getInt("customerId"));
        account = dbadapter.getAccounts(customer.getId()).get(0);

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());
    }

    public void openStatement(View view){
        Intent i = new Intent(getActivity(), Statement.class);
        i.putExtra("customerId", getActivity().getIntent().getExtras().getInt("customerId"));
        startActivity(i);
    }

    public void openTransfers(View view){
        Intent i = new Intent(getActivity(), Payments.class);
        i.putExtra("customerId", getActivity().getIntent().getExtras().getInt("customerId"));
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }







}
