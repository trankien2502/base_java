package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityCustomMenuBinding;

public class CustomMenuActivity extends BaseActivity<ActivityCustomMenuBinding> {

    @Override
    public ActivityCustomMenuBinding getBinding() {
        return ActivityCustomMenuBinding.inflate(getLayoutInflater());
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