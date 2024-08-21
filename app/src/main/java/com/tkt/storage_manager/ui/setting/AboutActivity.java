package com.tkt.storage_manager.ui.setting;

import com.tkt.storage_manager.base.BaseActivity;
import com.tkt.storage_manager.ui.policy.PolicyActivity;
import com.tkt.storage_manager.util.EventTracking;
import com.tkt.storage_manager.R;
import com.tkt.storage_manager.databinding.ActivityAboutBinding;

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