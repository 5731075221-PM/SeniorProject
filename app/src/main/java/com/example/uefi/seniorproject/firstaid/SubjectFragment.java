package com.example.uefi.seniorproject.firstaid;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment {

    DBHelperDAO dbHelperDAO;
    public TextView textTool;
    public String toolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private int id;
    private ArrayList items;

    public SubjectFragment() {
        // Required empty public constructor
    }

    public static SubjectFragment newInstance(String test) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar",test);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ศรีษะ ตา หู คอ");

        Bundle bundle = getArguments();
        toolbar = bundle.getString("toolbar");
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        textTool.setText(toolbar);
//        textTool = (TextView) view.findViewById(R.id.test);
//        textTool.setText(toolbar);


        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();

        id = dbHelperDAO.getFirstaidId(toolbar);

//        byte[] img = dbHelperDAO.test();
//        ImageView gg = (ImageView) view.findViewById(R.id.imageView2);
//        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
//        gg.setImageBitmap(bmp);
        items = new ArrayList();
        items = dbHelperDAO.getFirstaidDetail(id);
//        items.add(new DetailItem(id+""+toolbar));
////        items.add(new PicItem(img));
//        items.add(new PicDetailItem("ฮือๆ เทสๆ"));



        RecyclerView rcv = (RecyclerView) view.findViewById(R.id.recycler_firstaid);
        mLayoutManager = new LinearLayoutManager( getActivity());
        rcv.setLayoutManager(mLayoutManager);
        CustomAdapterFirstaidDetail adapter = new CustomAdapterFirstaidDetail(getActivity(),items);
        rcv.setAdapter(adapter);




        return  view;
    }

}
