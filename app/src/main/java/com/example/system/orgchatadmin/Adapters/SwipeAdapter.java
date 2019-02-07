package com.example.system.orgchatadmin.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.system.orgchatadmin.Fragments.CompliantSwipeFragment;
import com.example.system.orgchatadmin.Fragments.SuggestionSwipeFragment;

public class SwipeAdapter extends FragmentPagerAdapter {

    public SwipeAdapter(FragmentManager fm){
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
