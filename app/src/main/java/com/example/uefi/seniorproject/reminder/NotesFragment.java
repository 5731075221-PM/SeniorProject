package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.uefi.seniorproject.firstaid.FirstaidFragment;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    public DBHelperDAO dbHelperDAO;
    public InternalDatabaseHelper internalDatabaseHelper;
    public ArrayList<ChoiceItem> listNew,createFromlistNote,listNote;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNote mAdapter;
    public ImageView notePic;

    public NotesFragment() {
        // Required empty public constructor
    }
    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_note);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterNote(getActivity(),listNote);
        mAdapter.setItemClickListener(new CustomAdapterNote.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position,int note_id) {
                createFromlistNote = internalDatabaseHelper.getChoiceItem(note_id,listNew);
                FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                transaction.replace(R.id.main_layout, NoteAddFragment.newInstance("edit",
                        note_id
                ));
                transaction.addToBackStack("แก้ไขบันทึกสุขภาพ");
                transaction.commit();
            }
        });
        mRecyclerView.setAdapter(mAdapter);



        LinearLayout addNote = (LinearLayout) view.findViewById(R.id.add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager() .beginTransaction();
            transaction.replace(R.id.main_layout, NoteAddFragment.newInstance("add",0
            ));
            transaction.addToBackStack("เพิ่มบันทึกสุขภาพ");
            transaction.commit();
            }
        });

        if(!listNote.isEmpty()) {
            notePic = (ImageView) view.findViewById(R.id.notePic);
            notePic.setVisibility(View.GONE);
        }
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        listNew = dbHelperDAO.getSymptomsChoice();

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();
        listNote = internalDatabaseHelper.readAllNote();
    }

}
