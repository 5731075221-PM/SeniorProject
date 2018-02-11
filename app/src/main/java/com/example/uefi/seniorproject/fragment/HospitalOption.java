package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

/**
 * Created by UEFI on 7/2/2561.
 */

public class HospitalOption extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_option, container, false);

        ConstraintLayout nearbyHospital = (ConstraintLayout)view.findViewById(R.id.nearbyHospitalLayout);
        ConstraintLayout areaHospital = (ConstraintLayout)view.findViewById(R.id.areaHospitalLayout);

        nearbyHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HospitalNearbyFragment fragment = new HospitalNearbyFragment();
                fragment.setArguments(getArguments());
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        areaHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HospitalAreaFragment fragment = new HospitalAreaFragment();
                fragment.setArguments(getArguments());
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
