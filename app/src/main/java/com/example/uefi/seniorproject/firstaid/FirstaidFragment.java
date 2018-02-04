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

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                view.findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.nav_firstaid2:
                                selectedFragment = FirstaidNavFragment.newInstance();
                                break;
                            case R.id.nav_cpr:
                                selectedFragment = CprNavFragment.newInstance();
                                break;
                            case R.id.nav_transit:
                                selectedFragment = TransitNavFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_firstaid, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        FragmentTransaction transaction = getFragmentManager() .beginTransaction();
        transaction.replace(R.id.frame_firstaid, FirstaidNavFragment.newInstance());
        transaction.commit();



        return view;
    }

}
