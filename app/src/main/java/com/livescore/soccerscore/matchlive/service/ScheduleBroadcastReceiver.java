package com.livescore.soccerscore.matchlive.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;

import java.util.Calendar;

public class ScheduleBroadcastReceiver extends BroadcastReceiver {
    FixtureBase fixtureBase;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = "Alarm Reboot";
//            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        } else {
            Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            if (bundle != null)
                fixtureBase = (FixtureBase) bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
            String toastText = "Alarm Received";
            Log.d("alarmcheck", toastText);
            if (fixtureBase != null) {
                startAlarmService(context, fixtureBase);
//                if (!alarm.isRecurring()) {
//                    Log.d("alarmcheck", "broadcast 1 day");
//                    startAlarmService(context, alarm);
//                } else {
//                    if (isAlarmToday(alarm)) {
//                        Log.d("alarmcheck", "broadcast nhieu day");
//                        startAlarmService(context, alarm);
//                    }
//                }
            }
        }
    }

    private void startAlarmService(Context context, FixtureBase fixtureBase1) {
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


