package com.example.uefi.seniorproject.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UEFI on 11/2/2561.
 */

public class PagerAdapterFragment extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    int mNumOfTabs;

    public PagerAdapterFragment(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        System.out.println("Title = ");
        return mFragmentTitleList.get(position);
    }
}
//    final int PAGE_COUNT = 3;
//    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
//    private Context context;
//
//    public PagerAdapterFragment (FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//    }
//    @Override
//    public Fragment getItem(int position) {
//        return CausePageFragment.newInstance(position + 1);
//    }
//
//    @Override
//    public int getCount() {
//        return PAGE_COUNT;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
//    }
//}
