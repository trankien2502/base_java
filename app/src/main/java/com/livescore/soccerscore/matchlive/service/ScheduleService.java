package com.livescore.soccerscore.matchlive.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.livescore.soccerscore.matchlive.MyApplication.CHANNEL_ID_SERVICE;

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

import com.livescore.soccerscore.matchlive.MyApplication;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.ui.splash.SplashActivity;
import com.livescore.soccerscore.matchlive.util.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleService extends Service {
    public List<FixtureModel> listFixture;
    public static ScheduleService instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        listFixture = new ArrayList<>();
        startForeground(1, createNotification());
    }

    private Notification createNotification() {
        SystemUtil.setLocale(this);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
                .setContentTitle(getString(R.string.asisitive_touch_s_service_is_running))
                .setContentText(getString(R.string.tap_to_open))
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle != null) {
            FixtureModel fixtureModel = (FixtureModel) bundle.getSerializable(getString(R.string.arg_alarm_obj));
            listFixture.add(fixtureModel);

            if (fixtureModel != null) {
                Log.d("alarmcheck", "service" + listFixture);
            }
        }
        return START_STICKY;
    }
}
