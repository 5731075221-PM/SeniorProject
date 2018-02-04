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
public class CprNavFragment extends Fragment {


    public CprNavFragment() {
        // Required empty public constructor
    }

    public static CprNavFragment newInstance() {
        CprNavFragment fragment = new CprNavFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cpr_nav, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("วิธีช่วยชีวิตขั้นพื้นฐาน");

        return view;
    }

}
