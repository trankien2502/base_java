package com.assistivetouch.easytouch.homebutton;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;


public class MyApplication extends Application {
    public static final String CHANNEL_ID = "assistive_touch_channel";

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
                    "Foreground Service Assistive Touch",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}

