package com.tkt.storage_manager.ui.setting;

import com.tkt.storage_manager.base.BaseActivity;
import com.tkt.storage_manager.ui.language.LanguageActivity;
import com.tkt.storage_manager.R;
import com.tkt.storage_manager.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {


    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.clLanguage.setOnClickListener(view -> {
            startNextActivity(LanguageActivity.class,null);
        });

    }
}