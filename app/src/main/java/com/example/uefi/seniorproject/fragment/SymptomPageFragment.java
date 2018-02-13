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
public class SymptomPageFragment extends Fragment {
    String symptom = "";
    TextView symptomTextView;

    public SymptomPageFragment(String symptom) {
        this.symptom = symptom;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symptom_page, container, false);
        symptomTextView = (TextView)view.findViewById(R.id.pageTextview);
        symptomTextView.setText(symptom);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
