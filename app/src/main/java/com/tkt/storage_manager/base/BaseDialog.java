package com.tkt.storage_manager.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.tkt.storage_manager.util.SystemUtil;
import com.tkt.storage_manager.R;


public abstract class BaseDialog<VB extends ViewBinding> extends Dialog {

    public VB binding;

    protected abstract VB setBinding();

    protected abstract void initView();

    protected abstract void bindView();

    public BaseDialog(@NonNull Context context, boolean canAble) {
        super(context, R.style.BaseDialog);
        SystemUtil.setLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = setBinding();
        setContentView(binding.getRoot());

        setCancelable(canAble);

        initView();
        bindView();
    }

}
