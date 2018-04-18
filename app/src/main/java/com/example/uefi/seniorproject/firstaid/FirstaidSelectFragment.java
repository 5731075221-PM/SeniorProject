package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstaidSelectFragment extends Fragment {

    public DBHelperDAO dbHelperDAO;
    public TextView textTool;
    public String toolbarText;
    private RecyclerView.LayoutManager mLayoutManager;
    private int id,isArrowYet;
    private ArrayList items;
    public AppBarLayout appBarLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    public FirstaidSelectFragment() {
        // Required empty public constructor
    }

    public static FirstaidSelectFragment newInstance(String test,int isArrowYet) {
        FirstaidSelectFragment fragment = new FirstaidSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar",test);
        bundle.putInt("isArrowYet",isArrowYet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_select, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        RecyclerView rcv = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        rcv.setLayoutManager(mLayoutManager);
        CustomAdapterFirstaidDetail adapter = new CustomAdapterFirstaidDetail(getActivity(),items);
        rcv.setAdapter(adapter);

        return  view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        toolbarText = bundle.getString("toolbar");

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        id = dbHelperDAO.getFirstaidId(toolbarText);
        items = dbHelperDAO.getFirstaidDetail(id);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        isArrowYet = bundle.getInt("isArrowYet");

        if(isArrowYet==0) {
            drawer = (DrawerLayout) (getActivity()).findViewById(R.id.drawer_layout);

            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                            toggle.setDrawerIndicatorEnabled(true);
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            toggle.syncState();
                            getFragmentManager().popBackStack();
                        } else
                            ((AppCompatActivity) getActivity()).onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }
        }
    }

    public void onResume() {
        super.onResume();
        textTool.setText(toolbarText);
    }
}
