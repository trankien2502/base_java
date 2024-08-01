package com.tkt.spin_wheel;

import android.app.Application;

import com.tkt.spin_wheel.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

