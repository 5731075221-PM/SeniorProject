package com.example.uefi.seniorproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * Created by UEFI on 12/2/2561.
 */

@SuppressLint("ValidFragment")
public class TreatPageFragment extends Fragment {
    String treat = "";
    TextView treatTextView;

    public TreatPageFragment(String treat) {
        this.treat = treat;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treat_page, container, false);
        treatTextView = (TextView)view.findViewById(R.id.pageTextview);
        treatTextView.setText(treat);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
