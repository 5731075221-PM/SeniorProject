package com.example.uefi.seniorproject.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by UEFI on 6/4/2561.
 */

public class HospitalNavFragment extends Fragment {
    final Fragment fragment1 = new HospitalNearbyFragment();
    final Fragment fragment2 = new SearchHospitalByName();
    int currentFrag = 0;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView title;
    AppBarLayout appBarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_nav, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpagerhospital);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    title = getActivity().findViewById(R.id.textTool);
                    title.setText("ค้นหาโรงพยาบาลใกล้เคียง");
                }else if(position == 1){
                    title = getActivity().findViewById(R.id.textTool);
                    title.setText("ค้นหาโรงพยาบาลจากชื่อ");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.hospital_nav_view);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    private void setupTabIcons() {
        Drawable image = getActivity().getResources().getDrawable(R.drawable.ic_near_me);
        image.setBounds(25, 25, 25, 25);
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(0).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.ic_search);
        image.setBounds(25, 25, 25, 25);
        tabLayout.getTabAt(1).setIcon(image);

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
        adapter.addFrag(fragment1, "ค้นหาโรงพยาบาลใกล้เคียง");
        adapter.addFrag(fragment2, "ค้นหาโรงพยาบาลจากชื่อ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        title = getActivity().findViewById(R.id.textTool);
        title.setText("ค้นหาโรงพยาบาลใกล้เคียง");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                HospitalMapFragment fragment = new HospitalMapFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentFrag == 0){
            title = getActivity().findViewById(R.id.textTool);
            title.setText("ค้นหาโรงพยาบาลใกล้เคียง");
        }else if(currentFrag == 1){
            title = getActivity().findViewById(R.id.textTool);
            title.setText("้นหาโรงพยาบาลจากชื่อ");
        }
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
