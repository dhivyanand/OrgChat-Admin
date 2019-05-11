package com.example.system.orgchatadmin.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Services.ApplicationBackgroundService;

public class home extends AppCompatActivity {

    GridLayout grid;

    public static boolean isMyServiceRunning(Class<?> serviceClass,Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void start_bg_service(){

        if (isOnline(getApplicationContext()) && !isMyServiceRunning(ApplicationBackgroundService.class, getApplicationContext())) {

            Intent appBgSer = new Intent(getApplicationContext(),ApplicationBackgroundService.class);
            startService(appBgSer);

        }

    }

    public void logout(){
        finish();

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("status", "nil");
        editor.putString("user", "nil");
        editor.putString("password", "nil");
        editor.commit();

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("DELETE FROM USER");
        sqLiteDatabase.execSQL("DELETE FROM DEPARTMENT");
        sqLiteDatabase.execSQL("DELETE FROM SUBDEPARTMENT");
        sqLiteDatabase.execSQL("DELETE FROM MESSAGE");
        sqLiteDatabase.execSQL("DELETE FROM CIRCULAR");
        sqLiteDatabase.execSQL("DELETE FROM ATTACHMENT");
        sqLiteDatabase.execSQL("DELETE FROM FILE");
        sqLiteDatabase.execSQL("DELETE FROM DATE");

        startActivity(new Intent(home.this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        start_bg_service();

        grid = (GridLayout) findViewById(R.id.gridview);

        CardView user, circular, suggestion, compliant, esi, epf;

        final ImageButton logout = (ImageButton)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout();

            }
        });

        user = (CardView)grid.getChildAt(0);
        circular = (CardView)grid.getChildAt(1);
        suggestion = (CardView)grid.getChildAt(2);
        compliant = (CardView)grid.getChildAt(3);
        esi = (CardView)grid.getChildAt(4);
        epf = (CardView)grid.getChildAt(5);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,HomeUsers.class));
            }
        });

        circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,HomeCircular.class));
            }
        });

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,HomeSuggestion.class));
            }
        });

        compliant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,HomeCompliant.class));
            }
        });

        esi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://epostbox.sakthiauto.com/ESI/ESIHospital.pdf"));
                startActivity(browserIntent);
            }
        });

        epf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unifiedportal-mem.epfindia.gov.in/"));
                startActivity(browserIntent);

            }
        });

    }
}
