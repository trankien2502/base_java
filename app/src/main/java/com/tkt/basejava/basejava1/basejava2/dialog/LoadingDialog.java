package com.tkt.basejava.basejava1.basejava2.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tkt.basejava.basejava1.basejava2.base.BaseDialog;
import com.tkt.basejava.basejava1.basejava2.databinding.DialogLoadingBinding;


public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
    public LoadingDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogLoadingBinding setBinding() {
        return DialogLoadingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void bindView() {

    }
}
