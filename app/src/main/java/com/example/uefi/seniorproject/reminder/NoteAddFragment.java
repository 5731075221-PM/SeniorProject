package com.example.uefi.seniorproject.reminder;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteAddFragment extends Fragment {
    public TextView textTool,selectChoice,editDate;
    public Calendar myCalendar;
    public DatePickerDialog.OnDateSetListener date;
    public EditText commentEditText;
    public SimpleDateFormat sdf;
    final int FRAGMENT_CODE = 100;
    public ImageView arrow;
    public ArrayList<ChoiceItem> list,listNew;
    public DBHelperDAO dbHelperDAO;
    public InternalDatabaseHelper internalDatabaseHelper;
    public int day,month,year,note_id;
    public String state,title,comment;
    public Bundle bundle;
    public String[] dateComment;
    public LinearLayout addChoice,save;
    public AppBarLayout appBarLayout;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

    public NoteAddFragment() {
        // Required empty public constructor
    }

    public static NoteAddFragment newInstance(String state,int note_id) {
        NoteAddFragment fragment = new NoteAddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state",state);
        bundle.putInt("note_id",note_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_note_add, container, false);

        appBarLayout.setExpanded(true, true);

        textTool = (TextView) getActivity().findViewById(R.id.textTool);

        // calendat input
        String myFormat = "dd/MM/yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate = (TextView) view.findViewById(R.id.date);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                updateLabel();
            }
        };

        LinearLayout linearDate = (LinearLayout) view.findViewById(R.id.linear_date);
        linearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("ggggg","gggg");
                new DatePickerDialog(getActivity(), R.style.datepicker,date,
                        year, month,
                        day).show();
            }
        });
        firstSetEditDate();

        //choice input
        arrow = (ImageView) view.findViewById(R.id.imageView7);

        selectChoice = (TextView) view.findViewById(R.id.selectChoice);
        setSelectChoiceText(list);

        addChoice = (LinearLayout) view.findViewById(R.id.choice);
        addChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteAddChoiceFragment fragmentChild = NoteAddChoiceFragment.newInstance(list);
                fragmentChild.setTargetFragment(NoteAddFragment.this, FRAGMENT_CODE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, fragmentChild)
                        .addToBackStack(title)
                        .commit();
            }
        });

        //save
        save = (LinearLayout) view.findViewById(R.id.add);
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
                if(state.equals("add"))
                    internalDatabaseHelper.createNote(myCalendar.get(Calendar.DAY_OF_MONTH),myCalendar.get(Calendar.MONTH)+1, myCalendar.get(Calendar.YEAR),comment,symptom_note);
                else if(state.equals("edit")) {
                    internalDatabaseHelper.updateNote(note_id, day, month + 1, year, comment, symptom_note);
                }
                getFragmentManager().popBackStack();
            }
        });

        //comment
        commentEditText = (EditText) view.findViewById(R.id.editText);
        commentEditText.setText(comment, TextView.BufferType.EDITABLE);
        commentEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                showSave();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        showSave();
        //hide soft keyboard
        setupUI(view.findViewById(R.id.linear_choice));

        return view;
    }

    private boolean isUnCheckAllChoice(){
        for (ChoiceItem choice : list){
            if(choice.isCheck()){
                return false;
            }
        }
        return true;
    }

    private void showSave(){
        if(commentEditText.getText().toString().trim().length() == 0 &&
//                commentEditText.getText().toString().equals("") &&
                isUnCheckAllChoice()){
            save.setVisibility(View.GONE);
        }else{
            save.setVisibility(View.VISIBLE);
        }
    }

    private  void firstSetEditDate(){
        Date date1 = new Date(year-1900,month,day);
        if(state.equals("add"))
            editDate.setText("วันนี้");
        else if (state.equals("edit") &&
                sdf.format(Calendar.getInstance().getTime()).equals(sdf.format(date1)))
            editDate.setText("วันนี้");
        else
            editDate.setText(sdf.format(date1));
    }

    private void updateLabel() {
        day =  myCalendar.get(Calendar.DAY_OF_MONTH);
        month =  myCalendar.get(Calendar.MONTH);
        year =  myCalendar.get(Calendar.YEAR);

        Date date1 = new Date(year-1900,month,day);

        if(sdf.format(Calendar.getInstance().getTime()).equals(sdf.format(date1))){
            editDate.setText("วันนี้");
        }else {
            editDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                ArrayList<ChoiceItem> value = data.getParcelableArrayListExtra("list_select");
                if(value != null) {
                    list = value;
                    setSelectChoiceText(list);
                }
            }
        }
        showSave();
    }

    public void setSelectChoiceText(ArrayList<ChoiceItem> list){
        String set ="";
        for (int i =0 ;i<list.size();i++){
            if(list.get(i).isCheck()) {
                set += "- "+ list.get(i).getText()+"\n";
            }
        }
        if(set==""){
            arrow.setImageResource(R.drawable.ic_action_keyboard_arrow_right);
        }else{
            arrow.setImageResource(R.drawable.ic_action_keyboard_arrow_down);
        }
        selectChoice.setText(set);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        state = bundle.getString("state");
        note_id = bundle.getInt("note_id");
        dbHelperDAO = DBHelperDAO.getInstance(getActivity());
        dbHelperDAO.open();
        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();
        listNew = dbHelperDAO.getSymptomsChoice();

        myCalendar = Calendar.getInstance();

        if(state.equals("add")){
            list = listNew;
            day = myCalendar.get(Calendar.DAY_OF_MONTH);
            month = myCalendar.get(Calendar.MONTH);
            year = myCalendar.get(Calendar.YEAR);
            comment = "";
        }else if( state.equals("edit")){
            list = internalDatabaseHelper.getChoiceItem(note_id,listNew);
            dateComment = internalDatabaseHelper.getComment(note_id);
            day = Integer.parseInt(dateComment[0]);
            month = Integer.parseInt(dateComment[1]) -1 ;
            year = Integer.parseInt(dateComment[2]);
            comment = dateComment[3];
        }
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        drawer = (DrawerLayout) (getActivity()).findViewById(R.id.drawer_layout);


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle( getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!mToolBarNavigationListenerIsRegistered) {
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Doesn't have to be onBackPressed
//                    getFragmentManager().popBackStack();
//                    if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0)
//                        drawer.openDrawer(Gravity.START);
//
                    if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        toggle.setDrawerIndicatorEnabled(true);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        getFragmentManager().popBackStack();
                    }
                    else
                        ((AppCompatActivity) getActivity()).onBackPressed();
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    toggle.setDrawerIndicatorEnabled(true);
                }
            });
            mToolBarNavigationListenerIsRegistered = true;
        }

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(NoteAddFragment.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideSoftKeyboard(Fragment activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onResume() {
        super.onResume();
        if(state.equals("add"))
            title = "เพิ่มบันทึกสุขภาพ";
        else if (state.equals("edit"))
            title = "แก้ไขบันทึกสุขภาพ";
        textTool.setText(title);
    }

    public void onPause() {
        super.onPause();
        hideSoftKeyboard(NoteAddFragment.this);
    }

    public void onDestroy() {
        super.onDestroy();
        toggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();
    }

}
