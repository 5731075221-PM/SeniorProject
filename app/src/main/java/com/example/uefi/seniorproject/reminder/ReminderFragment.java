package com.example.uefi.seniorproject.reminder;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    public TextView textTool;
    PagerAdapterReminder adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    public Toolbar toolbar;
    public AppBarLayout.LayoutParams params;
    public AppBarLayout appBarLayout;


    public static ReminderFragment newInstance() {
        ReminderFragment fragment = new ReminderFragment();
        return fragment;
    }

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("สุขภาพของฉัน");

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("บันทึกสุขภาพ"));
        tabLayout.addTab(tabLayout.newTab().setText("กิจกรรมของฉัน"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        adapter = new PagerAdapterReminder
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        toolbar = getActivity().findViewById(R.id.toolbar);
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        params.setScrollFlags(0);


    }

    public void onDestroy() {
        super.onDestroy();
        params.setScrollFlags(1);
    }

}
