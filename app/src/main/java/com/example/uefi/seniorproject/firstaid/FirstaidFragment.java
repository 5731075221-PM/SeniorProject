package com.example.uefi.seniorproject.firstaid;


import android.graphics.Typeface;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstaidFragment extends Fragment {

    public TextView head;
    public TextView body;
    public TextView fire;
    public TextView poison;
    public TextView normal;
    public TextView cpr;
    public TextView transport;
    public TextView textTool;
    public String toolbar;

    public FirstaidFragment() {
        // Required empty public constructor
    }
    public static FirstaidFragment newInstance() {
        FirstaidFragment fragment = new FirstaidFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid, container, false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("การปฐมพยาบาล");

        head = (TextView) view.findViewById(R.id.head);
        body = (TextView) view.findViewById(R.id.body);
        fire = (TextView) view.findViewById(R.id.fire);
        poison = (TextView) view.findViewById(R.id.poison);
        normal = (TextView) view.findViewById(R.id.normal);
        cpr = (TextView) view.findViewById(R.id.cpr);
        transport = (TextView) view.findViewById(R.id.transport);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("การปฐมพยาบาล");

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Regular.ttf");
        head.setTypeface(font);
        body.setTypeface(font);
        fire.setTypeface(font);
        poison.setTypeface(font);
        normal.setTypeface(font);
        cpr.setTypeface(font);
        transport.setTypeface(font);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "ตา หู คอ";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,1),"ตา หู คอ");
                transaction.addToBackStack("ตา หู คอ");
                transaction.commit();
            }
        });
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "กล้ามเนื้อ กระดูก ลำไส้";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,2));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "แผลไหม้ น้ำร้อนลวก";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,3));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        poison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "พิษ สารพิษ";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,4));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "เบ็ดเตล็ด";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,5));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        cpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "การช่วยฟื้นคืนชีพขั้นพื้นฐาน";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,5));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "การยกและเคลื่อนย้ายผู้ป่วย";
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance(toolbar,5));
                transaction.addToBackStack("");
                transaction.commit();
            }
        });


        return view;
    }

}
