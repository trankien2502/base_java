package com.assistivetouch.easytouch.homebutton.ui.home;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.dialog.TestDialog;
import com.assistivetouch.easytouch.homebutton.dialog.exit.ExitAppDialog;
import com.assistivetouch.easytouch.homebutton.dialog.exit.IClickDialogExit;
import com.assistivetouch.easytouch.homebutton.dialog.rate.IClickDialogRate;
import com.assistivetouch.easytouch.homebutton.dialog.rate.RatingDialog;
import com.assistivetouch.easytouch.homebutton.service.MyDeviceAdminReceiver;
import com.assistivetouch.easytouch.homebutton.service.ScreenRecordService;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.CustomMenuActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.icon.FloatingIconActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.volume.ButtonStyleActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.volume.VolumeConfigActivity;
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
    public static HomeActivity instance;

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
    }

    public void checkState() {
        if (!isMyServiceRunning(ServiceScreen.class)) {
            binding.swTouch.setChecked(false);
            binding.swVolume.setChecked(false);
        } else {
            if (ServiceScreen.instance == null) {
                binding.swTouch.setChecked(false);
                binding.swVolume.setChecked(false);
            } else {
                binding.swTouch.setChecked(ServiceScreen.instance.floatingView != null);
                binding.swVolume.setChecked(ServiceScreen.instance.volumeView != null);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
    }

    @Override
    public void bindView() {
        binding.ivSetting.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, SettingActivity.class));
            EventTracking.logEvent(this, "home_setting_click");
//            resultLauncher.launch(new Intent(this, ScreenRecorderActivity.class));
        });
        binding.swTouch.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_enable_touch_click");
            enableTouch();
//            Intent serviceIntent = new Intent(this, ScreenRecordService.class);
//            stopService(serviceIntent);
        });
        binding.clMenuTouch.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_custom_menu_click");
            resultLauncher.launch(new Intent(this, CustomMenuActivity.class));
        });
        binding.clIconTouch.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_floating_icon_click");
            resultLauncher.launch(new Intent(this, FloatingIconActivity.class));
        });
        binding.swVolume.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_enable_volume_click");
            enableVolume();
        });
        binding.clVolumeConfig.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_volume_config_click");
            resultLauncher.launch(new Intent(this, VolumeConfigActivity.class));
        });
        binding.clButtonVolume.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_button_style_click");
            resultLauncher.launch(new Intent(this, ButtonStyleActivity.class));
        });
    }

    private void enableTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showDialogGotoSetting(2);
                binding.swTouch.setChecked(false);
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    if (!checkAdviceAdmin())
                        showDialogGotoSetting(7);
                    else {
                        if (binding.swTouch.isChecked()) {
                            if (ServiceScreen.instance != null && isMyServiceRunning(ServiceScreen.class)) {
                                ServiceScreen.instance.addFloatingIcon();
                            } else {
                                Intent serviceIntent = new Intent(this, ServiceScreen.class);
                                serviceIntent.putExtra("IS_ADD_TOUCH_ICON", true);
                                startService(serviceIntent);
                            }
                            Toast.makeText(this, R.string.enable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                        } else {
                            if (ServiceScreen.instance != null) {
                                if (ServiceScreen.instance.floatingView != null)
                                    ServiceScreen.instance.removeFloatingView();
                                if (ServiceScreen.instance.floatingView == null && ServiceScreen.instance.volumeView == null) {
                                    Intent serviceIntent = new Intent(this, ServiceScreen.class);
                                    stopService(serviceIntent);
                                }
                            }
                            Toast.makeText(this, R.string.disable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (binding.swTouch.isChecked()) {
                        if (ServiceScreen.instance != null && isMyServiceRunning(ServiceScreen.class)) {
                            ServiceScreen.instance.addFloatingIcon();
                        } else {
                            Intent serviceIntent = new Intent(this, ServiceScreen.class);
                            serviceIntent.putExtra("IS_ADD_TOUCH_ICON", true);
                            startService(serviceIntent);
                        }
                        Toast.makeText(this, R.string.enable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                    } else {
                        if (ServiceScreen.instance != null) {
                            if (ServiceScreen.instance.floatingView != null)
                                ServiceScreen.instance.removeFloatingView();
                            if (ServiceScreen.instance.floatingView == null && ServiceScreen.instance.volumeView == null) {
                                Intent serviceIntent = new Intent(this, ServiceScreen.class);
                                stopService(serviceIntent);
                            }
                        }
                        Toast.makeText(this, R.string.disable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            if (!checkAdviceAdmin())
                showDialogGotoSetting(7);
            else {
                if (binding.swTouch.isChecked()) {
                    if (ServiceScreen.instance != null && isMyServiceRunning(ServiceScreen.class)) {
                        ServiceScreen.instance.addFloatingIcon();
                    } else {
                        Intent serviceIntent = new Intent(this, ServiceScreen.class);
                        serviceIntent.putExtra("IS_ADD_TOUCH_ICON", true);
                        startService(serviceIntent);
                    }
                    Toast.makeText(this, R.string.enable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                } else {
                    if (ServiceScreen.instance != null) {
                        if (ServiceScreen.instance.floatingView != null)
                            ServiceScreen.instance.removeFloatingView();
                        if (ServiceScreen.instance.floatingView == null && ServiceScreen.instance.volumeView == null) {
                            Intent serviceIntent = new Intent(this, ServiceScreen.class);
                            stopService(serviceIntent);
                        }
                    }
                    Toast.makeText(this, R.string.disable_assistive_touch_success, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void enableVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showDialogGotoSetting(2);
                binding.swVolume.setChecked(false);
            } else {
                if (binding.swVolume.isChecked()) {
                    if (ServiceScreen.instance != null && isMyServiceRunning(ServiceScreen.class)) {
                        ServiceScreen.instance.addVolumeIcon();
                    } else {
                        Intent serviceIntent = new Intent(this, ServiceScreen.class);
                        serviceIntent.putExtra("IS_ADD_TOUCH_ICON", false);
                        startService(serviceIntent);
                    }
                    Toast.makeText(this, R.string.enable_assistive_volume_success, Toast.LENGTH_SHORT).show();
                } else {
                    if (ServiceScreen.instance != null) {
                        if (ServiceScreen.instance.volumeView != null)
                            ServiceScreen.instance.removeVolumeView();
                        if (ServiceScreen.instance.floatingView == null && ServiceScreen.instance.volumeView == null) {
                            Intent serviceIntent = new Intent(this, ServiceScreen.class);
                            stopService(serviceIntent);
                        }
                    }
                    Toast.makeText(this, R.string.disable_assistive_volume_success, Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            if (binding.swVolume.isChecked()) {
                if (ServiceScreen.instance != null && isMyServiceRunning(ServiceScreen.class)) {
                    ServiceScreen.instance.addVolumeIcon();
                } else {
                    Intent serviceIntent = new Intent(this, ServiceScreen.class);
                    serviceIntent.putExtra("IS_ADD_TOUCH_ICON", false);
                    startService(serviceIntent);
                }
                Toast.makeText(this, R.string.enable_assistive_volume_success, Toast.LENGTH_SHORT).show();
            } else {
                if (ServiceScreen.instance != null) {
                    if (ServiceScreen.instance.volumeView != null)
                        ServiceScreen.instance.removeVolumeView();
                    if (ServiceScreen.instance.floatingView == null && ServiceScreen.instance.volumeView == null) {
                        Intent serviceIntent = new Intent(this, ServiceScreen.class);
                        stopService(serviceIntent);
                    }
                }
                Toast.makeText(this, R.string.disable_assistive_volume_success, Toast.LENGTH_SHORT).show();

            }
        }
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

    private boolean checkAdviceAdmin() {
        ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(componentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1100 && resultCode == RESULT_OK) {
            Toast.makeText(instance, "enable", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);

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
        instance = null;
    }

    private void showDialogGotoSetting(int type) {
        GoToSettingDialog dialog = new GoToSettingDialog(this, true);
        SystemUtil.setLocale(this);

        if (type == 1) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_noti);
        } else if (type == 2) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_overlay);
        } else if (type == 3) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_write_setting);
        } else if (type == 4) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_accessibility);
        } else if (type == 5) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_camera);
        } else if (type == 6) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_storage);
        }else if (type == 7) {
            dialog.binding.tvContent.setText(R.string.you_need_to_enable_device_admin_feature);
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
            if (type == 1 || type == 5 || type == 6) {
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
            } else if (type == 3) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else if (type == 4) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Log.e("check_service", "off");
            }else if (type == 7) {
                ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.allow_assistive_touch_to_lock_screen));
                startActivity(intent);
            }
        });
        dialog.show();
    }
}
