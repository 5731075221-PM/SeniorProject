package com.example.uefi.seniorproject.reminder;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
//                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                NotesFragment tab2 = new NotesFragment();
//                TabFragment1 tab1 = new TabFragment1();
                return tab2;
//            case 2:
//                TabFragment3 tab3 = new TabFragment3();
//                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}