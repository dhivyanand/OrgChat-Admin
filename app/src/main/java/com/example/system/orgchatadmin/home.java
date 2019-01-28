package com.example.system.orgchatadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class home extends AppCompatActivity {

    GridView grid;

    private boolean check_session(String uname , String password){

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        String status = sharedpreferences.getString("status","nil");
        String user = sharedpreferences.getString("user","nil");
        String password = sharedpreferences.getString("password","nil");

        if(status.equals("verified"))
            if(check_session(user,password))
                startActivity(new Intent(home.this,HomeNav.class));
            else
                startActivity(new Intent(home.this,Login.class));
        else
            startActivity(new Intent(home.this,Login.class));

        finish();

    }
}
