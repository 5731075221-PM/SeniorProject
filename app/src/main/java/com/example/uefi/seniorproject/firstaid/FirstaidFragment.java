package com.example.uefi.seniorproject.firstaid;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.uefi.seniorproject.R;

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
//        Toolbar toolbarTop = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("การปฐมพยาบาล");
        return view;
    }

}
