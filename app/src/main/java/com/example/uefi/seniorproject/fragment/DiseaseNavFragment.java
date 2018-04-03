package com.example.uefi.seniorproject.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
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
    TabLayout tabLayout;
    ViewPager viewPager;
    AppBarLayout appBarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_nav, container, false);

        appBarLayout.setExpanded(true, true);

        viewPager = (ViewPager) view.findViewById(R.id.viewpagerdisease);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    title = getActivity().findViewById(R.id.textTool);
                    title.setText("หมวดหมู่ของโรค");
                }else if(position == 1){
                    title = getActivity().findViewById(R.id.textTool);
                    title.setText("ค้นหาโรคจากอาการ");
                }else{
                    title = getActivity().findViewById(R.id.textTool);
                    title.setText("ค้นหาโรคจากชื่อ");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.bottom_nav_view);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

//        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_nav_view);
//        bottomNavigationView.inflateMenu(R.menu.bottom_nav);
//        bottomNavigationView.setItemBackgroundResource(R.color.cardview_light_background);
//        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorMacawBlueGreen));
//        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorMacawBlueGreen));

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.item_recent:
//                        currentFrag = 0;
//                        if(getChildFragmentManager().findFragmentByTag("1") != null){
//                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
//                                    .show(fragment1).commit();
//                            selectedFragment = fragment1;
//                        }
//                        title = getActivity().findViewById(R.id.textTool);
//                        title.setText("หมวดหมู่ของโรค");
//                        break;
//                    case R.id.item_favorite:
//                        currentFrag = 1;
//                        if(getChildFragmentManager().findFragmentByTag("2") != null){
//                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
//                                    .show(fragment2).commit();
//                            selectedFragment = fragment2;
//                        }else {
//                            getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment2,"2").commit();
//                            selectedFragment = fragment2;
//                        }
////                        Bundle args = new Bundle();
////                        args.putStringArrayList("dict",getArguments().getStringArrayList("dict"));
////                        args.putStringArrayList("stop",getArguments().getStringArrayList("stop"));
////                        fragment2.setArguments(args);
////                        selectedFragment = fragment2;//new SearchSymptomFragment();
////                        selectedFragment.setArguments(args);
//                        title = getActivity().findViewById(R.id.textTool);
//                        title.setText("ค้นหาโรคจากอาการ");
//                        break;
//                    case R.id.item_nearby:
//                        currentFrag = 2;
//                        if(getChildFragmentManager().findFragmentByTag("3") != null){
//                            getChildFragmentManager().beginTransaction().hide(selectedFragment)
//                                    .show(fragment3).commit();
//                            selectedFragment = fragment3;
//                        }else {
//                            getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment3,"3").commit();
//                            selectedFragment = fragment3;
//                        }
//                        title = getActivity().findViewById(R.id.textTool);
//                        title.setText("ค้นหาโรคจากชื่อ");
//                        break;
//                }
////                getActivity().getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.frame_bottom_nav, selectedFragment).commit();
//                return true;
//            }
//        });

        return view;
    }

    private void setupTabIcons() {
        Drawable image = getActivity().getResources().getDrawable(R.drawable.ic_grid);
        image.setBounds(25, 25, 25, 25);
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(0).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.ic_search);
        image.setBounds(25, 25, 25, 25);
        tabLayout.getTabAt(1).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.ic_sort_list);
        image.setBounds(25, 25, 25, 25);
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(image);

        tabLayout.setTabTextColors(Color.WHITE,getResources().getColor(R.color.cardview_light_background));

        ViewGroup childTabLayout = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < childTabLayout.getChildCount(); i++) {
            ViewGroup viewTab = (ViewGroup) childTabLayout.getChildAt(i);
            for (int j = 0; j < viewTab.getChildCount(); j++) {
                View tabTextView = viewTab.getChildAt(j);
                if (tabTextView instanceof TextView) {
                    Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");
                    ((TextView) tabTextView).setTypeface(typeface);
                    ((TextView) tabTextView).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapterFragment adapter = new PagerAdapterFragment(getChildFragmentManager());
        adapter.addFrag(fragment1, "หมวดหมู่ของโรค");
        adapter.addFrag(fragment2, "ค้นหาจากอาการ");
        adapter.addFrag(fragment3, "รายชื่อของโรค");
        viewPager.setAdapter(adapter);
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

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        title = getActivity().findViewById(R.id.textTool);
        title.setText("หมวดหมู่ของโรค");

//        selectedFragment = fragment1;
        Bundle args = new Bundle();
        args.putStringArrayList("dict",getArguments().getStringArrayList("dict"));
        args.putStringArrayList("stop",getArguments().getStringArrayList("stop"));
        fragment2.setArguments(args);
//        getChildFragmentManager().beginTransaction().add(R.id.frame_bottom_nav,fragment1,"1").commit();
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

