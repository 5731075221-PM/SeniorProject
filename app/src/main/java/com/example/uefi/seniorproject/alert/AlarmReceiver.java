package com.example.uefi.seniorproject.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by palida on 10-Apr-18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public InternalDatabaseHelper internalDatabaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        internalDatabaseHelper = InternalDatabaseHelper.getInstance(context);
        internalDatabaseHelper.open();

        ArrayList<Integer> setting = internalDatabaseHelper.readSetting();

        int code= intent.getIntExtra("requestCode", 1);
        ArrayList<String> list = intent.getStringArrayListExtra("list");
        ArrayList<Integer> quantity = intent.getIntegerArrayListExtra("quantity");

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        if(code == 1){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาก่อนอาหารเช้า");
        }else if (code== 2){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาหลังอาหารเช้า");
        }else if(code == 3){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาก่อนอาหารกลางวัน");
        }else if (code== 4){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาหลังอาหารกลางวัน");
        }else if(code == 5){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาก่อนอาหารเย็น");
        }else if (code== 6){
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาหลังอาหารเย็น");
        }else{
            inboxStyle.setBigContentTitle("อย่าลืมรับประทาน ยาก่อนนอน");
        }

        for(int i=0;i<list.size();i++){
            inboxStyle.addLine(list.get(i) +" จำนวน " + quantity.get(i)+ " เม็ด");
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_splash_screen);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Notification notification =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.clock)
//                        .setLargeIcon(bitmap)
////                        .setContentTitle("Mymor")
////                        .setContentText("Good night.")
//                        .setStyle(inboxStyle)
//                        .setAutoCancel(true)
//                        .setColor(context.getResources().getColor(R.color.nav_bar))
//                        .setVibrate(new long[] { 500, 1000, 500 })
//                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setSound(soundUri)
//                        .setContentIntent(pendingIntent)
//                        .build();

        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(code, context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_splash_screen)
                        .setLargeIcon(bitmap)
                        .setStyle(inboxStyle)
                        .setAutoCancel(true)
                        .setColor(context.getResources().getColor(R.color.nav_bar))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setOngoing(true)
                        .addAction(R.drawable.ic_action_check, "รับประทานยาแล้ว", dismissIntent);

        if(setting.get(8) ==1){
            builder.setVibrate(new long[] { 500, 1000, 500 });
        }
        if(setting.get(9) ==1){
            builder.setSound(soundUri);
        }



        Notification notification = builder.build();

//        notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(code, notification);


    }

}