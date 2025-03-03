package com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon;

import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFloatingIconBinding;

public class FloatingIconActivity extends BaseActivity<ActivityFloatingIconBinding> {

    @Override
    public ActivityFloatingIconBinding getBinding() {
        return ActivityFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        startNextActivity(IconStyleActivity.class,null);
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}