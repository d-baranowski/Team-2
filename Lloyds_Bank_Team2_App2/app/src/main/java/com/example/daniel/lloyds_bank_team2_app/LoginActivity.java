package com.example.daniel.lloyds_bank_team2_app;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static DatabaseAdapter dbadapter;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbadapter = new DatabaseAdapter(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
    }
    public void login(View view){
        String userId = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (dbadapter.login(userId,password)){
            int databaseId = dbadapter.getId(userId);
            Intent i = new Intent(this, MainScreen.class);
            i.putExtra("customerId", databaseId);
            startActivity(i);
            finish();
        }
    }
}



