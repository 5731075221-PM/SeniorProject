package com.example.uefi.seniorproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * Created by UEFI on 11/2/2561.
 */

@SuppressLint("ValidFragment")
public class PageFragment extends Fragment {
 private int mPage;

    @SuppressLint("ValidFragment")
    public PageFragment(int mPage) {
        this.mPage = mPage;
    }

    //    public static PageFragment newInstance(int page) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
//        PageFragment fragment = new PageFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPage = getArguments().getInt(ARG_PAGE);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPage);
        return view;
    }
}