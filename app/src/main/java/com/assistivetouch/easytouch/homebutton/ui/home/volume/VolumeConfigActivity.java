package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityVolumeConfigBinding;
import com.assistivetouch.easytouch.homebutton.dialog.ChooseActionDialog;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.service.MyDeviceAdminReceiver;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class VolumeConfigActivity extends BaseActivity<ActivityVolumeConfigBinding> {

    ChooseActionDialog dialog;
    Handler handler = new Handler();
    Runnable runnableNativeAds;
    Runnable runnableNativeDialogAds;
    GoToSettingDialog permissionDialog;

    @Override
    public ActivityVolumeConfigBinding getBinding() {
        return ActivityVolumeConfigBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativeVolumeAds();
        EventTracking.logEvent(getBaseContext(), "volume_config_view");
        changeState();
        switch (SPUtils.getInt(getBaseContext(), SPUtils.LONG_PRESS_VOLUME_ACTION, 1)) {
            case 1:
                binding.tvAction.setText(R.string.hide_button);
                break;
            case 2:
                binding.tvAction.setText(R.string.screen_off);
                break;
            case 3:
                binding.tvAction.setText(R.string.open_notification);
                break;
            case 4:
                binding.tvAction.setText(R.string.mute_media_volume);
                break;
        }
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {

            onBack();
        });
        binding.llHideButton.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_hide_button_click");
            showChooseActionDialog();
        });
        binding.clMedia.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
            if (SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)) {
                if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true) || SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true)
                        || SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false) || SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true) || SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                    SPUtils.setBoolean(this, SPUtils.SHOW_MEDIA, false);
                    changeState();
                } else
                    Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_MEDIA, true);
                changeState();
            }
        });
        binding.clRing.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
            if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true)) {
                if (SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true) || SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)
                        || SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false) || SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true) || SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                    SPUtils.setBoolean(this, SPUtils.SHOW_RINGTONE, false);
                    changeState();
                } else
                    Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_RINGTONE, true);
                changeState();
            }
        });
        binding.clNotification.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
            if (SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true)) {
                if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true) || SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)
                        || SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false) || SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true) || SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                    SPUtils.setBoolean(this, SPUtils.SHOW_NOTIFICATION, false);
                    changeState();
                } else
                    Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_NOTIFICATION, true);
                changeState();
            }
        });
        binding.clCall.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
            if (SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true)) {
                if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true) || SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true) || SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)
                        || SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false) || SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                    SPUtils.setBoolean(this, SPUtils.SHOW_CALL, false);
                    changeState();
                } else
                    Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_CALL, true);
                changeState();
            }
        });
        binding.clBrightness.setOnClickListener(v -> {
            if (!CheckUtils.checkSystemWriteSetting(this)) {
                showDialogGotoSetting(3);
            } else {
                EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
                if (SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false)) {
                    if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true) || SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true) || SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)
                            || SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true) || SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                        SPUtils.setBoolean(this, SPUtils.SHOW_BRIGHTNESS, false);
                        changeState();
                    } else
                        Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
                } else {
                    SPUtils.setBoolean(this, SPUtils.SHOW_BRIGHTNESS, true);
                    changeState();
                }
            }
        });
        binding.clDark.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_item_click");
            if (SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
                if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true) || SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true) || SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)
                        || SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false) || SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true)) {
                    SPUtils.setBoolean(this, SPUtils.SHOW_DARKNESS, false);
                    changeState();
                } else
                    Toast.makeText(this, R.string.you_must_choose_at_least_1_volume_option, Toast.LENGTH_SHORT).show();
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_DARKNESS, true);
                changeState();
            }
        });
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "volume_config_back_click");
        setResult(RESULT_OK);
        finish();
    }

    private void changeState() {
        if (SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)) {
            binding.clMedia.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt1.setVisibility(VISIBLE);
        } else {
            binding.clMedia.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt1.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true)) {
            binding.clRing.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt2.setVisibility(VISIBLE);
        } else {
            binding.clRing.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt2.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true)) {
            binding.clNotification.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt3.setVisibility(VISIBLE);
        } else {
            binding.clNotification.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt3.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true)) {
            binding.clCall.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt4.setVisibility(VISIBLE);
        } else {
            binding.clCall.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt4.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false)) {
            binding.clBrightness.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt5.setVisibility(VISIBLE);
        } else {
            binding.clBrightness.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt5.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
            binding.clDark.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt6.setVisibility(VISIBLE);
        } else {
            binding.clDark.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt6.setVisibility(INVISIBLE);
        }
    }

    private void showChooseActionDialog() {
        dialog = new ChooseActionDialog(this);
        int x = SPUtils.getInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 1);
        dialog.binding.llHideButton.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 1);
            changeStateAction(1);
        });
        dialog.binding.llScreenOff.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 28) {
                if (!CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
                    showDialogGotoSetting(4);
                } else {
                    SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 2);
                    changeStateAction(2);
                }
            } else {
                if (!checkAdviceAdmin()) {
                    showDialogGotoSetting(7);
                } else {
                    SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 2);
                    changeStateAction(2);
                }
            }
        });
        dialog.binding.llOpenNotification.setOnClickListener(v -> {
            if (!CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
                showDialogGotoSetting(4);
            } else {
                SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 3);
                changeStateAction(3);
            }
        });
        dialog.binding.llMuteMedia.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 4);
            changeStateAction(4);
        });
        dialog.show();
        loadNativePopupAds();
        changeStateAction(x);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnableNativeDialogAds);
                loadNativeVolumeAds();
                switch (SPUtils.getInt(getBaseContext(), SPUtils.LONG_PRESS_VOLUME_ACTION, 1)) {
                    case 1:
                        binding.tvAction.setText(R.string.hide_button);
                        break;
                    case 2:
                        binding.tvAction.setText(R.string.screen_off);
                        break;
                    case 3:
                        binding.tvAction.setText(R.string.open_notification);
                        break;
                    case 4:
                        binding.tvAction.setText(R.string.mute_media_volume);
                        break;
                }

            }
        });
    }
    public void loadNativePopupAds() {
        if (dialog!=null && dialog.isShowing()){
            try {
                if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativePopup.isEmpty() && ConstantRemote.native_popup && ConstantRemote.show_ads) {
                    handler.removeCallbacks(runnableNativeDialogAds);
                    runnableNativeDialogAds = new Runnable() {
                        @Override
                        public void run() {
                            loadNativePopupAds();
                        }
                    };
                    handler.removeCallbacks(runnableNativeAds);
                    @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                    dialog.binding.nativePopup.removeAllViews();
                    dialog.binding.nativePopup.addView(adViewLoad);
                    dialog.binding.nativePopup.setVisibility(View.VISIBLE);
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativePopup, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                            dialog.binding.nativePopup.removeAllViews();
                            dialog.binding.nativePopup.addView(adView);
                            Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            if (ConstantRemote.time_native_reload != 0)
                                handler.postDelayed(runnableNativeDialogAds, ConstantRemote.time_native_reload * 1000);
                            CheckAds.getInstance().checkAds(adView, CheckAds.OT);

                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            dialog.binding.nativePopup.setVisibility(View.GONE);
                        }
                    });

                } else {
                    dialog.binding.nativePopup.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                dialog.binding.nativePopup.setVisibility(View.GONE);
            }
        }
        
    }
    private boolean checkAdviceAdmin() {
        ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(componentName);
    }

    private void changeStateAction(int i) {
        if (dialog != null && dialog.isShowing()) {
            resetChange();
            switch (i) {
                case 1:
                    dialog.binding.ivHideButton.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 2:
                    dialog.binding.ivScreenOff.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 3:
                    dialog.binding.ivOpenNotification.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 4:
                    dialog.binding.ivMuteMedia.setImageResource(R.drawable.radio_volume_s);
                    break;
            }

        }

    }

    private void resetChange() {
        dialog.binding.ivHideButton.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivOpenNotification.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivScreenOff.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivMuteMedia.setImageResource(R.drawable.radio_volume_sn);
    }
    public void loadNativePopupPermissionAds() {
        if (permissionDialog!=null && permissionDialog.isShowing()){
            try {
                if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativePopup.isEmpty() && ConstantRemote.native_popup && ConstantRemote.show_ads) {
                    handler.removeCallbacks(runnableNativeDialogAds);
                    runnableNativeDialogAds = new Runnable() {
                        @Override
                        public void run() {
                            loadNativePopupPermissionAds();
                        }
                    };
                    @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                    permissionDialog.binding.nativePopup.removeAllViews();
                    permissionDialog.binding.nativePopup.addView(adViewLoad);
                    permissionDialog.binding.nativePopup.setVisibility(View.VISIBLE);
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativePopup, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                            permissionDialog.binding.nativePopup.removeAllViews();
                            permissionDialog.binding.nativePopup.addView(adView);
                            Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            if (ConstantRemote.time_native_reload != 0)
                                handler.postDelayed(runnableNativeDialogAds, ConstantRemote.time_native_reload * 1000);
                            CheckAds.getInstance().checkAds(adView, CheckAds.OT);

                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            permissionDialog.binding.nativePopup.setVisibility(View.GONE);
                        }
                    });

                } else {
                    permissionDialog.binding.nativePopup.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                permissionDialog.binding.nativePopup.setVisibility(View.GONE);
            }
        }

    }
    private void showDialogGotoSetting(int type) {
        permissionDialog = new GoToSettingDialog(this, true);
        SystemUtil.setLocale(this);

        if (type == 1) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_noti);
        } else if (type == 2) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_overlay);
        } else if (type == 3) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_write_setting);
        } else if (type == 4) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_accessibility);
        } else if (type == 5) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_camera);
        } else if (type == 6) {
            permissionDialog.binding.tvContent.setText(R.string.content_dialog_per_storage);
        } else if (type == 7) {
            permissionDialog.binding.tvContent.setText(R.string.you_need_to_enable_device_admin_feature);
        }

        permissionDialog.binding.tvStay.setOnClickListener(view -> {
            permissionDialog.dismiss();
        });
        permissionDialog.binding.tvContent.setOnClickListener(view -> {
            permissionDialog.dismiss();
        });
        permissionDialog.binding.tvAgree.setOnClickListener(view -> {
            AppOpenManager.getInstance().disableAppResumeWithActivity(VolumeConfigActivity.class);
            permissionDialog.dismiss();
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
                resultLauncher.launch(intent);
            } else if (type == 4) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                resultLauncher.launch(intent);
                Log.e("check_service", "off");
            } else if (type == 7) {
                ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.allow_assistive_touch_to_lock_screen));
                resultLauncher.launch(intent);
            }
        });
        permissionDialog.show();
        loadNativePopupPermissionAds();
        permissionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnableNativeDialogAds);
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    public void loadNativeVolumeAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeVolume.isEmpty() && ConstantRemote.native_volume  && ConstantRemote.show_ads) {
                handler.removeCallbacks(runnableNativeAds);
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativeVolumeAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativeVolume.removeAllViews();
                binding.nativeVolume.addView(adViewLoad);
                binding.nativeVolume.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeVolume, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativeVolume.removeAllViews();
                                binding.nativeVolume.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativeVolume.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativeVolume.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeVolume.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
    }
}