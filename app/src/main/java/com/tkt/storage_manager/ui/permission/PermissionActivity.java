package com.tkt.storage_manager.ui.permission;


import com.tkt.storage_manager.base.BaseActivity;
import com.tkt.storage_manager.ui.home.HomeActivity;
import com.tkt.storage_manager.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
