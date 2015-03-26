package com.team.two.lloyds_app.screens.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;
import com.team.two.lloyds_app.screens.drawer.NavigationDrawerFragment;
import com.team.two.lloyds_app.screens.fragments.MainScreenFragment;
import com.team.two.lloyds_app.screens.fragments.StatementScreenFragment;
import com.team.two.lloyds_app.screens.fragments.TransactionsScreenFragment;
import com.team.two.lloyds_app.screens.fragments.MoneyPlannerFragment;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity  {
    private static DatabaseAdapter dbadapter;
    private Account account;
    private Toolbar toolbar;
    private int customerId;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbadapter = new DatabaseAdapter(this);
        customerId = getIntent().getExtras().getInt("customerId");
        fm = getSupportFragmentManager();

        setContentView(R.layout.activity_main_appbar);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openHome();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout),toolbar);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.logo_horsey){
            openHome();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openHome(){
        Fragment bf = new MainScreenFragment();
        toolbar.setTitle(MainScreenFragment.TITLE);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openStatement(){
        Fragment bf = new StatementScreenFragment();
        toolbar.setTitle(StatementScreenFragment.TITLE);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openTransfers(){
        Fragment bf = new TransactionsScreenFragment();
        toolbar.setTitle(TransactionsScreenFragment.TITLE);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openPlanner(){
        Fragment bf = new MoneyPlannerFragment();
        toolbar.setTitle(MoneyPlannerFragment.TITLE);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public Customer getCustomer(){
        return dbadapter.getCustomer(customerId);
    }

    public ArrayList<Account> getAccounts(){
        return dbadapter.getAccounts(customerId);
    }

    public ArrayList<Recipient> getRecipments() {
        return dbadapter.getRecipments(customerId);
    }

    public DatabaseAdapter getAdapter(){
        return dbadapter;
    }



}
