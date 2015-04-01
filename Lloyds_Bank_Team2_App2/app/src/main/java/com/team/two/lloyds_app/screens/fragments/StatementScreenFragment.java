package com.team.two.lloyds_app.screens.fragments;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import android.widget.TableRow.LayoutParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementScreenFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Statement";
    protected View root;
    private Customer customer;
    protected Account account;

    //List of Transactions
    private ArrayList<HashMap<String, String>> transactionsList;

    //UI references
    private Spinner accountName;
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountBalance;
    private TextView accountAvailable;
    private TableLayout table;

    public static StatementScreenFragment newInstance() {
        return new StatementScreenFragment();
    }

    public StatementScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        getActivity().setTitle("Statement");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customer = ((MainActivity) getActivity()).getCustomer();
        account = ((MainActivity) getActivity()).getAccounts().get(0);

        root = inflater.inflate(R.layout.fragment_statement, container, false);

        accountName = (Spinner) root.findViewById(R.id.account_name);
        accountType = (TextView) root.findViewById(R.id.account_type);
        accountNumber = (TextView) root.findViewById(R.id.account_number);
        accountSortCode = (TextView) root.findViewById(R.id.account_sort_code);
        accountBalance = (TextView) root.findViewById(R.id.account_balance);
        accountAvailable = (TextView) root.findViewById(R.id.account_available);
        table = (TableLayout) root.findViewById(R.id.tableLayout1);

        transactionsList = account.getTransactions();
        addItemsOnSpinner();

        return root;
    }


    public void addItemsOnSpinner() {
        ArrayList<Account> accounts = ((MainActivity) getActivity()).getAccounts();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < accounts.size(); i++) {
            list.add(accounts.get(i).getAccountName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.statement_spinner, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountName.setAdapter(dataAdapter);
        accountName.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void updateAccount(int position) {
        account = ((MainActivity) getActivity()).getAccounts().get(position);
        DecimalFormat df = new DecimalFormat("#.00");
        accountBalance.setText("£" + String.valueOf(df.format(account.getAccountBalance())));
        accountAvailable.setText("£" + String.valueOf(df.format(account.getAvailableBalance())));
        accountType.setText(account.getAccountType());

        if (!account.getAccountType().equals("Subaccount")) {
            accountNumber.setText(String.valueOf(account.getAccountNumber()));
            accountSortCode.setText(account.getSortCode());
        } else {
            accountNumber.setText("");
            accountSortCode.setText("");
        }

        transactionsList.clear();
        transactionsList = account.getTransactions();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            buildTableVertical();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buildTableHorizontal();
        }


        table.setBackgroundResource(R.drawable.cell_shape);
    }

    public void buildTableHorizontal() {
        table.removeAllViews();
        int rows = transactionsList.size();
        int cols = 6;
        String[] keys = {"Date", "Description", "TransactionType", "Income", "Outcome", "TransactionBalance"};
        String[] labels = {"Date", "Description", "Type", "Income", "Outcome", "Balance"};

        for (int i = 0; i < rows + 1; i++) {
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < cols; j++) {

                TextView textBox = new TextView(getActivity());
                textBox.setTextSize(15);
                textBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                textBox.setPadding(5, 5, 5, 5);

                if (i == 0) {
                    textBox.setBackgroundColor(getResources().getColor(R.color.lloyds_green));
                    textBox.setText(labels[j]);
                    textBox.setTextColor(Color.WHITE);
                } else {
                    HashMap<String, String> transactionRow = transactionsList.get(i - 1);
                    String cellData = transactionRow.get(keys[j]);
                    textBox.setText(cellData);

                    if (i % 2 == 0) {
                        textBox.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    } else {
                        textBox.setBackgroundColor(Color.WHITE);
                    }
                }

                row.addView(textBox);
            }

            table.addView(row);
        }
    }

    public void buildTableVertical() {
        table.removeAllViews();
        int rows = transactionsList.size();
        int cols = 4;
        String[] keys = {"Date", "Description", "Income", "TransactionBalance"};
        String[] labels = {"Date", "Description", "Cash Flow", "Balance"};

        for (int i = 0; i < rows + 1; i++) {
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < cols; j++) {
                TextView textBox = new TextView(getActivity());
                textBox.setTextSize(12);
                textBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                textBox.setPadding(5, 5, 5, 5);

                if (i == 0) {
                    textBox.setBackgroundColor(getResources().getColor(R.color.lloyds_green));
                    textBox.setText(labels[j]);
                    textBox.setTextColor(Color.WHITE);
                } else {
                    HashMap<String, String> transactionRow = transactionsList.get(i - 1);
                    String cellData = transactionRow.get(keys[j]);
                    if (j == 2) {
                        double income = Double.parseDouble(transactionRow.get(keys[2]));
                        double outcome = Double.parseDouble(transactionRow.get("Outcome"));

                        if (income > 0) {
                            cellData = income + "";
                        } else if (outcome > 0) {
                            cellData = "- " + outcome;
                        }
                    }
                    textBox.setText(cellData);

                    if (i % 2 == 0) {
                        textBox.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    } else {
                        textBox.setBackgroundColor(Color.WHITE);
                    }
                }

                row.addView(textBox);
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
