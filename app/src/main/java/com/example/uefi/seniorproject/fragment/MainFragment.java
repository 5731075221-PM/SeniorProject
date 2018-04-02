package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uefi.seniorproject.MainActivity;
import com.example.uefi.seniorproject.R;

/**
 * Created by UEFI on 27/12/2560.
 */

public class MainFragment extends Fragment {
    AppBarLayout appBarLayout;

    public MainFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        appBarLayout.setExpanded(true, true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
    }
}

