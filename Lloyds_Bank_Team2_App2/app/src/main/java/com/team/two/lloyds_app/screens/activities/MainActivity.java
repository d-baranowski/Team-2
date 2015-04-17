package com.team.two.lloyds_app.screens.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.team.two.lloyds_app.exceptions.EmptyMandatoryFieldException;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Achievement;
import com.team.two.lloyds_app.objects.Offer;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;
import com.team.two.lloyds_app.screens.drawer.NavigationDrawerFragment;
import com.team.two.lloyds_app.screens.fragments.BranchFinderFragment;
import com.team.two.lloyds_app.screens.fragments.HelpScreenFragment;
import com.team.two.lloyds_app.screens.fragments.MainScreenFragment;
import com.team.two.lloyds_app.screens.fragments.OffersFragment;
import com.team.two.lloyds_app.screens.fragments.OptionsFragment;
import com.team.two.lloyds_app.screens.fragments.StatementScreenFragment;
import com.team.two.lloyds_app.screens.fragments.TransactionsScreenFragment;
import com.team.two.lloyds_app.screens.fragments.MoneyPlannerFragment;
import com.team.two.lloyds_app.screens.fragments.AchievementsFragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static DatabaseAdapter dbadapter;
    private String title;
    private Account account;
    private Toolbar toolbar;
    private int customerId;
    private FragmentManager fm;

    //Client used to interact with Google APIs
    public GoogleApiClient mGoogleApiClient;

    //Variable to store latest location
    public Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Build Google API client and connect
        buildGoogleAPIClient();
        mGoogleApiClient.connect();

        dbadapter = new DatabaseAdapter(this);
        customerId = getIntent().getExtras().getInt("customerId");
        fm = getSupportFragmentManager();

        setContentView(R.layout.activity_main_appbar);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        // Increment login
        //incrementLogin(customerId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            openHome();
        } else {
        }

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


        if (id == R.id.logo_horsey){
            openHome();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openHome(){
        Fragment bf = new MainScreenFragment();
        title = MainScreenFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openStatement(){
        Fragment bf = new StatementScreenFragment();
        title = StatementScreenFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openTransfers(){
        Fragment bf = new TransactionsScreenFragment();
        title = TransactionsScreenFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openAchievements(){
        Fragment bf = new AchievementsFragment();
        title = AchievementsFragment.TITLE;
        toolbar.setTitle(title);

        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openPlanner(){
        Fragment bf = new MoneyPlannerFragment();
        title = MoneyPlannerFragment.TITLE;
        toolbar.setTitle(MoneyPlannerFragment.TITLE);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openFinder(){
        Fragment bf = new BranchFinderFragment();
        title = BranchFinderFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openOptions(){
        Fragment bf = new OptionsFragment();
        title = OptionsFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openHelp(){
        Fragment bf = new HelpScreenFragment();
        title = HelpScreenFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder, bf).commit();
    }

    public void openOffers(){
        Fragment bf = new OffersFragment();
        title = OffersFragment.TITLE;
        toolbar.setTitle(title);
        fm.beginTransaction().replace(R.id.mainFragmentHolder,bf).commit();
    }

    public void logOut(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public Customer getCustomer(){
        return dbadapter.getCustomer(customerId);
    }

    public ArrayList<Account> getAccounts(){
        return dbadapter.getAccounts(customerId);
    }

    public ArrayList<Recipient> getRecipients() {
        return dbadapter.getRecipients(customerId);
    }

    public ArrayList<Offer> getAvailableOffers(){
        return dbadapter.getAvailableOffers();
    }

    public List<Achievement> getAchievements() { return dbadapter.getAchievements(); }

    public boolean achievementIsComplete(int achievementId, int customerId) { return dbadapter.achievementAccomplished(achievementId, customerId); }

    public void addCompletedAchievement(int achievementId, int customerId) { dbadapter.addCompletedAchievement(achievementId, customerId); }

    public void updateTransactionsTotal(int customerId, double updateAmount) { dbadapter.updateTransactionsTotal(customerId, updateAmount); }

    public double getTransactionsTotal(int customerId) { return dbadapter.getTransactionsTotal(customerId); }

    public void incrementLogin(int customerId) { dbadapter.incrementLogin(customerId); }

    public int getLoginsTotal(int customerId) { return dbadapter.getLoginsTotal(customerId); }

    public ArrayList<Offer> getActiveOffers() { return dbadapter.getActiveOffers(customerId); }

    public void activateOffer(Offer offer){ dbadapter.activateOffer(offer, customerId);}

    public void spendOffersPoints(int price) {
        dbadapter.spendOfferPoints(customerId,price);
    }

    public void gainOffersPoints(int amount){
        dbadapter.gainOfferPoints(customerId,amount);
    }

    public DatabaseAdapter getAdapter(){
        return dbadapter;
    }

    public void addRecipient(String name, String sortCode, int accountNumber){
        dbadapter.addRecipient(customerId,name,sortCode,accountNumber);
    }

    public void checkEmptyField(TextView field, String errorMsg) throws EmptyMandatoryFieldException {
        if (field.getText().toString().isEmpty()){
            throw new EmptyMandatoryFieldException(errorMsg);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // Create the Google API Client with access to location services
    protected synchronized void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Handle Google API Client connection
    @Override
    public void onConnected(Bundle bundle) {
       mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    // Handle Google API Client connection suspension
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    // Handle Google API Client connection failure
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
