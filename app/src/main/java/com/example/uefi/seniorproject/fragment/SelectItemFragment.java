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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

/**
 * Created by UEFI on 5/2/2561.
 */

public class SelectItemFragment extends Fragment{
    DBHelperDAO dbHelperDAO;
    String name = "",cause,symptom,treat,protect,type;
    TextView title;
    TabLayout tabLayout;
    ViewPager viewPager;
    AppBarLayout appBarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_item, container, false);

        appBarLayout.setExpanded(true, true);

        title = getActivity().findViewById(R.id.textTool);
        title.setText(name);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

//        t = (TextView)view.findViewById(R.id.testtt);
//        Bundle extraBundle = getArguments();
//        if(!extraBundle.isEmpty()){
//            name = extraBundle.getString("name");
//        }
//        new FetchData().execute();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        name = getArguments().getString("name");
        cause = dbHelperDAO.getContent(name,"cause");
        symptom = dbHelperDAO.getContent(name, "symptom");
        treat = dbHelperDAO.getContent(name, "treat");
        protect = dbHelperDAO.getContent(name, "protect");
        type = getArguments().getString("type");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.hospital_item_menu, menu);

        MenuItem item = menu.findItem(R.id.item_star);
        Drawable drawable = item.getIcon();
        if(dbHelperDAO.checkExistItem("disease", name)) drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        else drawable.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_star:
                Drawable drawable = item.getIcon();
                if(dbHelperDAO.checkExistItem("disease", name)){
                    drawable.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    dbHelperDAO.removeFavItem("disease", name);
                }else{
                    drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                    dbHelperDAO.addFavDiseaseItem("disease", name, type);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupTabIcons() {
        Drawable image = getActivity().getResources().getDrawable(R.drawable.image1_tab_select);
        image.setBounds(0, 0, 25, 25);
        tabLayout.getTabAt(0).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image2_tab_select);
        image.setBounds(25, 25, 25, 25);
        tabLayout.getTabAt(1).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image3_tab_select);
        image.setBounds(25, 25, 25, 25);
        tabLayout.getTabAt(2).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image4_tab_select);
        image.setBounds(25, 25, 25, 25);
        tabLayout.getTabAt(3).setIcon(image);
        tabLayout.setTabTextColors(Color.GRAY,getResources().getColor(R.color.second_bar));

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
        adapter.addFrag(new CausePageFragment(cause), "สาเหตุ");
        adapter.addFrag(new SymptomPageFragment(symptom), "อาการ");
        adapter.addFrag(new TreatPageFragment(treat), "วิธีรักษา");
        adapter.addFrag(new ProtectPageFragment(protect), "วิธีป้องกัน");
        viewPager.setAdapter(adapter);
    }
}
