package com.example.uefi.seniorproject.reminder;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    CustomViewPager viewPager;
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

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("บันทึกสุขภาพ"));
        tabLayout.addTab(tabLayout.newTab().setText("นัดคุณหมอ"));
        tabLayout.addTab(tabLayout.newTab().setText("กล่องยา"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (CustomViewPager) view.findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);
        adapter = new PagerAdapterReminder
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                appBarLayout.setExpanded(true, true);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.alert, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_setting:
//                //Do Whatever you want to do here.
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

//        toolbar = getActivity().findViewById(R.id.toolbar);
//        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
//                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//        params.setScrollFlags(0);


    }

    public void onDestroy() {
        super.onDestroy();
//        params.setScrollFlags(1);
    }

    public void onResume() {
        super.onResume();
        textTool.setText("สุขภาพของฉัน");

    }



}
