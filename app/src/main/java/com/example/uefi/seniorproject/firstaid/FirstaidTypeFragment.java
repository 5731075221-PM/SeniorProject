package com.example.uefi.seniorproject.firstaid;


import android.app.Activity;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstaidTypeFragment extends Fragment {


    DBHelperDAO dbHelperDAO;
    private RecyclerView mRecyclerView;
    private CustomAdapterFirstaid mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public String toolbar;
    public String indicator;
    public ArrayList<String> list;
    public TextView textTool;

    public FirstaidTypeFragment() {
        // Required empty public constructor
    }

    public static FirstaidTypeFragment newInstance(String indicator) {
        FirstaidTypeFragment fragment = new FirstaidTypeFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("toolbar",toolbar);
        bundle.putString("indicator",indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_firstaid_type, container, false);

        Bundle bundle = getArguments();
//        toolbar = bundle.getString("toolbar");
        indicator = bundle.getString("indicator");

        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText(indicator);

//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Regular.ttf");
//        TextView header = (TextView) view.findViewById(R.id.textView);
//        header.setText(indicator);
//        header.setTypeface(font);


        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        list = dbHelperDAO.getFirstaidList(indicator);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterFirstaid(getActivity(),list);
        mAdapter.setItemClickListener(new CustomAdapterFirstaid.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.frameFirstaidNav, FirstaidSelectFragment.newInstance(list.get(position)));
                transaction.addToBackStack("การปฐมพยาบาล");
                transaction.commit();
            }
        });

        mRecyclerView.setAdapter(mAdapter);




        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Check", "onActivityCreated 2");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("Check", "onAttach 2");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Check", "onCreate 2");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("Check", "onDestroy 2");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Check", "onDestroyView 2");
    }

    public void onDetach() {
        super.onDetach();
        Log.i("Check", "onDetach 2");
    }

//    public void onPause() {
//        super.onPause();
//        Log.i("Check", "onPause 2");
//    }

    @Override
    public void onPause() {
        super.onPause();

        FirstaidFragment fragment2= (FirstaidFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_fragment);
        if (fragment2!= null) {
            //you can call any function from fragment2
            fragment2.onResume();
        }
    }

    public void onResume() {
        super.onResume();
        Log.i("Check", "onResume 2");
    }

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart 2");
    }

    public void onStop() {
        super.onStop();
        Log.i("Check", "onStop 2");
    }
}
