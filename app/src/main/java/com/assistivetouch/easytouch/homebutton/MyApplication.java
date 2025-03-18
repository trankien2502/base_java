package com.assistivetouch.easytouch.homebutton;

import static com.facebook.FacebookSdk.sdkInitialize;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.application.AdsMultiDexApplication;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ui.splash.SplashActivity;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;


public class MyApplication extends AdsMultiDexApplication {
    public static final String CHANNEL_ID = "assistive_touch_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);
        createNotificationChannel();
        ConstantRemote.initRemoteConfig(task -> {
            if (task.isSuccessful()) {
                ConstantRemote.show_ump = ConstantRemote.getRemoteConfigBoolean("show_ump");
            }
        });
        sdkInitialize(getApplicationContext());

        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        Admob.getInstance().setNumToShowAds(0);
    }

    public void initAds() {
        commonAdConfig.setMediationProvider(CommonAdConfig.PROVIDER_ADMOB);
        commonAdConfig.setVariant(true);
        commonAdConfig.setIdAdResume(getString(R.string.resume));
        commonAdConfig.setListDeviceTest(listTestDevice);
        commonAdConfig.setMediationFloor(CommonAdConfig.WARTER_FALL);

        CommonAd.getInstance().init(this, commonAdConfig, false);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
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

