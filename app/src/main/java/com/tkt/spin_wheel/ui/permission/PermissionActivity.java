package com.tkt.spin_wheel.ui.permission;


import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.databinding.ActivityPermissionBinding;

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
