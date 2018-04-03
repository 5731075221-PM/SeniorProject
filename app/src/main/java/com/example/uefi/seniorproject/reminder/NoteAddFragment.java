package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.DBHelperDAO;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteAddFragment extends Fragment {
    public TextView textTool;
    public Calendar myCalendar;
    public DatePickerDialog.OnDateSetListener date;
    public EditText editDate;
    public SimpleDateFormat sdf;
    final int FRAGMENT_CODE = 100;
    public ImageView arrow;
    public ArrayList<ChoiceItem> list;
    public TextView selectChoice;
    public EditText commentEditText;
    public String comment;
    public DBHelperDAO dbHelperDAO;
    public InternalDatabaseHelper internalDatabaseHelper;
    public int day,month,year;
    public String state,title;
    Bundle bundle;

    public NoteAddFragment() {
        // Required empty public constructor
    }

    public static NoteAddFragment newInstance(String state,ArrayList<ChoiceItem> list,int day,int month,int year,String comment) {
        NoteAddFragment fragment = new NoteAddFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list",list);
        bundle.putInt("day",day);
        bundle.putInt("month",month);
        bundle.putInt("year",year);
        bundle.putString("comment",comment);
        bundle.putString("state",state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (savedInstanceState == null){
            dbHelperDAO = DBHelperDAO.getInstance(getActivity());
            dbHelperDAO.open();
            if(state.equals("add")) {
                list = dbHelperDAO.getSymptomsChoice();
            }else{
                list = bundle.getParcelableArrayList("list");
            }
        }else{
            list = savedInstanceState.getParcelableArrayList("list");
        }

        View view = inflater.inflate(R.layout.fragment_note_add, container, false);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);
        if(state.equals("add"))
            title = "เพิ่มบันทึกสุขภาพ";
        else if (state.equals("edit"))
            title = "แก้ไขบันทึกสุขภาพ";
            textTool.setText(title);

        // calendat input
        myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate = (EditText) view.findViewById(R.id.date);
        updateLabel();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                updateLabel();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.datepicker,date,
                        year, month,
                        day).show();
            }
        });


        //choice input
        arrow = (ImageView) view.findViewById(R.id.imageView7);

        selectChoice = (TextView) view.findViewById(R.id.selectChoice);
        setSelectChoiceText(list);

        LinearLayout addChoice = (LinearLayout) view.findViewById(R.id.choice);
        addChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteAddChoiceFragment fragmentChild = NoteAddChoiceFragment.newInstance(list);
                fragmentChild.setTargetFragment(NoteAddFragment.this, FRAGMENT_CODE);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.linear_choice, fragmentChild);
                transaction.addToBackStack(title);
                transaction.commit();
            }
        });

        //comment
        commentEditText = (EditText) view.findViewById(R.id.editText);
        commentEditText.setText(comment, TextView.BufferType.EDITABLE);

        //save
        LinearLayout save = (LinearLayout) view.findViewById(R.id.add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> symptom_note = new ArrayList<>();
                for(int i =0;i<list.size();i++){
                    if(list.get(i).isCheck())
                    symptom_note.add(list.get(i).getText());
                }
                String comment = commentEditText.getText().toString();
                //month start with 0
                internalDatabaseHelper.createNote(myCalendar.get(Calendar.DAY_OF_MONTH),myCalendar.get(Calendar.MONTH)+1, myCalendar.get(Calendar.YEAR),comment,symptom_note);
                getFragmentManager().popBackStack();
            }
        });

//        if(commentEditText.getText().toString().equals("") || list.isEmpty()) {
//            save.setVisibility(View.GONE);
//        }else{
//            save.setVisibility(View.VISIBLE);
//        }

        return view;
    }

    private void updateLabel() {
        String dayinput = day+"/" + month +"/" +year;
        if(sdf.format(myCalendar.getTime()).equals(sdf.format(Calendar.getInstance().getTime()))) {
            editDate.setText("วันนี้");
            //edit day wrongggggggggggggggggg
        }else {
            editDate.setText(sdf.format(myCalendar.getTime()));
        }
            Log.i("check","today1");
//        editDate.setText(sdf.format(myCalendar.getTime()));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                ArrayList<ChoiceItem> value = data.getParcelableArrayListExtra("list_select");
                if(value != null) {
                    list = value;
                    setSelectChoiceText(value);
                }
            }
        }
    }

    public void setSelectChoiceText(ArrayList<ChoiceItem> list){
        String set ="";
        for (int i =0 ;i<list.size();i++){
            if(list.get(i).isCheck()) {
                set += "- "+ list.get(i).getText()+"\n";
            }
        }
        if(set==""){
            arrow.setImageResource(R.drawable.right_arrow);
        }else{
            arrow.setImageResource(R.drawable.down_arrow);
        }
        selectChoice.setText(set);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        comment = bundle.getString("comment");
        day = bundle.getInt("day");
        month = bundle.getInt("month");
        year = bundle.getInt("year");
        state = bundle.getString("state");

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

    }

}
