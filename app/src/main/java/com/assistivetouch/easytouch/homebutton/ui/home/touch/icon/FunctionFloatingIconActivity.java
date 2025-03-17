package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFunctionFloatingIconBinding;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionCallBack;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIconAdapter;
import com.assistivetouch.easytouch.homebutton.service.MyDeviceAdminReceiver;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class FunctionFloatingIconActivity extends BaseActivity<ActivityFunctionFloatingIconBinding> {

    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    ItemFunctionIconAdapter adapter;
    ItemFunctionIcon functionIcon;
    String type = "";
    ArrayList<ItemFunctionIcon> list;
    boolean isAvailable = true;

    @Override
    public ActivityFunctionFloatingIconBinding getBinding() {
        return ActivityFunctionFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        list = SPUtils.getListFloatingIcon();
        type = getIntent().getStringExtra(SPUtils.INTENT_SELECT_FUNCTION);
        initData();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, true, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {
                if ((icon.getActionNumber() == ItemFunctionIcon.ACTION_TIME_OUT || icon.getActionNumber() == ItemFunctionIcon.ACTION_BRIGHTNESS || icon.getActionNumber() == ItemFunctionIcon.ACTION_LOCK_ROTATION)
                        && !CheckUtils.checkSystemWriteSetting(getBaseContext())) {
                    isAvailable = false;
                    showDialogGotoSetting(3);
                } else if ((icon.getActionNumber() == ItemFunctionIcon.ACTION_HOME || icon.getActionNumber() == ItemFunctionIcon.ACTION_BACK || icon.getActionNumber() == ItemFunctionIcon.ACTION_POWER || icon.getActionNumber() == ItemFunctionIcon.ACTION_NOTIFICATION || icon.getActionNumber() == ItemFunctionIcon.ACTION_RECENT) && !CheckUtils.isAccessibilitySettingsOn(getBaseContext(), ServiceControl.class)) {
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
                } else if (icon.getActionNumber() == ItemFunctionIcon.ACTION_CAMERA) {
                    if (!PermissionManager.checkCameraPermission(getBaseContext())) {
                        isAvailable = false;
                        showDialogGotoSetting(5);
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
                    adapter.setCheckIcon(icon);
                    functionIcon = icon;
                }
            }
        });
        binding.rcvFunction.setAdapter(adapter);
        if (type.equals(SPUtils.FLOATING_ICON_SINGLE_TAP))
            adapter.setCheckIcon(SPUtils.getObject(this, type, list.get(3)));
        else
            adapter.setCheckIcon(SPUtils.getObject(this, type, list.get(0)));

    }
    private boolean checkAdviceAdmin() {
        ComponentName componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(componentName);
    }
    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            if (functionIcon != null) SPUtils.setObject(this, type, functionIcon);
            setResult(2502);
            finish();
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void initData() {
        functionIconList = SPUtils.getListFloatingIcon();
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
            }
        });
        dialog.show();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //ads
            Log.d("activity_check", "home");
        }
    });
}