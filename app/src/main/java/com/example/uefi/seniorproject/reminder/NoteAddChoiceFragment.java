package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteAddChoiceFragment extends Fragment {
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNoteAddChoice mAdapter;
    public ArrayList<ChoiceItem> list,currentSelectedItems = new ArrayList<>();;
    public DBHelperDAO dbHelperDAO;
    public Bundle bundle;
    int isSave;
    public AppBarLayout appBarLayout;
    public TextView add;

    public NoteAddChoiceFragment() {
        // Required empty public constructor
    }

    public static NoteAddChoiceFragment newInstance(ArrayList<ChoiceItem> list) {
        NoteAddChoiceFragment fragment = new NoteAddChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list",list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_note_add_choice, container, false);

        appBarLayout.setExpanded(true, true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recy_choice);
        mLayoutManager = new LinearLayoutManager( getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapterNoteAddChoice(getActivity(),list, new CustomAdapterNoteAddChoice.OnItemCheckListener() {
            @Override
            public void onItemCheck(ChoiceItem item) {
                currentSelectedItems.add(item);

            }

            @Override
            public void onItemUncheck(ChoiceItem item) {
                currentSelectedItems.remove(item);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        isSave = 0;

        LinearLayout buttonClick = (LinearLayout) view.findViewById(R.id.save);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave=1;

                for(int i =0;i<list.size();i++){
                    list.get(i).setCheck(false);
                    for(int j = 0;j<currentSelectedItems.size();j++){
                        if(list.get(i).getText().equals(currentSelectedItems.get(j).getText())){
                            list.get(i).setCheck(true);
                        }
                    }
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list_select", list);
                intent.putExtra("isSave",isSave);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().popBackStack();

            }
        });
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");

        add = (TextView) view.findViewById(R.id.textView3);
        add.setTypeface(tf);

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

        if (savedInstanceState == null){
            dbHelperDAO = DBHelperDAO.getInstance(getActivity());
            dbHelperDAO.open();
            list = bundle.getParcelableArrayList("list");
        }else{
            list = savedInstanceState.getParcelableArrayList("list");
        }
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);


    }


}
