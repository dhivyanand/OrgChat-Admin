package com.example.system.orgchatadmin.Activities;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.system.orgchatadmin.Fragments.CircularFragment;
import com.example.system.orgchatadmin.Fragments.CompliantFragment;
import com.example.system.orgchatadmin.Fragments.DepartmentFragment;
import com.example.system.orgchatadmin.Fragments.DocFragment;
import com.example.system.orgchatadmin.Fragments.FeedbackFragment;
import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Fragments.SuggestionFragment;
import com.example.system.orgchatadmin.Fragments.UserFragment;
import com.example.system.orgchatadmin.Receivers.NetworkStateReceiver;
import com.example.system.orgchatadmin.Services.ApplicationBackgroundService;

public class HomeNav extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0);

        if(NetworkStateReceiver.isOnline(getApplicationContext()) && !NetworkStateReceiver.isMyServiceRunning(ApplicationBackgroundService.class, getApplicationContext())){

            Intent appBgSer = new Intent(HomeNav.this,ApplicationBackgroundService.class);
            startService(appBgSer);

        }

        start_bg_service(); */

    }


}
