package com.example.uefi.seniorproject.alert;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.akexorcist.googledirection.model.Line;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.NoteItem;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertSettingFragment extends Fragment {

    public LinearLayout breakfast,lunch,dinner,bed;
    public TextView textTool,dateBreakfast,dateLunch,dateDinner,dateBed;
    public AppBarLayout appBarLayout;
    public InternalDatabaseHelper internalDatabaseHelper;
    public int breakfast_hour,breakfast_minute,lunch_hour,lunch_minute,dinner_hour,dinner_minute,bed_hour,bed_minute,vibrate,sound;
    public ArrayList<Integer> list;
    public ImageView info;
    public Switch switchSound,switchVibrate;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

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


        switchSound = (Switch) view.findViewById(R.id.switchSound);
        switchVibrate = (Switch) view.findViewById(R.id.switchVibrate);
        switchVibrate.setChecked(vibrate == 1);
        switchSound.setChecked(sound == 1);

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

        info = (ImageView)  view.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(info).setText("แจ้งเตือนก่อนรับประทาน 45 นาที \nแจ้งเตือนหลังรับประทาน 30 นาที \nแจ้งเตือนก่อนเข้านอน 30 นาที")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();

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
        vibrate = list.get(8);
        sound = list.get(9);

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

    public void onResume() {
        super.onResume();
        textTool.setText("ตั้งค่า");
    }

    public void onPause() {
        super.onPause();
        internalDatabaseHelper.updateSetting(
                breakfast_hour,breakfast_minute,lunch_hour,lunch_minute,dinner_hour,dinner_minute,bed_hour,bed_minute,
                switchVibrate.isChecked() ? 1:0,switchSound.isChecked() ?1:0
        );

        ArrayList<NoteItem> breakfast_before = internalDatabaseHelper.readAllAlertByIFAlertBefore("breakfast");
        ArrayList<NoteItem> breakfast_after =  internalDatabaseHelper.readAllAlertByIFAlertAfter("breakfast");
        ArrayList<NoteItem> lunch_before =  internalDatabaseHelper.readAllAlertByIFAlertBefore("lunch");
        ArrayList<NoteItem> lunch_after = internalDatabaseHelper.readAllAlertByIFAlertAfter("lunch");
        ArrayList<NoteItem> dinner_before =  internalDatabaseHelper.readAllAlertByIFAlertBefore("dinner");
        ArrayList<NoteItem> dinner_after =  internalDatabaseHelper.readAllAlertByIFAlertAfter("dinner");
        ArrayList<NoteItem> bed =  internalDatabaseHelper.readAllAlertByIFAlert("bed");

        createAlarm(1,breakfast_before);
        createAlarm(2,breakfast_after);
        createAlarm(3,lunch_before);
        createAlarm(4,lunch_after);
        createAlarm(5,dinner_before);
        createAlarm(6,dinner_after);
        createAlarm(7,bed);

    }

    private void createAlarm(int request_code, ArrayList<NoteItem> noteItems){
        if(noteItems.size()==0){
            Log.i("alert add","size==0");
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.putExtra("requestCode", request_code);
            intent.putStringArrayListExtra("list", new ArrayList<String>());
            intent.putIntegerArrayListExtra("quantity", new ArrayList<Integer>());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),request_code,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }else {
            ArrayList<String> list = new ArrayList<>();
            ArrayList<Integer> quantity = new ArrayList<>();
            Log.i("alert add","size>0");
            for(int i =0;i<noteItems.size();i++){
                list.add(noteItems.get(i).getText());
                quantity.add(noteItems.get(i).getNumber());
            }

            ArrayList<Integer> setting = internalDatabaseHelper.readSetting();

            int hour = 0, minute=0;
            if(request_code ==1){
                hour = setting.get(0);
                minute = setting.get(1) -45;
            }else if(request_code ==2){
                hour = setting.get(0);
                minute = setting.get(1) +30;
            }else if(request_code ==3){
                hour = setting.get(2);
                minute = setting.get(3) -45;
            }else if(request_code ==4){
                hour = setting.get(2);
                minute = setting.get(3) +30;
            }else if(request_code ==5){
                hour = setting.get(4);
                minute = setting.get(5) -45;
            }else if(request_code ==6){
                hour = setting.get(4);
                minute = setting.get(5) +30;
            }else if(request_code ==7){
                hour = setting.get(6);
                minute = setting.get(7) -30;
            }

            if(minute<0){
                hour = hour-1;
                minute = minute+60;
            } else if (minute > 60) {
                hour = hour+1;
                minute = minute -60;
            }
            if(hour<0){
                hour=23;
            }else if(hour>23){
                hour=0;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            Log.i("alert add before sent",request_code+" " + list.size() +" " +quantity.size());

            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.putExtra("requestCode", request_code);
            intent.putStringArrayListExtra("list", list);
            intent.putIntegerArrayListExtra("quantity", quantity);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), request_code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.alert, menu);
    }

}
