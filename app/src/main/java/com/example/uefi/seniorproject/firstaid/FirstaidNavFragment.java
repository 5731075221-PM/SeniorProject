package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstaidNavFragment extends Fragment {


    DBHelperDAO dbHelperDAO;
    private RecyclerView mRecyclerView;
    private CustomAdapterFirstaid mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public String toolbar;
    public int indicator;
    public ArrayList<String> list;

    public FirstaidNavFragment() {
        // Required empty public constructor
    }

    public static FirstaidNavFragment newInstance(String toolbar) {
        FirstaidNavFragment fragment = new FirstaidNavFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar",toolbar);
//        bundle.putInt("indicator",indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_nav, container, false);
      
        Bundle bundle = getArguments();
        toolbar = bundle.getString("toolbar");
//        indicator = bundle.getInt("indicator");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(toolbar);

//        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
//        dbHelperDAO.open();
//
//        list = dbHelperDAO.getFirstaidList(indicator);
//
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
//
//        mLayoutManager = new LinearLayoutManager( getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new CustomAdapterFirstaid(list);
//        mRecyclerView.setAdapter(mAdapter);




        return view;
    }

}
