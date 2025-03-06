package com.assistivetouch.easytouch.homebutton.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogLoadingBinding;


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
