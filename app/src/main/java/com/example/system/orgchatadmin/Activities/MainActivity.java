package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.system.orgchatadmin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        String status = sharedpreferences.getString("status","nil");
        String user = sharedpreferences.getString("user","nil");
        String password = sharedpreferences.getString("password","nil");

        if(status.equals("verified"))
            startActivity(new Intent(MainActivity.this,home.class));
        else
            startActivity(new Intent(MainActivity.this,Login.class));

        finish();

    }
}
