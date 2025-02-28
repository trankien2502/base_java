package com.tkt.basejava.basejava1.basejava2;

import android.app.Application;

import com.tkt.basejava.basejava1.basejava2.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

