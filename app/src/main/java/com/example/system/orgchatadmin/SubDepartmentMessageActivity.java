package com.example.system.orgchatadmin;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SubDepartmentMessageActivity extends FragmentActivity implements
        ActionBar.TabListener {

    android.app.ActionBar actionBar;
    SwipeAdapter swipeAdapter;
    ViewPager pager;

    String tabs[] = {"Suggestion" , "Compliant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_department_message);

        actionBar = getActionBar();
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
    public void onTabSelected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }
}
