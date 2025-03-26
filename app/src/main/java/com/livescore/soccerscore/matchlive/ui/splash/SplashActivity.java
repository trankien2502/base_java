package com.livescore.soccerscore.matchlive.ui.splash;

import android.os.Handler;
import android.util.Log;

import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.CallApiUtils;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.ui.language.LanguageStartActivity;
import com.livescore.soccerscore.matchlive.util.SharePrefUtils;
import com.livescore.soccerscore.matchlive.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IsNetWork.haveNetworkConnection(this)) {
            CallApiUtils.fetchFixtureDatePage("2006-03-25",1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
        if (IsNetWork.haveNetworkConnection(this)){
//            CallApiUtils.callDataLeague(this);
//            CallApiUtils.callDataTeam(this);
            new Handler().postDelayed(() -> {
                startNextActivity(LanguageStartActivity.class, null);
                finishAffinity();
            }, 3000);
        }
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {

    }
}
