package com.example.system.orgchatadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.system.orgchatadmin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this,home.class));
        //startActivity(new Intent(MainActivity.this,SubDepartmentMessageActivity.class));

        finish();

    }
}
