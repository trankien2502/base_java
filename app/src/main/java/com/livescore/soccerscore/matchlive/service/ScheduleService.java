package com.livescore.soccerscore.matchlive.service;

import static com.livescore.soccerscore.matchlive.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;

import java.io.IOException;
import java.util.Objects;

public class ScheduleService extends Service {
    FixtureBase fixtureBase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle != null) {
            if (fixtureBase != null) {
//                if (!fixtureBase.isRecurring()) dismissAlarm(fixtureBase);
//                else dismissAlarmRecurring(fixtureBase);
                fixtureBase = null;
            }
            fixtureBase = (FixtureBase) bundle.getSerializable(getString(R.string.arg_alarm_obj));
            if (fixtureBase != null) {
                Log.d("alarmcheck", "service" + fixtureBase);
                String alarmTitle = fixtureBase.name;
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(alarmTitle)
                        .setContentText(alarmTitle)
                        .setSmallIcon(R.drawable.img_logo)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .build();
                startForeground(1, notification);
            }
        }
        return START_STICKY;
    }
}
