package com.example.uefi.seniorproject.reminder;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

    public TextView textTool;

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
//        View view =  inflater.inflate(R.layout.fragment_reminder, container, false);
//        textTool = (TextView) getActivity().findViewById(R.id.textTool);
//        textTool.setText("สุขภาพของฉัน");
//
//
//        return view;
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("สุขภาพของฉัน");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("บันทึกสุขภาพ"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(Color.GRAY,getResources().getColor(R.color.colorMacawBlueGreen));

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PagerAdapterReminder adapter = new PagerAdapterReminder
                (getFragmentManager(), tabLayout.getTabCount());
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_sample, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
