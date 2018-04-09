package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    public DBHelperDAO dbHelperDAO;
    public InternalDatabaseHelper internalDatabaseHelper;
    public ArrayList<ChoiceItem> listNew,listNote;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNote mAdapter;
    public ImageView notePic;
    public AppBarLayout appBarLayout;

    public NotesFragment() {
        // Required empty public constructor
    }
    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);

        appBarLayout.setExpanded(true, true);

        listNote = internalDatabaseHelper.readAllNote();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_note);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterNote(getActivity(),listNote,getActivity().getSupportFragmentManager(),NotesFragment.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mAdapter.getOpenItems().size() > 0) {
                    mAdapter.closeAllExcept(null);
                }
            }
        });
        mAdapter.notifyDataSetChanged();


        LinearLayout addNote = (LinearLayout) view.findViewById(R.id.add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment, NoteAddFragment.newInstance("add",0))
                    .addToBackStack("เพิ่มบันทึกสุขภาพ")
                    .commit();
            }
        });

        notePic = (ImageView) view.findViewById(R.id.notePic);
        showPic();

        return view;
    }

    public void showPic(){
        if(!listNote.isEmpty()) {
            notePic.setVisibility(View.GONE);
        }else{
            notePic.setVisibility(View.VISIBLE);
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        listNew = dbHelperDAO.getSymptomsChoice();

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
    }

}
