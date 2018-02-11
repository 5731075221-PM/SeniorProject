package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

    public FirstaidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("การปฐมพยาบาล");

        LinearLayout app_layer = (LinearLayout) view.findViewById (R.id.blood);
        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sent bundle ma duay na *********

                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.relaFirstaid, FirstaidNavFragment.newInstance());
                transaction.addToBackStack("");
                transaction.commit();
            }
        });


        return view;
    }

}
