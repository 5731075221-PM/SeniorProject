package com.example.uefi.seniorproject.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.uefi.seniorproject.R;

/**
 * Created by UEFI on 14/3/2561.
 */

public class DiseaseNavFragment extends Fragment{
    final Fragment fragment1 = new DiseaseGridFragment();
    final Fragment fragment2 = new SearchSymptomFragment();
    final Fragment fragment3 = new DiseaseListFragment();
    Fragment selectedFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_nav, container, false);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_nav_view);
//        bottomNavigationView.inflateMenu(R.menu.bottom_nav);
//        bottomNavigationView.setItemBackgroundResource(R.color.cardview_light_background);
//        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorMacawBlueGreen));
//        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorMacawBlueGreen));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_recent:
                        if(getChildFragmentManager().findFragmentByTag("1") != null){
                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
                                    .show(fragment1).commit();
                            selectedFragment = fragment1;
                        }
                        //new DiseaseGridFragment();
                        break;
                    case R.id.item_favorite:
                        if(getChildFragmentManager().findFragmentByTag("2") != null){
                            System.out.println("AAA = "+getChildFragmentManager().getBackStackEntryCount());
                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
                                    .show(fragment2).commit();
                            selectedFragment = fragment2;
                        }else {
                            getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment2,"2").commit();
                            selectedFragment = fragment2;
                        }
//                        Bundle args = new Bundle();
//                        args.putStringArrayList("dict",getArguments().getStringArrayList("dict"));
//                        args.putStringArrayList("stop",getArguments().getStringArrayList("stop"));
//                        fragment2.setArguments(args);
//                        selectedFragment = fragment2;//new SearchSymptomFragment();
//                        selectedFragment.setArguments(args);
                        break;
                    case R.id.item_nearby:
                        if(getChildFragmentManager().findFragmentByTag("3") != null){
                            System.out.println("AAA = "+getChildFragmentManager().getBackStackEntryCount());
                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
                                    .show(fragment3).commit();
                            selectedFragment = fragment3;
                        }else {
                            getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment3,"3").commit();
                            selectedFragment = fragment3;
                        }
                       //selectedFragment = fragment3;//new DiseaseListFragment();
                        break;
                }
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frame_bottom_nav, selectedFragment).commit();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedFragment = fragment1;
        Bundle args = new Bundle();
        args.putStringArrayList("dict",getArguments().getStringArrayList("dict"));
        args.putStringArrayList("stop",getArguments().getStringArrayList("stop"));
        fragment2.setArguments(args);
        getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment1,"1").commit();
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_bottom_nav,fragment1).commit();

    }
}

