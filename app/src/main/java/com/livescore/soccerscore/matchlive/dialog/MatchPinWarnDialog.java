package com.livescore.soccerscore.matchlive.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogPinnedBinding;
import com.livescore.soccerscore.matchlive.databinding.DialogWarnPinBinding;


public class MatchPinWarnDialog extends BaseDialog<DialogWarnPinBinding> {
    public MatchPinWarnDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogWarnPinBinding setBinding() {
        return DialogWarnPinBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
}
