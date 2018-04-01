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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 14/3/2561.
 */

public class DiseaseNavFragment extends Fragment{
    final Fragment fragment1 = new DiseaseGridFragment();
    final Fragment fragment2 = new SearchSymptomFragment();
    final Fragment fragment3 = new DiseaseListFragment();
    Fragment selectedFragment;
    TextView title;
    int currentFrag = 0;

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
                        currentFrag = 0;
                        if(getChildFragmentManager().findFragmentByTag("1") != null){
                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
                                    .show(fragment1).commit();
                            selectedFragment = fragment1;
                        }
                        title = getActivity().findViewById(R.id.textTool);
                        title.setText("หมวดหมู่ของโรค");
                        break;
                    case R.id.item_favorite:
                        currentFrag = 1;
                        if(getChildFragmentManager().findFragmentByTag("2") != null){
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
                        title = getActivity().findViewById(R.id.textTool);
                        title.setText("ค้นหาโรคจากอาการ");
                        break;
                    case R.id.item_nearby:
                        currentFrag = 2;
                        if(getChildFragmentManager().findFragmentByTag("3") != null){
                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
                                    .show(fragment3).commit();
                            selectedFragment = fragment3;
                        }else {
                            getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment3,"3").commit();
                            selectedFragment = fragment3;
                        }
                        title = getActivity().findViewById(R.id.textTool);
                        title.setText("ค้นหาโรคจากชื่อ");
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
    public void onResume() {
        super.onResume();
        if(currentFrag == 0){
            title = getActivity().findViewById(R.id.textTool);
            title.setText("หมวดหมู่ของโรค");
        }else if(currentFrag == 1){
            title = getActivity().findViewById(R.id.textTool);
            title.setText("ค้นหาโรคจากอาการ");
        }else{
            title = getActivity().findViewById(R.id.textTool);
            title.setText("ค้นหาโรคจากชื่อ");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getActivity().findViewById(R.id.textTool);
        title.setText("หมวดหมู่ของโรค");

        selectedFragment = fragment1;
        Bundle args = new Bundle();
        args.putStringArrayList("dict",getArguments().getStringArrayList("dict"));
        args.putStringArrayList("stop",getArguments().getStringArrayList("stop"));
        fragment2.setArguments(args);
        getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment1,"1").commit();
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_bottom_nav,fragment1).commit();

    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}

