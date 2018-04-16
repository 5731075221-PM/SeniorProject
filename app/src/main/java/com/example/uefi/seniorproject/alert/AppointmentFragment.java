package com.example.uefi.seniorproject.alert;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.CustomAdapterNote;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {
    public InternalDatabaseHelper internalDatabaseHelper;
    public AppBarLayout appBarLayout;
    public ArrayList<AppointmentItem> listAppointment;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNote mAdapter;
    public ImageView notePic;
    public TextView add;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        appBarLayout.setExpanded(true, true);

        listAppointment = internalDatabaseHelper.readAppointment();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_appointment);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterNote(getActivity(),listAppointment,getActivity().getSupportFragmentManager(),AppointmentFragment.this);
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter.notifyDataSetChanged();

        // add
        add = (TextView) view.findViewById(R.id.textView3);
        add.setText("เพิ่มรายการนัดคุณหมอ");
        LinearLayout addNote = (LinearLayout) view.findViewById(R.id.add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, AppointmentAddFragment.newInstance("add",0))
                        .addToBackStack("")
                        .commit();
            }
        });

        notePic = (ImageView) view.findViewById(R.id.notePic);
        showPic();
        return view;
    }

    public void showPic(){
        if(!listAppointment.isEmpty()) {
            notePic.setVisibility(View.GONE);
        }else{
            notePic.setVisibility(View.VISIBLE);
        }
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
//        setHasOptionsMenu(true);
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
