package com.example.uefi.seniorproject.reminder;


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
import android.support.v7.widget.Toolbar;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_note);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapterNote(getActivity(),listNote,getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        LinearLayout addNote = (LinearLayout) view.findViewById(R.id.add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager() .beginTransaction();
            transaction.replace(R.id.container_fragment, NoteAddFragment.newInstance("add",0
            ));
            transaction.addToBackStack("เพิ่มบันทึกสุขภาพ");
            transaction.commit();
            }
        });

        if(!listNote.isEmpty()) {
            notePic = (ImageView) view.findViewById(R.id.notePic);
            notePic.setVisibility(View.GONE);
        }
        Log.i("Check", "onCreateView NNNNNNNNNNNN");

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
        Log.i("Check", "onCreate NNNNNNNNNNNN");

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Check", "onActivityCreated NNNNNNNNNNNN");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("Check", "onDestroy NNNNNNNNNNNN");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Check", "onDestroyView NNNNNNNNNNNN");
    }

    public void onDetach() {
        super.onDetach();
        Log.i("Check", "onDetach NNNNNNNNNNNN");
    }

    public void onPause() {
        super.onPause();
        Log.i("Check", "onPause NNNNNNNNNNNN");
    }

    public void onResume() {
        super.onResume();
        Log.i("Check", "onResume NNNNNNNNNNNN");
    }

    public void onStart() {
        super.onStart();
        Log.i("Check", "onStart   NNNNNNNNNNNN");
    }

    public void onStop() {
        super.onStop();
        Log.i("Check", "onStop      NNNNNNNNNNNN");
    }

}
