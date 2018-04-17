package com.example.uefi.seniorproject.food;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment implements SearchView.OnQueryTextListener{
    public AppBarLayout appBarLayout;
    public TextView textTool;
    SearchView searchView;


    public FoodFragment() {
        // Required empty public constructor
    }

    public static FoodFragment newInstance() {
        FoodFragment fragment = new FoodFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);

        searchView = (SearchView) view.findViewById(R.id.searchSymptom);
        searchView.setBackgroundResource(R.drawable.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("ค้นหารายชื่ออาหาร");
        searchView.setOnQueryTextListener(this);


        return view;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
//        internalDatabaseHelper.open();

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

    }

    public void onResume() {
        super.onResume();
        textTool.setText("เมนูของฉัน");

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
