package com.example.system.orgchatadmin.Activities;

import android.app.FragmentManager;
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
import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Fragments.SuggestionFragment;
import com.example.system.orgchatadmin.Fragments.UserFragment;

public class HomeNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0);

    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_department) {

            fragment = new DepartmentFragment();
            getSupportActionBar().setTitle("Department");

        } else if (id == R.id.nav_user) {

            fragment = new UserFragment();
            getSupportActionBar().setTitle("User");

        } else if (id == R.id.nav_suggestion) {

            fragment = new SuggestionFragment();
            getSupportActionBar().setTitle("Suggestion");

        } else if (id == R.id.nav_compliant) {

            fragment = new CompliantFragment();
            getSupportActionBar().setTitle("Compliant");

        } else if (id == R.id.nav_circular) {

            fragment = new CircularFragment();
            getSupportActionBar().setTitle("Circular");

        } else if (id == R.id.nav_account_settings) {

            getSupportActionBar().setTitle("Account");

        } else if (id == R.id.nav_server_settings) {

            getSupportActionBar().setTitle("Server");
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_frame, fragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
