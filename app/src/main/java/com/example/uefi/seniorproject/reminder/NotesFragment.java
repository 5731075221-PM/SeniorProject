package com.example.uefi.seniorproject.reminder;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.firstaid.FirstaidNavFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


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
//        return inflater.inflate(R.layout.fragment_notes, container, false);
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);


        LinearLayout buttonClick = (LinearLayout) view.findViewById(R.id.linearLayout);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager() .beginTransaction();
            transaction.replace(R.id.main_layout, AddNoteFragment.newInstance());
            transaction.addToBackStack("");
            transaction.commit();

//            ReminderFragment parentFrag = ((ReminderFragment)NotesFragment.this.getParentFragment());
//            parentFrag.clickAdd();
            }
        });
        return view;
    }

}
