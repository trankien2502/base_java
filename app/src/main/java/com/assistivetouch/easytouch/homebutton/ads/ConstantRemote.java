package com.assistivetouch.easytouch.homebutton.ads;

import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Arrays;

import com.assistivetouch.easytouch.homebutton.R;

public class ConstantRemote {
    public static boolean banner_splash = true;
    public static boolean open_splash = true;
    public static boolean inter_splash = true;
    public static boolean native_language = true;
    public static boolean native_intro = true;
    public static boolean native_intro_full = true;
    public static boolean inter_intro = true;
    public static boolean native_permission = true;
    public static boolean resume = true;
    public static boolean native_resume = true;
    public static boolean banner_all = true;
    public static boolean native_popup = true;
    public static boolean collapse_home = true;
    public static boolean inter_home = true;
    public static boolean native_home = true;
    public static boolean native_menu = true;
    public static boolean inter_menu = true;
    public static boolean native_floating = true;
    public static boolean inter_floating = true;
    public static boolean native_volume = true;
    public static boolean native_button = true;
    public static boolean inter_button = true;
    public static boolean show_ads = true;

    public static boolean show_ump = true;
    public static long interval_interstitial_from_start = 5;//5;
    public static long collap_reload_interval = 20;//20;
    public static long time_native_reload  = 10;//10;
    public static long interval_interstitial_from_start_old = 0;
    public static long interval_between_interstitial = 8;//8
    public static long time_interval_old = 0;
    public static ArrayList<String> rate_aoa_inter_splash = new ArrayList<>(Arrays.asList("1", "99"));

    public static void initRemoteConfig(OnCompleteListener listener) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.reset();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build();
        new Handler().postDelayed(() -> {
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(listener);
        }, 2000);
    }


    public static boolean getRemoteConfigBoolean(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getBoolean(adUnitId);
    }

    public static ArrayList<String> getRemoteConfigString(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split(",");
        return new ArrayList<>(Arrays.asList(arStr));
    }

    public static ArrayList<String> getRemoteConfigOpenSplash(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split("_");
        return new ArrayList<>(Arrays.asList(arStr));
    }

    public static long getRemoteConfigLong(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getLong(adUnitId);
    }
}
