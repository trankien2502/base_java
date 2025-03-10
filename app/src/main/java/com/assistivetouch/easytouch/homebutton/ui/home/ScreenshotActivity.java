package com.assistivetouch.easytouch.homebutton.ui.home;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityScreenshotBinding;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;

public class ScreenshotActivity extends BaseActivity<ActivityScreenshotBinding> {

    private static final int REQUEST_CODE = 1001;
    private MediaProjectionManager mediaProjectionManager;
    @Override
    public ActivityScreenshotBinding getBinding() {
        return ActivityScreenshotBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        requestScreenCapturePermission();
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    public void requestScreenCapturePermission() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ServiceScreen.instance.takeScreenshot(mediaProjectionManager.getMediaProjection(resultCode, data));
        }
        onBack();
    }
}