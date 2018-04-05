package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    final int FRAGMENT_CODE = 100;
    public TextView textTool;
    PagerAdapterReminder adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    public Toolbar toolbar;
    public AppBarLayout.LayoutParams params;


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
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("สุขภาพของฉัน");

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("บันทึกสุขภาพ"));
        tabLayout.addTab(tabLayout.newTab().setText("กิจกรรมของฉัน"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(Color.GRAY,getResources().getColor(R.color.colorMacawBlueGreen));

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        adapter = new PagerAdapterReminder
                (getFragmentManager(), tabLayout.getTabCount());
//        viewPager.setOffscreenPageLimit(0);
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

        Log.i("Check", "onCreateView RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar);
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        params.setScrollFlags(0);
        Log.i("Check", "onCreate RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");

    }

    public void onDestroy() {
        super.onDestroy();
        params.setScrollFlags(1);
        Log.i("Check", "onDestroy RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");

    }

    public void onResume() {
        super.onResume();
        Log.i("Check", "onResume RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Check", "onActivityCreated  RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("Check", "onAttach       RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Check", "onDestroyView  RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onDetach() {
        super.onDetach();
        Log.i("Check", "onDetach RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onPause() {
        super.onPause();
        Log.i("Check", "onPause RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

    public void onStop() {
        super.onStop();
        Log.i("Check", "onStop RRRRRRRRRRRRRRRRRRRRRRRRRRRRrr");
    }

}
