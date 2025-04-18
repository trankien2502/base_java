package com.tkt.basejava.basejava1.basejava2.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tkt.basejava.basejava1.basejava2.base.BaseDialog;
import com.tkt.basejava.basejava1.basejava2.databinding.DialogPermissionBinding;
import com.tkt.basejava.basejava1.basejava2.util.GoToSettingCallBack;


public class GoToSettingDialog extends BaseDialog<DialogPermissionBinding> {
    int type = 2;
    //    Context context;
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

    }

    public void initDialog(int type, GoToSettingCallBack callBack) {
        this.type = type;
        this.callBack = callBack;
    }
}
