package com.example.uefi.seniorproject.firstaid;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstaidFragment extends Fragment {

    public TextView head,body,fire,poison,normal,cpr,transport,textTool;
    public String toolbar;
    public AppBarLayout appBarLayout;

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

        appBarLayout.setExpanded(true, true);

        head = (TextView) view.findViewById(R.id.head);
        body = (TextView) view.findViewById(R.id.body);
        fire = (TextView) view.findViewById(R.id.fire);
        poison = (TextView) view.findViewById(R.id.poison);
        normal = (TextView) view.findViewById(R.id.normal);
        cpr = (TextView) view.findViewById(R.id.cpr);
        transport = (TextView) view.findViewById(R.id.transport);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("การปฐมพยาบาล");

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "ตา หู คอ";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidTypeFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "กล้ามเนื้อ กระดูก ลำไส้";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidTypeFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "แผลไหม้ น้ำร้อนลวก";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidTypeFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });
        poison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "พิษ สารพิษ";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidTypeFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "เบ็ดเตล็ด";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidTypeFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });
        cpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "การช่วยฟื้นคืนชีพขั้นพื้นฐาน";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidSelectFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();

            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar = "การยกและเคลื่อนย้ายผู้ป่วย";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidSelectFragment.newInstance(toolbar))
                        .addToBackStack(toolbar)
                        .commit();
            }
        });

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
    }

}
