package com.team.two.lloyds_app.screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.team.two.lloyds_app.database.DatabaseAdapter;
import com.team.two.lloyds_app.R;


public class AddRecipient extends Activity {
    private int ownerId;
    private EditText name;
    private EditText accountNumber;
    private EditText sortCode;
    private static DatabaseAdapter dbadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipient);

        dbadapter = new DatabaseAdapter(this);

        ownerId = getIntent().getExtras().getInt("ownerId");
        name = (EditText)findViewById(R.id.description_text);
        accountNumber = (EditText)findViewById(R.id.account_number_text);
        sortCode = (EditText)findViewById(R.id.sort_code_text);


    }

    public void addRecipient(View view){
        dbadapter.addRecipient(ownerId, name.getText().toString(), sortCode.getText().toString(),Integer.parseInt(accountNumber.getText().toString()));
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Recipient Added", duration);
        toast.show();
        finish();
    }


}
