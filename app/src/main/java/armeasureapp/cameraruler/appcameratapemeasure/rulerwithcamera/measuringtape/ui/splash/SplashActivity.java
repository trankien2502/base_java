package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.splash;

import android.os.Handler;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.language.LanguageStartActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SPUtils;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SharePrefUtils;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivitySplashBinding;


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
