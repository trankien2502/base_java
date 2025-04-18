package com.tkt.basejava.basejava1.basejava2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.tkt.basejava.basejava1.basejava2.util.SharePrefUtils;


public class MyApplication extends Application {

    public static final String CHANNEL_ID_SERVICE = "channel_service";
    public static final String CHANNEL_ID_GENERAL = "channel_general";

    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID_SERVICE,
                    "Foreground Service Live Score",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationChannel generalChannel = new NotificationChannel(
                    CHANNEL_ID_GENERAL,
                    "General notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
                manager.createNotificationChannel(generalChannel);
            }
        }
    }
}

