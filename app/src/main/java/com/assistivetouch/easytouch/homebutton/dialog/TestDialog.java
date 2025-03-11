package com.assistivetouch.easytouch.homebutton.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogLoadingBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig1Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig3Binding;


public class TestDialog extends BaseDialog<PopupVoulumeConfig3Binding> {
    public TestDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected PopupVoulumeConfig3Binding setBinding() {
        return PopupVoulumeConfig3Binding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void bindView() {
    }
}
