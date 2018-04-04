package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    DBHelperDAO dbHelperDAO;
    public TextView textTool;
    public String toolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private int id;
    private ArrayList items;

    public FirstaidSelectFragment() {
        // Required empty public constructor
    }

    public static FirstaidSelectFragment newInstance(String test) {
        FirstaidSelectFragment fragment = new FirstaidSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar",test);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_select, container, false);

        Bundle bundle = getArguments();
        toolbar = bundle.getString("toolbar");
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText(toolbar);


        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        id = dbHelperDAO.getFirstaidId(toolbar);
        items = new ArrayList();
        items = dbHelperDAO.getFirstaidDetail(id);


        RecyclerView rcv = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        rcv.setLayoutManager(mLayoutManager);
        CustomAdapterFirstaidDetail adapter = new CustomAdapterFirstaidDetail(getActivity(),items);
        rcv.setAdapter(adapter);

        return  view;
    }

}