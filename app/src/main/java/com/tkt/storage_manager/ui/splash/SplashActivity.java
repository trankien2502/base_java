package com.tkt.storage_manager.ui.splash;

import android.os.Handler;

import com.tkt.storage_manager.base.BaseActivity;
import com.tkt.storage_manager.ui.language.LanguageStartActivity;
import com.tkt.storage_manager.util.SPUtils;
import com.tkt.storage_manager.util.SharePrefUtils;
import com.tkt.storage_manager.R;
import com.tkt.storage_manager.databinding.ActivitySplashBinding;


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
    public void onBackPressed() {
    }


}
