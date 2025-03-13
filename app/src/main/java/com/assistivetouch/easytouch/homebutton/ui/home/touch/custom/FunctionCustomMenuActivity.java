package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFunctionCustomMenuBinding;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionCallBack;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIconAdapter;
import com.assistivetouch.easytouch.homebutton.service.ServiceControl;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

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

    @Override
    public ActivityFunctionCustomMenuBinding getBinding() {
        return ActivityFunctionCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
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
        Toast.makeText(this, "menu & position: " + menuFunction + menuPosition, Toast.LENGTH_SHORT).show();
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