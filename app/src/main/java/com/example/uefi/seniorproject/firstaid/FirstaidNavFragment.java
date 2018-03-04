package com.example.uefi.seniorproject.firstaid;


import android.graphics.Typeface;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class FirstaidNavFragment extends Fragment {


    DBHelperDAO dbHelperDAO;
    private RecyclerView mRecyclerView;
    private CustomAdapterFirstaid mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public String toolbar;
    public String indicator;
    public ArrayList<String> list;
    public TextView textTool;

    public FirstaidNavFragment() {
        // Required empty public constructor
    }

    public static FirstaidNavFragment newInstance(String toolbar,String indicator) {
        FirstaidNavFragment fragment = new FirstaidNavFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar",toolbar);
        bundle.putString("indicator",indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_nav, container, false);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText("การปฐมพยาบาล");

        Bundle bundle = getArguments();
        toolbar = bundle.getString("toolbar");
        indicator = bundle.getString("indicator");
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Regular.ttf");
        TextView header = (TextView) view.findViewById(R.id.textView);
        header.setText(toolbar);
//        header.setTypeface(font);


        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        list = dbHelperDAO.getFirstaidList(indicator);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterFirstaid(getActivity(),list);
        mAdapter.setItemClickListener(new CustomAdapterFirstaid.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.frameFirstaidNav, SubjectFragment.newInstance(list.get(position)));
//                transaction.replace(R.id.frameFirstaidNav, SelectFirstaidFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        mRecyclerView.setAdapter(mAdapter);




        return view;
    }

}
