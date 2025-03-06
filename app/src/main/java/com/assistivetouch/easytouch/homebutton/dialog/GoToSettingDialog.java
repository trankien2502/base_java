package com.assistivetouch.easytouch.homebutton.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogPermissionBinding;


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
