package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFunctionCustomMenuBinding;

public class FunctionCustomMenuActivity extends BaseActivity<ActivityFunctionCustomMenuBinding> {

    @Override
    public ActivityFunctionCustomMenuBinding getBinding() {
        return ActivityFunctionCustomMenuBinding.inflate(getLayoutInflater());
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