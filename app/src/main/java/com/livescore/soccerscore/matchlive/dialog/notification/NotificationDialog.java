package com.livescore.soccerscore.matchlive.dialog.notification;

import android.content.Context;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogNotificationBinding;
import com.livescore.soccerscore.matchlive.databinding.DialogPermissionBinding;


public class NotificationDialog extends BaseDialog<DialogNotificationBinding> {


    public NotificationDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogNotificationBinding setBinding() {
        return DialogNotificationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
    public void initCallBack(){

    }
}
