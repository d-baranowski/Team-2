package com.team.two.lloyds_app.screens.activities;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.Constants;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static DatabaseAdapter dbadapter;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbadapter = new DatabaseAdapter(this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        remember = (CheckBox) findViewById(R.id.remember_id);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        remember.setChecked(sharedPreferences.getBoolean("remember",false));
        mEmailView.setText(sharedPreferences.getString("userId",""));
    }
    public void login(View view){
        String userId = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (true){

            if (remember.isChecked()){
                SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId",userId);
                editor.putBoolean("remember",true);
                editor.apply();
            } else {
                SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId","");
                editor.putBoolean("remember",false);
                editor.apply();
            }

        //if (dbadapter.login(userId,password)){
            int databaseId = dbadapter.getId("123456789");
            //int databaseId = dbadapter.getId(userId);
            //Intent i = new Intent(this, MainScreen.class);
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("customerId", databaseId);
            startActivity(i);
            finish();
        }
    }
}



