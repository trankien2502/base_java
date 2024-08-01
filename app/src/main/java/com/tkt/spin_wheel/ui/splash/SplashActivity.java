package com.tkt.spin_wheel.ui.splash;

import android.os.Handler;

import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.language.LanguageStartActivity;
import com.tkt.spin_wheel.util.SPUtils;
import com.tkt.spin_wheel.util.SharePrefUtils;
import com.tkt.spin_wheel.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);
        SPUtils.setInt(this,SPUtils.GHOST_TYPE,0);
        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBackPressed() {
    }
}
