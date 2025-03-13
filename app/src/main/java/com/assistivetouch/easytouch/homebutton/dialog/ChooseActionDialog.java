package com.assistivetouch.easytouch.homebutton.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.assistivetouch.easytouch.homebutton.base.BaseBottomSheetDialog;
import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogChooseActionBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig3Binding;


public class ChooseActionDialog extends BaseBottomSheetDialog<DialogChooseActionBinding> {
    public ChooseActionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogChooseActionBinding setBinding() {
        return DialogChooseActionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void bindView() {
    }
}
