package com.team.two.lloyds_app.screens.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.*;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.team.two.lloyds_app.Exceptions.EmptyMandatoryFieldException;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.objects.Account;
import com.team.two.lloyds_app.objects.Customer;
import com.team.two.lloyds_app.objects.Recipient;
import com.team.two.lloyds_app.screens.drawer.NavigationDrawerFragment;
import com.team.two.lloyds_app.screens.fragments.BranchFinderFragment;
import com.team.two.lloyds_app.screens.fragments.HelpScreenFragment;
import com.team.two.lloyds_app.screens.fragments.MainScreenFragment;
import com.team.two.lloyds_app.screens.fragments.OptionsFragment;
import com.team.two.lloyds_app.screens.fragments.StatementScreenFragment;
import com.team.two.lloyds_app.screens.fragments.TransactionsScreenFragment;
import com.team.two.lloyds_app.screens.fragments.MoneyPlannerFragment;
import com.team.two.lloyds_app.screens.fragments.AchievementsFragment;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends ActionBarActivity {
    // Mikey, the class needs to implement the following (copy and paste it):
    // implements AchievementsFragment.Listener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    private static DatabaseAdapter dbadapter;
    private String title;
    private Account account;
    GoogleMap googleMap;
    private Toolbar toolbar;
    private int customerId;
    private FragmentManager fm;

    //-------------- achievements variables START

    /*
    // Fragments
    AchievementsFragment mAchievementsFragment;

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    // achievements and scores we're pending to push to the cloud
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();
    */
    //-------------- END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ---------- achievements connection code START
        // Create the Google API Client with access to Plus and Games
        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // load outbox from file
        mOutbox.loadLocal(this);
        */

        //-------------------- END

        dbadapter = new DatabaseAdapter(this);
        customerId = getIntent().getExtras().getInt("customerId");
        fm = getSupportFragmentManager();

        setContentView(R.layout.activity_main_appbar);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

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

        // listen to fragment events
        //mAchievementsFragment = (AchievementsFragment) bf;
        //mAchievementsFragment.setListener(this);
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

    // ------------------- achievement sign in/out helper methods
    /*
    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }
        mAchievementsFragment.setGreeting("Hello, " + displayName);

        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Sign-in failed, so show sign-in button on main menu
        mAchievementsFragment.setGreeting(getString(R.string.signed_out_greeting));
    }

    void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
        if (mOutbox.mPrimeAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_prime));
            mOutbox.mPrimeAchievement = false;
        }
        if (mOutbox.mArrogantAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_arrogant));
            mOutbox.mArrogantAchievement = false;
        }
        if (mOutbox.mHumbleAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_humble));
            mOutbox.mHumbleAchievement = false;
        }
        if (mOutbox.mLeetAchievement) {
            Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_leet));
            mOutbox.mLeetAchievement = false;
        }
        if (mOutbox.mBoredSteps > 0) {
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_really_bored),
                    mOutbox.mBoredSteps);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_bored),
                    mOutbox.mBoredSteps);
        }
        if (mOutbox.mBoredSteps > 0) {
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_really_bored),
                    mOutbox.mBoredSteps);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.achievement_bored),
                    mOutbox.mBoredSteps);
        }
        mOutbox.saveLocal(this);
    }


    class AccomplishmentsOutbox {
        boolean mPrimeAchievement = false;
        boolean mHumbleAchievement = false;
        boolean mLeetAchievement = false;
        boolean mArrogantAchievement = false;
        int mBoredSteps = 0;
        int mEasyModeScore = -1;
        int mHardModeScore = -1;

        boolean isEmpty() {
            return !mPrimeAchievement && !mHumbleAchievement && !mLeetAchievement &&
                    !mArrogantAchievement && mBoredSteps == 0 && mEasyModeScore < 0 &&
                    mHardModeScore < 0;
        }

        public void saveLocal(Context ctx) {
        }

        public void loadLocal(Context ctx) {
        }
    }
    */

}
