package com.tkt.storage_manager.dialog.micro_access;

import android.content.Context;

import com.tkt.storage_manager.base.BaseDialog;
import com.tkt.storage_manager.databinding.DialogMicroAccessBinding;

public class MicroAccessDialog extends BaseDialog<DialogMicroAccessBinding> {
    IClickDialogMicroAccess iBaseListener;
    Context context;

    public MicroAccessDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogMicroAccessBinding setBinding() {
        return DialogMicroAccessBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnDeny.setOnClickListener(view -> iBaseListener.deny());

        binding.btnAllow.setOnClickListener(view -> iBaseListener.allow());

    }

    public void init(IClickDialogMicroAccess iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
