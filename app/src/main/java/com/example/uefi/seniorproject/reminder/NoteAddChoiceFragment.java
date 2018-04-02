package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteAddChoiceFragment extends Fragment {
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public CustomAdapterNoteAddChoice mAdapter;
    public ArrayList<ChoiceItem> list;
    public ArrayList<ChoiceItem> currentSelectedItems = new ArrayList<>();


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

        LinearLayout buttonClick = (LinearLayout) view.findViewById(R.id.save);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list_select", list);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().popBackStack();

            }
        });

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        list = bundle.getParcelableArrayList("list");
    }
}
