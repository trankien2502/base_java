package com.assistivetouch.easytouch.homebutton.ui.splash;

import android.os.Handler;

import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.ui.language.LanguageStartActivity;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;
import com.assistivetouch.easytouch.homebutton.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);
        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {

    }
}
