package com.assistivetouch.easytouch.homebutton.ui.permission;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityPermissionBinding;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.jetbrains.annotations.Nullable;


public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 120;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 130;
    private int countCamera = 0;
    private int countNotification = 0;
    Handler handler = new Handler();
    Runnable runnableNativeAds;

    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativePermissionAds();
        EventTracking.logEvent(this, "permission_open");
        countCamera = SPUtils.getInt(this, SPUtils.CAMERA, 0);
        countNotification = SPUtils.getInt(this, SPUtils.NOTIFICATION, 0);
    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {
            EventTracking.logEvent(this, "permission_continue_click");
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
        binding.swPerNotification.setOnClickListener(view -> {
            if (!PermissionManager.checkNotificationPermission(this)) {
                EventTracking.logEvent(this, "permission_allow_click");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION_PERMISSION);
                }
            }
        });
        binding.swPer.setOnClickListener(view -> {
            if (!PermissionManager.checkOverlayPermission(this)) {
                EventTracking.logEvent(this, "permission_allow_click");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        //                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
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
        binding.swAccessibility.setOnClickListener(v -> {
            if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                EventTracking.logEvent(this, "permission_allow_click");
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Log.e("check_service", "off");
            }
        });
        binding.swWriteSetting.setOnClickListener(v -> {
            if (!CheckUtils.checkSystemWriteSetting(this)) {
                EventTracking.logEvent(this, "permission_allow_click");
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSwOverlay();
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkSwOverlay();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        countCamera++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
                        SPUtils.setInt(this, SPUtils.CAMERA, countCamera);
                        if (countCamera > 1) {
                            showDialogGotoSetting(2);
                        }
                    }

                }
            }
        }
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSwNotification();
            }

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkSwNotification();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        countNotification++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
                        SPUtils.setInt(this, SPUtils.NOTIFICATION, countNotification);
                        if (countNotification > 1) {
                            showDialogGotoSetting(1);
                        }
                    }
                }
            }
        }
    }

    public void loadNativePermissionAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativePermission.isEmpty() && ConstantRemote.native_permission) {
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativePermissionAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(PermissionActivity.this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativePermission.removeAllViews();
                binding.nativePermission.addView(adViewLoad);
                binding.nativePermission.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativePermission, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(PermissionActivity.this).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativePermission.removeAllViews();
                                binding.nativePermission.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.PE);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativePermission.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativePermission.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativePermission.setVisibility(View.GONE);
        }
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
//            AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
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

    @Override
    public void onBack() {
        finishAffinity();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void checkSwOverlay() {
        if (PermissionManager.checkOverlayPermission(this)) {
            binding.swPer.setChecked(true);
            binding.swPer.setOnTouchListener((view, motionEvent) -> true);
        } else {
            binding.swPer.setChecked(false);
            binding.swPer.setOnTouchListener((view, motionEvent) -> false);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void checkSwNotification() {
        if (PermissionManager.checkNotificationPermission(this)) {
            binding.swPerNotification.setChecked(true);
            binding.swPerNotification.setOnTouchListener((view, motionEvent) -> true);
        } else {
            binding.swPerNotification.setChecked(false);
            binding.swPerNotification.setOnTouchListener((view, motionEvent) -> false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void checkSwWriteSetting() {
        if (CheckUtils.checkSystemWriteSetting(this)) {
            binding.swWriteSetting.setChecked(true);
            binding.swWriteSetting.setOnTouchListener((view, motionEvent) -> true);
        } else {
            binding.swWriteSetting.setChecked(false);
            binding.swWriteSetting.setOnTouchListener((view, motionEvent) -> false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void checkSwAccessibility() {
        if (CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
            binding.swAccessibility.setChecked(true);
            binding.swAccessibility.setOnTouchListener((view, motionEvent) -> true);
        } else {
            binding.swAccessibility.setChecked(false);
            binding.swAccessibility.setOnTouchListener((view, motionEvent) -> false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSwNotification();
        checkSwOverlay();
        checkSwAccessibility();
        checkSwWriteSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
