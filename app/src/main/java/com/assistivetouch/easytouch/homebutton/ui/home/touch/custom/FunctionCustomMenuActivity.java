package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFunctionCustomMenuBinding;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionCallBack;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIconAdapter;
import com.assistivetouch.easytouch.homebutton.service.MyDeviceAdminReceiver;
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

import java.util.ArrayList;
import java.util.List;

public class FunctionCustomMenuActivity extends BaseActivity<ActivityFunctionCustomMenuBinding> {

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 20;
    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    List<ItemFunctionIcon> functionSelectedList = new ArrayList<>();

    ItemFunctionIconAdapter adapter, adapterSelect;
    ItemFunctionIcon currentSelectItem = null;
    int menuFunction;
    int menuPosition;
    boolean isAvailable = true;
    Handler handler = new Handler();
    Runnable runnableNativeDialogAds;
    GoToSettingDialog permissionDialog;
    Runnable runnableNativeAds;

    @Override
    public ActivityFunctionCustomMenuBinding getBinding() {
        return ActivityFunctionCustomMenuBinding.inflate(getLayoutInflater());
    }
    private boolean checkAdviceAdmin() {
        ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(componentName);
    }
    @Override
    public void initView() {
        loadNativeCustomAds();
        EventTracking.logEvent(this, "custom_menu_select_function_view");
        menuFunction = getIntent().getIntExtra(SPUtils.MENU_FUNCTION, 1);
        menuPosition = getIntent().getIntExtra(SPUtils.MENU_POSITION, 0);
        if (menuFunction == 1) {
            if (Menu1Fragment.instance.listCurrent != null)
                functionSelectedList.addAll(Menu1Fragment.instance.listCurrent);
        } else {
            if (Menu2Fragment.instance.listCurrent != null)
                functionSelectedList.addAll(Menu2Fragment.instance.listCurrent);
        }
        initData();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, true, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {
                if ((icon.getActionNumber() == ItemFunctionIcon.ACTION_TIME_OUT || icon.getActionNumber() == ItemFunctionIcon.ACTION_BRIGHTNESS || icon.getActionNumber() == ItemFunctionIcon.ACTION_LOCK_ROTATION)
                        && !CheckUtils.checkSystemWriteSetting(getBaseContext())) {
                    isAvailable = false;
                    showDialogGotoSetting(3);
                } else if ((icon.getActionNumber() == ItemFunctionIcon.ACTION_HOME ||icon.getActionNumber() == ItemFunctionIcon.ACTION_POWER || icon.getActionNumber() == ItemFunctionIcon.ACTION_BACK || icon.getActionNumber() == ItemFunctionIcon.ACTION_NOTIFICATION || icon.getActionNumber() == ItemFunctionIcon.ACTION_RECENT) && !CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
                    isAvailable = false;
                    showDialogGotoSetting(4);
                } else if (icon.getActionNumber() == ItemFunctionIcon.ACTION_LOCK_SCREEN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (!CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
                            isAvailable = false;
                            showDialogGotoSetting(4);
                        }
                    } else {
                        if (!checkAdviceAdmin()){
                            isAvailable = false;
                            showDialogGotoSetting(7);
                        }
                    }
                } else if (icon.getActionNumber() == ItemFunctionIcon.ACTION_SCREEN_SHOT) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (!CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
                            isAvailable = false;
                            showDialogGotoSetting(4);
                        }
                    } else {
                        if (!PermissionManager.checkReadPermission(getBaseContext())) {
                            isAvailable = false;
                            showDialogGotoSetting(6);
                        }
                    }
                } else isAvailable = true;
                if (isAvailable) {
                    EventTracking.logEvent(getBaseContext(), "custom_menu_select_function_item_click");
                    adapter.setCheckIcon(icon);
                    currentSelectItem = icon;
                }
            }
        });
        adapterSelect = new ItemFunctionIconAdapter(this, functionSelectedList, false, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {

            }
        });
        binding.rcvFunctionSelect.setAdapter(adapterSelect);
        binding.rcvFunction.setAdapter(adapter);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {

            onBack();
        });
        binding.ivGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTracking.logEvent(getBaseContext(), "custom_menu_select_function_done_click");
                if (currentSelectItem != null) {
                    currentSelectItem.setId(menuPosition);
                    if (menuFunction == 1) {
                        Menu1Fragment.instance.listCurrent.set(menuPosition, currentSelectItem);
                    } else {
                        Menu2Fragment.instance.listCurrent.set(menuPosition, currentSelectItem);
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(FunctionCustomMenuActivity.this, R.string.please_select_a_function_first, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "custom_menu_select_function_back_click");
        setResult(RESULT_OK);
        finish();
    }

    private void selectedList(List<ItemFunctionIcon> list) {
        List<ItemFunctionIcon> listDelete = new ArrayList<>();
        for (ItemFunctionIcon icon : list) {
            for (ItemFunctionIcon icon1 : functionIconList)
                if (icon.getText() == icon1.getText())
                    listDelete.add(icon1);
        }
        for (ItemFunctionIcon icon : listDelete) {
            if (icon.getText() != R.string.none)
                functionIconList.remove(icon);
        }
    }

    private void initData() {
        functionIconList = SPUtils.getListCustomMenu();
        selectedList(functionSelectedList);
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
        }else if (type == 7) {
            permissionDialog.binding.tvContent.setText(R.string.you_need_to_enable_device_admin_feature);
        }

        permissionDialog.binding.tvStay.setOnClickListener(view -> {
            permissionDialog.dismiss();
        });
        permissionDialog.binding.tvContent.setOnClickListener(view -> {
            permissionDialog.dismiss();
        });
        permissionDialog.binding.tvAgree.setOnClickListener(view -> {
            AppOpenManager.getInstance().disableAppResumeWithActivity(FunctionCustomMenuActivity.class);
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
            }else if (type == 7) {
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
            loadNativeCustomAds();
            Log.d("activity_check", "home");
        }
    });
    public void loadNativeCustomAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeMenu.isEmpty() && ConstantRemote.native_menu  && ConstantRemote.show_ads) {
                handler.removeCallbacks(runnableNativeAds);
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativeCustomAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativeCustom.removeAllViews();
                binding.nativeCustom.addView(adViewLoad);
                binding.nativeCustom.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeMenu, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativeCustom.removeAllViews();
                                binding.nativeCustom.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativeCustom.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativeCustom.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeCustom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
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
}