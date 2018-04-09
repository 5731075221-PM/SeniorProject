package com.example.uefi.seniorproject.alert;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.akexorcist.googledirection.model.Line;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertSettingFragment extends Fragment {

    public LinearLayout breakfast,lunch,dinner,bed,save;
    public TextView textTool,dateBreakfast,dateLunch,dateDinner,dateBed;
    public AppBarLayout appBarLayout;
    public InternalDatabaseHelper internalDatabaseHelper;
    public int hour = 22;
    public int minute = 53;
    public int breakfast_hour,breakfast_minute,lunch_hour,lunch_minute,dinner_hour,dinner_minute,bed_hour,bed_minute;
    public ArrayList<Integer> list;

    public AlertSettingFragment() {
        // Required empty public constructor
    }

    public static AlertSettingFragment newInstance() {
        AlertSettingFragment fragment = new AlertSettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert_setting, container, false);

        appBarLayout.setExpanded(true, true);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);


        dateBreakfast = (TextView) view.findViewById(R.id.dateBreakfast);
        dateLunch = (TextView) view.findViewById(R.id.dateLunch);
        dateDinner = (TextView) view.findViewById(R.id.dateDinner);
        dateBed = (TextView) view.findViewById(R.id.dateBed);


        breakfast = (LinearLayout) view.findViewById(R.id.breakfast);
        lunch = (LinearLayout) view.findViewById(R.id.lunch);
        dinner = (LinearLayout) view.findViewById(R.id.dinner);
        bed = (LinearLayout) view.findViewById(R.id.bed);

        dateBreakfast.setText(setTextDate(breakfast_hour,breakfast_minute));
        dateLunch.setText(setTextDate(lunch_hour,lunch_minute));
        dateDinner.setText(setTextDate(dinner_hour,dinner_minute));
        dateBed.setText(setTextDate(bed_hour,bed_minute));

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        dateBreakfast.setText(setTextDate(selectedHour,selectedMinute));
                        breakfast_hour = selectedHour;
                        breakfast_minute = selectedMinute;
                    }
                }, breakfast_hour, breakfast_minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        dateLunch.setText(setTextDate(selectedHour,selectedMinute));
                        lunch_hour = selectedHour;
                        lunch_minute = selectedMinute;
                    }
                }, lunch_hour, lunch_minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        dateDinner.setText(setTextDate(selectedHour,selectedMinute));
                        dinner_hour = selectedHour;
                        dinner_minute = selectedMinute;
                    }
                }, dinner_hour, dinner_minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        dateBed.setText(setTextDate(selectedHour,selectedMinute));
                        bed_hour = selectedHour;
                        bed_minute = selectedMinute;
                    }
                }, bed_hour, bed_minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        //save
        save = (LinearLayout) view.findViewById(R.id.add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internalDatabaseHelper.updateSetting(
                        breakfast_hour,breakfast_minute,lunch_hour,lunch_minute,dinner_hour,dinner_minute,bed_hour,bed_minute
                );
                getFragmentManager().popBackStack();
            }
        });

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



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);


        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();
        list = internalDatabaseHelper.readSetting();
        breakfast_hour = list.get(0);
        breakfast_minute = list.get(1);
        lunch_hour = list.get(2);
        lunch_minute = list.get(3);
        dinner_hour = list.get(4);
        dinner_minute = list.get(5);
        bed_hour = list.get(6);
        bed_minute = list.get(7);

        setHasOptionsMenu(true);


    }

    public void onResume() {
        super.onResume();
        textTool.setText("ตั้งค่าเวลา");
    }

    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.alert, menu);
    }

}
