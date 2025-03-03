package com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon;

import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFunctionFloatingIconBinding;

public class FunctionFloatingIconActivity extends BaseActivity<ActivityFunctionFloatingIconBinding> {

    @Override
    public ActivityFunctionFloatingIconBinding getBinding() {
        return ActivityFunctionFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

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