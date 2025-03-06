package com.assistivetouch.easytouch.homebutton;

import android.app.Application;

import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

