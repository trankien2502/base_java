package com.livescore.soccerscore.matchlive;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.livescore.soccerscore.matchlive.util.SharePrefUtils;


public class MyApplication extends Application {

    public static final String CHANNEL_ID = "live_score_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Live Score",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}

