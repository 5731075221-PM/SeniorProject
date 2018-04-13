package com.example.uefi.seniorproject.alert;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.ChoiceItem;
import com.example.uefi.seniorproject.reminder.NoteItem;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertAddFragment extends Fragment {

    public TextView textTool;
    public AppBarLayout appBarLayout;
    public String state,title,medicineName;
    public Bundle bundle;
    public LinearLayout save;
    public EditText medicineNameEdittext,medicineNumEdittext;
    public CheckBox breakfastCheckbox, lunchCheckbox, dinnerCheckbox,bedCheckbox,beforeCheckbox,afterCheckbox;
    public int medicineNum,alert_id;
    public Switch alert;
    public InternalDatabaseHelper internalDatabaseHelper;
    public ArrayList listFromDB;
    public Toast toast;

    public AlertAddFragment() {
        // Required empty public constructor
    }

    public static AlertAddFragment newInstance(String state,int alert_id) {
        AlertAddFragment fragment = new AlertAddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state",state);
        bundle.putInt("alert_id",alert_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_alert_add, container, false);

        appBarLayout.setExpanded(true, true);
        textTool = (TextView) getActivity().findViewById(R.id.textTool);

        //checkbox
        breakfastCheckbox = (CheckBox) view.findViewById(R.id.breakfast);
        lunchCheckbox = (CheckBox) view.findViewById(R.id.lunch);
        dinnerCheckbox = (CheckBox) view.findViewById(R.id.dinner);
        bedCheckbox  = (CheckBox) view.findViewById(R.id.bed);
        beforeCheckbox = (CheckBox) view.findViewById(R.id.before);
        afterCheckbox = (CheckBox) view.findViewById(R.id.after);
        alert = (Switch) view.findViewById(R.id.switchAlert);

        if(state.equals("add")) {
            beforeCheckbox.setChecked(true);
            alert.setChecked(true);
        }else if (state.equals("edit")){
            breakfastCheckbox.setChecked(Integer.parseInt(listFromDB.get(2).toString()) == 1);
            lunchCheckbox.setChecked(Integer.parseInt(listFromDB.get(3).toString()) == 1);
            dinnerCheckbox.setChecked(Integer.parseInt(listFromDB.get(4).toString()) == 1);
            bedCheckbox.setChecked(Integer.parseInt(listFromDB.get(5).toString()) == 1);
            beforeCheckbox.setChecked(Integer.parseInt(listFromDB.get(6).toString()) == 1);
            afterCheckbox.setChecked(Integer.parseInt(listFromDB.get(7).toString()) == 1);
            alert.setChecked(Integer.parseInt(listFromDB.get(8).toString()) == 1);
        }

        beforeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(afterCheckbox.isChecked()){
                    afterCheckbox.setChecked(false);
                }else{

                }
                beforeCheckbox.setChecked(true);
            }
        });
        afterCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(beforeCheckbox.isChecked()){
                    beforeCheckbox.setChecked(false);
                }else{

                }
                afterCheckbox.setChecked(true);
            }
        });

        //num
        medicineNumEdittext = (EditText) view.findViewById(R.id.editTextQuantity);
        medicineNumEdittext.setText(medicineNum+"");
//        medicineNumEdittext.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        medicineNumEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable edt) {

                int length = edt.length();
                if (length == 1 && medicineNumEdittext.getText().toString().equals( "0") )
                    medicineNumEdittext.setText("");
            }
        });

        //save
        save = (LinearLayout) view.findViewById(R.id.add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicineName = medicineNameEdittext.getText().toString();
                medicineNum = Integer.parseInt(medicineNumEdittext.getText().toString());

                if(!breakfastCheckbox.isChecked() &&
                        !lunchCheckbox.isChecked() &&
                        !dinnerCheckbox.isChecked() &&
                        !bedCheckbox.isChecked()){
                    if(toast ==null) {

                    }else {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "กรุณาเลือกช่วงเวลาที่ต้องรับประทานยา",
                            Toast.LENGTH_SHORT);
                    toast.show();

                }else {

                    if (state.equals("add")) {
                        int id = internalDatabaseHelper.createAlert(medicineName, medicineNum,
                                    breakfastCheckbox.isChecked() ? 1 : 0,
                                    lunchCheckbox.isChecked() ? 1 : 0,
                                    dinnerCheckbox.isChecked() ? 1 : 0,
                                    bedCheckbox.isChecked() ? 1 : 0,
                                    beforeCheckbox.isChecked() ? 1 : 0,
                                    afterCheckbox.isChecked() ? 1 : 0,
                                    alert.isChecked() ? 1 : 0);
                    } else if (state.equals("edit")) {
                        internalDatabaseHelper.updateAlert(alert_id, medicineName, medicineNum,
                                breakfastCheckbox.isChecked() ? 1 : 0,
                                lunchCheckbox.isChecked() ? 1 : 0,
                                dinnerCheckbox.isChecked() ? 1 : 0,
                                bedCheckbox.isChecked() ? 1 : 0,
                                beforeCheckbox.isChecked() ? 1 : 0,
                                afterCheckbox.isChecked() ? 1 : 0,
                                alert.isChecked() ? 1 : 0);
                    }

//                    ArrayList <NoteItem> breakfast_before1= internalDatabaseHelper.readAllAlertByIFAlertBefore("breakfast");
//                    ArrayList<String> breakfast = new ArrayList<>();
//                    ArrayList<Integer> quantity = new ArrayList<>();
//
//                    for(int i =0;i<breakfast_before1.size();i++){
//                        breakfast.add(breakfast_before1.get(i).getText());
//                        quantity.add(breakfast_before1.get(i).getNumber());
//                    }
//
//                    int hour = 2;
//                    int minute = 8;
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY,hour);
//                    calendar.set(Calendar.MINUTE,minute);
//                    calendar.set(Calendar.SECOND, 00);
//

//                    Intent intent = new Intent(getActivity(), AlarmReceiver.class);
//                    intent.putExtra("requestCode", 8);
//                    intent.putStringArrayListExtra("list", breakfast);
//                    intent.putIntegerArrayListExtra("quantity", quantity);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 8, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);




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

                    getFragmentManager().popBackStack();
                }
            }
        });

        //name
        medicineNameEdittext = (EditText) view.findViewById(R.id.editTextMedicine);
        medicineNameEdittext.setText(medicineName, TextView.BufferType.EDITABLE);
        medicineNameEdittext.addTextChangedListener(new TextWatcher() {
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

    private void showSave(){
        if(medicineNameEdittext.getText().toString().trim().length() == 0){
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
                    hideSoftKeyboard(AlertAddFragment.this);
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);

        bundle = getArguments();
        state = bundle.getString("state");
        alert_id = bundle.getInt("alert_id");

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(getActivity());
        internalDatabaseHelper.open();

        if(state.equals("add")){
            medicineName = "";
            medicineNum = 1;
        }else if( state.equals("edit")){
            listFromDB = internalDatabaseHelper.readAlert(alert_id);
            medicineName = listFromDB.get(0).toString();
            medicineNum = Integer.parseInt(listFromDB.get(1).toString());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    public void onResume() {
        super.onResume();
        if(state.equals("add"))
            title = "เพิ่มรายการยา";
        else if (state.equals("edit"))
            title = "แก้ไขรายการยา";
        textTool.setText(title);
    }

    public void onPause() {
        super.onPause();
        hideSoftKeyboard(AlertAddFragment.this);
    }
}
