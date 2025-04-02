package com.livescore.soccerscore.matchlive.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogPermissionBinding;
import com.livescore.soccerscore.matchlive.databinding.DialogPinnedBinding;


public class MatchPinnedDialog extends BaseDialog<DialogPinnedBinding> {
    public MatchPinnedDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogPinnedBinding setBinding() {
        return DialogPinnedBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
}
