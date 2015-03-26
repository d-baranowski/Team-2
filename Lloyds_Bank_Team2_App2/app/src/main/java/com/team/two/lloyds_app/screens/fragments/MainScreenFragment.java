package com.team.two.lloyds_app.screens.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

public class MainScreenFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Home";
    private View root;

    //UI references
    private TextView accountType;
    private TextView accountNumber;
    private TextView accountSortCode;
    private TextView accountName;
    private TextView accountBalance;
    private TextView accountAvailable;

    private Button openStatement;
    private Button openTransfers;
    private Button openAchievements;
    private Button openFinder;
    private Button openPlanner;
    private Button openOffers;

    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

    public MainScreenFragment() {
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

        root = inflater.inflate(R.layout.fragment_main_screen, container, false);
        accountType = (TextView) root.findViewById(R.id.account_type);
        accountNumber = (TextView) root.findViewById(R.id.account_number);
        accountSortCode = (TextView) root.findViewById(R.id.account_sort_code);
        accountName = (TextView) root.findViewById(R.id.account_name);
        accountBalance = (TextView) root.findViewById(R.id.account_balance);
        accountAvailable = (TextView) root.findViewById(R.id.account_available);

        openStatement = (Button) root.findViewById(R.id.statement_button);
        openTransfers = (Button) root.findViewById(R.id.transfers_button);
        openAchievements = (Button) root.findViewById(R.id.achievements_button);
        openFinder = (Button) root.findViewById(R.id.finder_button);
        openPlanner = (Button) root.findViewById(R.id.planner_button);
        openOffers = (Button) root.findViewById(R.id.deals_button);

        openStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openStatement();
            }
        });

        openStatement.setOnTouchListener(new View.OnTouchListener() {

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

        openTransfers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openTransfers();
            }
        });

        openTransfers.setOnTouchListener(new View.OnTouchListener() {

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

        openPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openPlanner();
            }
        });

        openPlanner.setOnTouchListener(new View.OnTouchListener() {

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

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());

        return root;
    }

    @Override
    public void onResume() {
        Customer customer = ((MainActivity)getActivity()).getCustomer();
        Account account = ((MainActivity)getActivity()).getAccounts().get(0);

        accountName.setText(account.getAccountName());
        accountBalance.setText("£"+String.valueOf(account.getAccountBalance()));
        accountAvailable.setText("£"+String.valueOf(account.getAvailableBalance()));
        accountType.setText(account.getAccountType());
        accountNumber.setText(String.valueOf(account.getAccountNumber()));
        accountSortCode.setText(account.getSortCode());

        super.onResume();
    }

}
