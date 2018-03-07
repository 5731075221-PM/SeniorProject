//package com.example.uefi.seniorproject.fragment;
//
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.example.uefi.seniorproject.R;
//
///**
// * Created by UEFI on 3/1/2561.
// */
//
//public class HospitalSelectFragment extends Fragment{
//    Button button1,button2;
//
//    public HospitalSelectFragment() {
//        // Required empty public constructor
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_hospital_select, container, false);
//
//        button1 = (Button)view.findViewById(R.id.selectArea1);
//        button2 = (Button)view.findViewById(R.id.selectArea2);
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HospitalOption fragment = new HospitalOption();
//                Bundle bundle = new Bundle();
//                bundle.putString("type","0");
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.container_fragment, fragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_map, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//}
