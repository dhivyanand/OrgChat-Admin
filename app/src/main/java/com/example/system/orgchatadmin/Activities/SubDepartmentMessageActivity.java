package com.example.system.orgchatadmin.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Adapters.SwipeAdapter;

public class SubDepartmentMessageActivity extends AppCompatActivity implements
        ActionBar.TabListener {

    ActionBar actionBar;
    SwipeAdapter swipeAdapter;
    ViewPager pager;

    String tabs[] = {"Suggestion" , "Compliant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_department_message);

        actionBar = getSupportActionBar();
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.viewPager);

        pager.setAdapter(swipeAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(SubDepartmentMessageActivity.this));

        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });



    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
