package com.example.uefi.seniorproject.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import java.util.ArrayList;

/**
 * Created by UEFI on 11/2/2561.
 */

@SuppressLint("ValidFragment")
public class CausePageFragment extends Fragment {
    String cause = "";
    TextView causeTextView, headCause;

    public CausePageFragment(String cause) {
        this.cause = cause;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cause_page, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
        causeTextView = (TextView)view.findViewById(R.id.pageTextview);
        causeTextView.setText(cause);
        causeTextView.setTypeface(tf);
        headCause = (TextView)view.findViewById(R.id.headCause);
        headCause.setTypeface(tf);
        return view;
    }
}