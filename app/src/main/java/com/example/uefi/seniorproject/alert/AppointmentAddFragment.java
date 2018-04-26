package com.example.uefi.seniorproject.alert;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.NoteItem;
import com.tooltip.Tooltip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentAddFragment extends Fragment {
    public TextView textTool,editDate,dateBed,add,textView6,textView7,textView3;
    public AppBarLayout appBarLayout;
    public String state,title,hospitalName;
    public Bundle bundle;
    public LinearLayout save;
    public EditText hospitalEdittext;
    public int appointment_id,day,month,year,hour,minute;
    public Switch alert;
    public InternalDatabaseHelper internalDatabaseHelper;
    public ArrayList listFromDB;
    public Toast toast;
    public ImageView info;
    public Calendar myCalendar;
    public DatePickerDialog.OnDateSetListener date;
    public SimpleDateFormat sdf;
    public LinearLayout time;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    public AppointmentAddFragment() {
        // Required empty public constructor
    }

    public static AppointmentAddFragment newInstance(String state,int appointment_id) {
        AppointmentAddFragment fragment = new AppointmentAddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state",state);
        bundle.putInt("appointment_id",appointment_id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_add, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/THSarabunNew.ttf");

        appBarLayout.setExpanded(true, true);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);

        alert = (Switch) view.findViewById(R.id.switchAlert);
        if(state.equals("add")) {
            alert.setChecked(true);
        }else if (state.equals("edit")){
            alert.setChecked(Integer.parseInt(listFromDB.get(6).toString()) == 1);
        }

        //textview
        textView6 = (TextView) view.findViewById(R.id.textView6);
        textView6.setTypeface(tf);
        textView7 = (TextView) view.findViewById(R.id.textView7);
        textView7.setTypeface(tf);
        textView3 = (TextView) view.findViewById(R.id.textView3);
        textView3.setTypeface(tf);

        // calendat input
        String myFormat = "dd/MM/yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate = (TextView) view.findViewById(R.id.date);
        editDate.setTypeface(tf);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

        //time
        time = (LinearLayout) view.findViewById(R.id.time);
        dateBed = (TextView) view.findViewById(R.id.dateBed);
        dateBed.setTypeface(tf);
        dateBed.setText(setTextDate(hour,minute));
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dateBed.setText(setTextDate(selectedHour,selectedMinute));
                        hour = selectedHour;
                        minute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //save
        save = (LinearLayout) view.findViewById(R.id.add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("add"))
                    internalDatabaseHelper.createAppointment(day, month, year, hour, minute, hospitalEdittext.getText().toString(),
                            alert.isChecked() ? 1 : 0);
                else if (state.equals("edit")) {
                    internalDatabaseHelper.updateAppointment(appointment_id, day, month, year, hour, minute, hospitalEdittext.getText().toString(),
                            alert.isChecked() ? 1 : 0);
                }

                ArrayList<NoteItem> listIFAlert = internalDatabaseHelper.readAppointmentIFAlert();

                if (listIFAlert.size() == 0) {
                    Log.i("appointment add", "size==0");
                    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                    intent.putExtra("requestCode", 8);
//                    intent.putStringArrayListExtra("list", new ArrayList<String>());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 8, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day - 1, 18, 0);

                    if (calendar.before(Calendar.getInstance())) {
//                        calendar.add(Calendar.DATE, 1);
                    } else {
                        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                        intent.putExtra("requestCode", 8);
                        intent.putExtra("hour", hour);
                        intent.putExtra("minute", minute);
                        intent.putExtra("hospital", hospitalEdittext.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 8, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
                getFragmentManager().popBackStack();
            }
        });

        //tooltip
        info = (ImageView)  view.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(info).setText("แจ้งเตือนเวลา 18:00 น. ก่อนวันนัด")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();

            }
        });

        //hospital name
        hospitalEdittext = (EditText) view.findViewById(R.id.editTextHospital);
        hospitalEdittext.setTypeface(tf);
        hospitalEdittext.setText(hospitalName, TextView.BufferType.EDITABLE);
        hospitalEdittext.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                showSave();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        showSave();

        setupUI(view.findViewById(R.id.linear_choice));
        return view;
    }

    public String setTextDate(int selectedHour, int selectedMinute){
        String selectHour = selectedHour+"";
        String selectMinute = selectedMinute + "";
        if(selectedHour<9){
            selectHour = "0"+selectHour;
        }
        if(selectedMinute<9){
            selectMinute = "0" +selectMinute;
        }
        return (selectHour + ":" + selectMinute + " น.");
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
    private void showSave(){
        if(hospitalEdittext.getText().toString().trim().length() == 0){
            save.setVisibility(View.GONE);
        }else{
            save.setVisibility(View.VISIBLE);
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AppointmentAddFragment.this);
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
            title = "เพิ่มรายการนัดคุณหมอ";
        else if (state.equals("edit"))
            title = "แก้ไขรายการนัดคุณหมอ";
        textTool.setText(title);
    }

    public void onPause() {
        super.onPause();
        hideSoftKeyboard(AppointmentAddFragment.this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        bundle = getArguments();
        state = bundle.getString("state");
        appointment_id = bundle.getInt("appointment_id");

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

        myCalendar = Calendar.getInstance();
        if(state.equals("add")){
            hospitalName ="";
            hour = 8;
            minute =0;
            day = myCalendar.get(Calendar.DAY_OF_MONTH);
            month = myCalendar.get(Calendar.MONTH);
            year = myCalendar.get(Calendar.YEAR);
        }else if( state.equals("edit")){
            listFromDB = internalDatabaseHelper.readAppointment(appointment_id);
            day = Integer.parseInt(listFromDB.get(0).toString());
            month = Integer.parseInt(listFromDB.get(1).toString());
            year = Integer.parseInt(listFromDB.get(2).toString());
            hour = Integer.parseInt(listFromDB.get(3).toString());
            minute = Integer.parseInt(listFromDB.get(4).toString());
            hospitalName = listFromDB.get(5).toString();
        }
        setHasOptionsMenu(true);

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
//                   getFragmentManager().popBackStack();
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    toggle.setDrawerIndicatorEnabled(true);
                    if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        toggle.setDrawerIndicatorEnabled(true);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        getFragmentManager().popBackStack();
                    }
                    else
                        ((AppCompatActivity) getActivity()).onBackPressed();
                }
            });
            mToolBarNavigationListenerIsRegistered = true;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    public void onDestroy() {
        super.onDestroy();
        toggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();
    }
}
