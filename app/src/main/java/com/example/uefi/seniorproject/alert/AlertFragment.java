package com.example.uefi.seniorproject.alert;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.ChoiceItem;
import com.example.uefi.seniorproject.reminder.CustomAdapterNote;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends Fragment {
    public TextView add;
    public InternalDatabaseHelper internalDatabaseHelper;
    public ArrayList<ChoiceItem> listAlert;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNote mAdapter;
    public ImageView notePic;
    public AppBarLayout appBarLayout;

    public AlertFragment() {
        // Required empty public constructor
    }

    public static AlertFragment newInstance() {
        AlertFragment fragment = new AlertFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        appBarLayout.setExpanded(true, true);

//        listAlert = internalDatabaseHelper.readAllAlert();
//
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_note);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new CustomAdapterNote(getActivity(),listAlert,getActivity().getSupportFragmentManager(),AlertFragment.this);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (mAdapter.getOpenItems().size() > 0) {
//                    mAdapter.closeAllExcept(null);
//                }
//            }
//        });
//        mAdapter.notifyDataSetChanged();

        setupRecy();

        // add
        add = (TextView) view.findViewById(R.id.textView3);
        add.setText("เพิ่มรายการยา");
        LinearLayout addNote = (LinearLayout) view.findViewById(R.id.add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, AlertAddFragment.newInstance("add",0))
                        .addToBackStack("เพิ่มรายการยา")
                        .commit();
            }
        });

        notePic = (ImageView) view.findViewById(R.id.notePic);
        notePic.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.alert, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_setting:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, AlertSettingFragment.newInstance())
                        .addToBackStack("เพิ่มรายการยา")
                        .commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupRecy(){
        listAlert = internalDatabaseHelper.readAllAlert();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterNote(getActivity(),listAlert,getActivity().getSupportFragmentManager(),AlertFragment.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAdapter.getOpenItems().size() > 0) {
                    mAdapter.closeAllExcept(null);
                }
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
//        setHasOptionsMenu(true);
    }

    public void onPause() {
        super.onPause();
    }


    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    public void onDestroy() {
        super.onDestroy();
        setHasOptionsMenu(false);
    }

}
