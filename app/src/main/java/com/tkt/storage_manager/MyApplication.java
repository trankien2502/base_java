package com.tkt.storage_manager;

import android.app.Application;

import com.tkt.storage_manager.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

