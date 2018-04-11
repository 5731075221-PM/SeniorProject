package com.example.uefi.seniorproject.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.reminder.ReminderFragment;

/**
 * Created by palida on 10-Apr-18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent1 = new Intent(context,AlertAddFragment.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //if we want ring on notifcation then uncomment below line//
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
////                setSmallIcon(R.drawable.applogo).
//                setContentIntent(pendingIntent).
//                setContentText("this is my notification").
//                setContentTitle("my notificaton").
////                setSound(alarmSound).
//        setAutoCancel(true);
//        notificationManager.notify(100,builder.build());


        Intent intent1 = new Intent(context,ReminderFragment.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_clock);

        Notification notification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.clock)
                        .setLargeIcon(bitmap)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Mymor")
                        .setContentText("Good night.")
                        .setAutoCancel(false)
                        .setColor(context.getResources().getColor(R.color.nav_bar))
                        .setVibrate(new long[] { 500, 1000, 500 })
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC )
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);

    }

}