package com.example.uefi.seniorproject.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
    String name = "",cause,symptom,treat,protect;
    TextView title;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_item, container, false);

        title = getActivity().findViewById(R.id.textTool);
        title.setText(name);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("Position = "+position);
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

        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        name = getArguments().getString("name");
        cause = dbHelperDAO.getContent(name,"cause");
        symptom = dbHelperDAO.getContent(name, "symptom");
        treat = dbHelperDAO.getContent(name, "treat");
        protect = dbHelperDAO.getContent(name, "protect");
    }

    private void setupTabIcons() {
        Drawable image = getActivity().getResources().getDrawable(R.drawable.image1_tab_select);
        image.setBounds(0, 0, 25, 25);
        tabLayout.getTabAt(0).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image2_tab_select);
        image.setBounds(0, 0, 25, 25);
        tabLayout.getTabAt(1).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image3_tab_select);
        image.setBounds(0, 0, 25, 25);
        tabLayout.getTabAt(2).setIcon(image);

        image = getActivity().getResources().getDrawable(R.drawable.image4_tab_select);
        image.setBounds(0, 0, 25, 25);
        tabLayout.getTabAt(3).setIcon(image);
        tabLayout.setTabTextColors(Color.GRAY,getResources().getColor(R.color.colorMacawBlueGreen));
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
