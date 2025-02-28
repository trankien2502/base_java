package com.tkt.basejava.basejava1.basejava2.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tkt.basejava.basejava1.basejava2.base.BaseDialog;
import com.tkt.basejava.basejava1.basejava2.databinding.DialogPermissionBinding;


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
