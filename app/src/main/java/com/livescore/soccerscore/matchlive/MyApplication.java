package com.livescore.soccerscore.matchlive;

import android.app.Application;

import com.livescore.soccerscore.matchlive.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

