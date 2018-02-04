package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uefi.seniorproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransitNavFragment extends Fragment {


    public TransitNavFragment() {
        // Required empty public constructor
    }

    public static TransitNavFragment newInstance() {
        TransitNavFragment fragment = new TransitNavFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transit_nav, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("วิธีเคลื่อนย้ายผู้ป่วย");

        return view;
    }

}
