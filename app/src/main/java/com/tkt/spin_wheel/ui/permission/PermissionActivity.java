package com.tkt.spin_wheel.ui.permission;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {

    private ActivityResultLauncher<Intent> manageExternalStorageLauncher;
    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        manageExternalStorageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(this)) {
                            checkSwStorage();
                        } else {
                            checkSwStorage();
                        }
                    }
                });
    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
        binding.swPer.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        manageExternalStorageLauncher.launch(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("PermissionError", "Error opening settings: " + e.getMessage());
                    }

                }
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void checkSwStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                binding.swPer.setChecked(true);
                binding.swPer.setOnTouchListener((view, motionEvent) -> true);
            } else {
                binding.swPer.setChecked(false);
                binding.swPer.setOnTouchListener((view, motionEvent) -> false);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
