package com.livescore.soccerscore.matchlive.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogPermissionBinding;


public class GoToSettingDialog extends BaseDialog<DialogPermissionBinding> {
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

    }
}
