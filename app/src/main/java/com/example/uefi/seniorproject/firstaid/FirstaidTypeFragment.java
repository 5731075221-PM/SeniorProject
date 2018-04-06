package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class FirstaidTypeFragment extends Fragment {


    public DBHelperDAO dbHelperDAO;
    public RecyclerView mRecyclerView;
    public CustomAdapterFirstaid mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public String toolbar,indicator;
    public ArrayList<String> list;
    public TextView textTool;
    public AppBarLayout appBarLayout;

    public FirstaidTypeFragment() {
        // Required empty public constructor
    }

    public static FirstaidTypeFragment newInstance(String indicator) {
        FirstaidTypeFragment fragment = new FirstaidTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("indicator",indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_type, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText(indicator);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterFirstaid(getActivity(),list);
        mAdapter.setItemClickListener(new CustomAdapterFirstaid.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, FirstaidSelectFragment.newInstance(list.get(position)))
                        .addToBackStack("การปฐมพยาบาล")
                        .commit();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        indicator = bundle.getString("indicator");

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        list = dbHelperDAO.getFirstaidList(indicator);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
    }

}
