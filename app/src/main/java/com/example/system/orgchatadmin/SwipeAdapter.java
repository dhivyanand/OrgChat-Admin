package com.example.system.orgchatadmin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SwipeAdapter extends FragmentPagerAdapter {

    SwipeAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return new SuggestionSwipeFragment();
            case 1:
                return new CompliantSwipeFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
