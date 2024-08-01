package com.tkt.spin_wheel.ui.setting;

import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.policy.PolicyActivity;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(AboutActivity.this,"view_about_app");
    }

    @Override
    public void bindView() {
        binding.privacy.setOnClickListener(view -> {
            startNextActivity(PolicyActivity.class,null);
        });
        binding.ivGone.setOnClickListener(view -> onBackPressed());
    }
}