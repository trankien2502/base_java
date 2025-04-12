package com.livescore.soccerscore.matchlive.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.livescore.soccerscore.matchlive.MyApplication;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

public class ScheduleBroadcastReceiver extends BroadcastReceiver {
    FixtureModel fixtureBase;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = "Alarm Reboot";
//            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        } else {
            String toastText = "Alarm Received";
            Log.d("alarmcheck", toastText);
            Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            if (bundle != null)
                fixtureBase = (FixtureModel) bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
            else Log.d("alarmcheck", "bundle null");
            if (fixtureBase != null) {
                String type = intent.getStringExtra("type");
                String content;
                if ("early".equals(type)) {
                    content = "Don't forget the match " + fixtureBase.name + " will start at " + fixtureBase.starting_at;
                    Log.d("alarmcheck", "receive before");
                    if (fixtureBase.is_before_match && fixtureBase.isAlarm) {
                        Log.d("alarmcheck", "ring before");
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID_GENERAL)
                                .setContentTitle(fixtureBase.name)
                                .setContentText(content)
                                .setSmallIcon(R.drawable.img_logo)
                                .setCategory(NotificationCompat.CATEGORY_ALARM)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .build();

                        notificationManager.notify((int) System.currentTimeMillis(), notification);
                    } else
                        Log.d("alarmcheck", "before not set alarm" + fixtureBase.isAlarm + fixtureBase.is_before_match + fixtureBase);
                } else if ("ontime".equals(type)) {
                    content = "The match " + fixtureBase.name + " start!";
                    Log.d("alarmcheck", "receive alarm");
                    if (fixtureBase.isAlarm) {
                        Log.d("alarmcheck", "ring alarm");
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID_GENERAL)
                                .setContentTitle(fixtureBase.name)
                                .setContentText(content)
                                .setSmallIcon(R.drawable.img_logo)
                                .setCategory(NotificationCompat.CATEGORY_ALARM)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .build();

                        notificationManager.notify((int) System.currentTimeMillis(), notification);
                    } else Log.d("alarmcheck", "not set alarm" + fixtureBase.isAlarm);
                    if (fixtureBase.isAlarm || fixtureBase.isPin)
                        startAlarmService(context, fixtureBase);
                } else if ("event".equals(type)) {

                }

            } else {
                Log.d("alarmcheck", "null");
            }
        }
    }

    private void startAlarmService(Context context, FixtureModel fixtureBase1) {
        Intent intentService = new Intent(context, ScheduleService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj), fixtureBase1);
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }


    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleNotificationAndPin.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}


