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
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ads.sapp.ads.CommonAd;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private boolean isStartActivity = true;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isPaused = false;
    private boolean isResume = false;
    private boolean isReloadAds = true;
    private ExecutorService executorService;

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
        if (isReloadAds) {
            isReloadAds = false;
            timeLeftInMillis = ConstantRemote.collap_reload_interval * 1000L;
            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                if (IsNetWork.haveNetworkConnection(this)
                        && !ConstantIdAds.listIDAdsCollapseHome.isEmpty()
                        && ConstantRemote.collapse_home) {

                    runOnUiThread(() -> {
                        CommonAd.getInstance().loadCollapsibleBannerFloor(this,
                                ConstantIdAds.listIDAdsCollapseHome, "bottom");
                        isReloadAds = true;
                        startTimer(ConstantRemote.collap_reload_interval * 1000L);
                        binding.rlBanner.setVisibility(View.VISIBLE);
                        executorService.shutdown();
                    });
                } else {
                    runOnUiThread(() -> {
                        isReloadAds = true;
                        binding.rlBanner.setVisibility(View.GONE);
                        binding.rlBanner.removeAllViews();
                    });
                }
            });
        }
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
        exitApp();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

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
    private void onReloadBannerCollapse() {
        resetTimer();
        if (isReloadAds) {
            isReloadAds = false;
            reloadBannerCo();
        }
    }

    public void reloadBannerCo() {
        binding.rlBanner.removeAllViews();
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.ads.sapp.R.layout.layout_banner_control, null, false);
        binding.rlBanner.addView(layout);

        new Thread(() -> runOnUiThread(() -> {
            if (IsNetWork.haveNetworkConnection(this) && !ConstantIdAds.listIDAdsCollapseHome.isEmpty() && ConstantRemote.collapse_home) {
                CommonAd.getInstance().loadCollapsibleBannerFloor(this, ConstantIdAds.listIDAdsCollapseHome, "bottom");
                binding.rlBanner.setVisibility(View.VISIBLE);
                isReloadAds = true;
                startTimer(ConstantRemote.collap_reload_interval * 1000L);
            } else {
                binding.rlBanner.setVisibility(View.GONE);
                isReloadAds = true;
            }
        })).start();
    }

    private void startTimer(long millis) {
        if (ConstantRemote.collap_reload_interval > 0) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsCollapseHome.isEmpty() && ConstantRemote.collapse_home) {
                countDownTimer = new CountDownTimer(millis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMillis = millisUntilFinished;
                        if (!IsNetWork.haveNetworkConnection(
                                HomeActivity.this)) {
                            cancel();
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (isReloadAds) {
                            isReloadAds = false;
                            reloadBannerCo();
                        }
                    }
                }.start();
            }
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = ConstantRemote.collap_reload_interval * 1000L;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState();
        if (isResume) {
            if (isStartActivity) {
                if (isPaused) {
                    startTimer(timeLeftInMillis);
                    isPaused = false;
                }
            }
        }

        if (!isResume) {
            isResume = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStartActivity) {
            Log.e("sdklfl0", "1");
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
                isPaused = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        if (executorService != null) {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

}
