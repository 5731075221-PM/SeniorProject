package com.example.uefi.seniorproject.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
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
public class ProtectPageFragment extends Fragment {
    String protect = "";
    TextView protectTextView, headProtect;

    public ProtectPageFragment(String protect) {
        this.protect = protect;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protect_page, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
        protectTextView = (TextView)view.findViewById(R.id.pageTextview);
        protectTextView.setText(protect);
        protectTextView.setTypeface(tf);
        headProtect = (TextView)view.findViewById(R.id.headProtect);
        headProtect.setTypeface(tf);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
