package com.example.uefi.seniorproject.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;
import com.example.uefi.seniorproject.reminder.NoteItem;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by palida on 27-Apr-18.
 */

public class SetAlarmAfterBoot extends BroadcastReceiver {
    public InternalDatabaseHelper internalDatabaseHelper;
    public Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
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
            Intent intent = new Intent(mContext, AlarmReceiver.class);
            intent.putExtra("requestCode", request_code);
            intent.putStringArrayListExtra("list", new ArrayList<String>());
            intent.putIntegerArrayListExtra("quantity", new ArrayList<Integer>());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,request_code,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
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

            Intent intent = new Intent(mContext, AlarmReceiver.class);
            intent.putExtra("requestCode", request_code);
            intent.putStringArrayListExtra("list", list);
            intent.putIntegerArrayListExtra("quantity", quantity);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, request_code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
