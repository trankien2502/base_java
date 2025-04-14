package com.livescore.soccerscore.matchlive.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogPermissionBinding;
import com.livescore.soccerscore.matchlive.util.GoToSettingCallBack;


public class GoToSettingDialog extends BaseDialog<DialogPermissionBinding> {
    int type = 2;
    GoToSettingCallBack callBack;

    public GoToSettingDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogPermissionBinding setBinding() {
        return DialogPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.tvStay.setOnClickListener(view -> {
            dismiss();
        });
        binding.tvAgree.setOnClickListener(view -> {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
            dismiss();
            if (type == 1) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                if (callBack != null) callBack.goToSetting(intent);
            } else if (type == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        if (callBack != null) callBack.goToSetting(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PermissionError", "Error opening settings: " + e.getMessage());
                    }

                }
            }
        });
    }

    public void initDialog(int type, GoToSettingCallBack callBack) {
        this.type = type;
        this.callBack = callBack;
    }
}
