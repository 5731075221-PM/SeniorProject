package com.example.uefi.seniorproject.reminder;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.uefi.seniorproject.alert.AlertFragment;
import com.example.uefi.seniorproject.firstaid.FirstaidFragment;

/**
 * Created by palida on 08-Mar-18.
 */

public class PagerAdapterReminder extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterReminder(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NotesFragment tab1 = new NotesFragment();
                return tab1;
            case 1:
                AlertFragment tab2 = new AlertFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}