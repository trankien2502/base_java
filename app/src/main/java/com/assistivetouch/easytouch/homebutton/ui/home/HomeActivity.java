package com.assistivetouch.easytouch.homebutton.ui.home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.dialog.exit.ExitAppDialog;
import com.assistivetouch.easytouch.homebutton.dialog.exit.IClickDialogExit;
import com.assistivetouch.easytouch.homebutton.dialog.rate.IClickDialogRate;
import com.assistivetouch.easytouch.homebutton.dialog.rate.RatingDialog;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.CustomMenuActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.icon.FloatingIconActivity;
import com.assistivetouch.easytouch.homebutton.ui.setting.SettingActivity;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
//import com.google.android.gms.tasks.Task;
//import com.google.android.play.core.review.ReviewInfo;
//import com.google.android.play.core.review.ReviewManager;
//import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {


    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));


    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
        binding.swTouch.setChecked(isMyServiceRunning(ServiceScreen.class));
    }

    @Override
    public void bindView() {
        binding.ivSetting.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, SettingActivity.class));
        });
        binding.swTouch.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showDialogGotoSetting(2);
                    binding.swTouch.setChecked(false);
                } else {
                    if (binding.swTouch.isChecked()) {
                        Intent serviceIntent = new Intent(this, ServiceScreen.class);
                        startService(serviceIntent);
                        Toast.makeText(this, R.string.enable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.disable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                        Intent serviceIntent = new Intent(this, ServiceScreen.class);
                        stopService(serviceIntent);
                    }
                }
            } else {
                if (binding.swTouch.isChecked()) {
                    Intent serviceIntent = new Intent(this, ServiceScreen.class);
                    startService(serviceIntent);
                    Toast.makeText(this, R.string.enable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.disable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                    Intent serviceIntent = new Intent(this, ServiceScreen.class);
                    stopService(serviceIntent);
                }
            }
        });
        binding.clMenuTouch.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, CustomMenuActivity.class));
        });
        binding.clIconTouch.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, FloatingIconActivity.class));
        });
        binding.swVolume.setOnClickListener(view -> {
            if (!PermissionManager.checkOverlayPermission(this)) {
                showDialogGotoSetting(2);
            } else {
                requestAccessibilityPermission();
            }
        });
        binding.clVolumeConfig.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, SettingActivity.class));
        });
        binding.clButtonVolume.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, SettingActivity.class));
        });
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void requestAccessibilityPermission() {
        if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Log.e("check_service", "off");
        } else {
            if (ServiceControl.instance != null) {
                ServiceControl service = new ServiceControl();
                service.turnOffScreen();
                Log.e("check_service", "on");
            } else {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Log.e("check_service", "null");
            }
        }
    }

    @Override
    public void onBack() {
        if (!SharePrefUtils.isRated(this)) {
            if (exitRate.contains(String.valueOf(SharePrefUtils.getCountOpenApp(this)))) {
                rateApp();
            } else {
                exitApp();
            }
        } else {
            exitApp();
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    private void rateApp() {
        RatingDialog ratingDialog = new RatingDialog(HomeActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                //binding.rlRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    finishAffinity();
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(HomeActivity.this);
                    int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(HomeActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(HomeActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            //binding.rlRate.setVisibility(View.GONE);
                            int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            SharePrefUtils.forceRated(HomeActivity.this);
                            ratingDialog.dismiss();
                            finishAffinity();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(HomeActivity.this, "rate_not_now");
                ratingDialog.dismiss();
                finishAffinity();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this, "rate_show");
    }

    private void showDialogGotoSetting(int type) {
        GoToSettingDialog dialog = new GoToSettingDialog(this, true);
        SystemUtil.setLocale(this);

        if (type == 1) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_noti);
        } else if (type == 2) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_overlay);
        }

        dialog.binding.tvStay.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvContent.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvAgree.setOnClickListener(view -> {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(HomeActivity.class);
            dialog.dismiss();
            if (type == 1) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                resultLauncher.launch(intent);
            } else if (type == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        resultLauncher.launch(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PermissionError", "Error opening settings: " + e.getMessage());
                    }

                }
            }
        });
        dialog.show();
    }

    private void exitApp() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this, true);
        exitAppDialog.init(new IClickDialogExit() {
            @Override
            public void cancel() {
                exitAppDialog.dismiss();
            }

            @Override
            public void quit() {
                exitAppDialog.dismiss();
                finishAffinity();
            }
        });

        try {
            exitAppDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
