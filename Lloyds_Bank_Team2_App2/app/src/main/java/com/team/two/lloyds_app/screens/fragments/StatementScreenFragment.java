package com.team.two.lloyds_app.screens.fragments;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
    private TableLayout table;

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
        table = (TableLayout) root.findViewById(R.id.tableLayout1);

        list = account.getTransactions();
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

        list.clear();
        list = account.getTransactions();
        buildTable();
        table.setBackgroundResource(R.drawable.cell_shape);
    }

    public void buildTable(){
        table.removeAllViews();
        int rows = list.size();
        int cols = 6;
        String[] keys = {"Date","Description","TransactionType","Income","Outcome","TransactionBalance"};
        String[] labels = {"Date","Description","Type","Income","Outcome","Balance"};

        for (int i = 0; i < rows; i++){
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < cols; j++){
                HashMap<String, String> map = list.get(i);
                String test = map.get(keys[j]);
                TextView text = new TextView(getActivity());
                text.setTextSize(7);
                text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                text.setPadding(5,5,5,5);

                if(i == 0){
                    text.setBackgroundColor(getResources().getColor(R.color.lloyds_green));
                    text.setText(labels[j]);
                    text.setTextColor(Color.WHITE);
                } else{
                    text.setText(test);

                    if (i % 2 == 0){
                        text.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    } else {
                        text.setBackgroundColor(Color.WHITE);
                    }
                }
                row.addView(text);
            }

            table.addView(row);
        }
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
