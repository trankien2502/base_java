package com.assistivetouch.easytouch.homebutton.ui.home;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityScreenRecorderBinding;
import com.assistivetouch.easytouch.homebutton.service.ScreenRecordService;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;

public class ScreenRecorderActivity extends BaseActivity<ActivityScreenRecorderBinding> {

    private static final int REQUEST_CODE = 1000;
    private MediaProjectionManager mediaProjectionManager;

    @Override
    public ActivityScreenRecorderBinding getBinding() {
        return ActivityScreenRecorderBinding.inflate(getLayoutInflater());
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
        Log.e("check_record", "????");
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            ServiceScreen.instance.mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
//            ServiceScreen.instance.setupMediaRecorder();
//            ServiceScreen.instance.startRecording();
            Log.e("check_record", "??");
            Intent serviceIntent = new Intent(this, ScreenRecordService.class);
            serviceIntent.putExtra("RESULT_CODE", resultCode);
            serviceIntent.putExtra("DATA_INTENT", data);
            startService(serviceIntent); // Bắt đầu Service
//            if (Build.VERSION.SDK_INT < 34) {
//
//            } else {
//                Toast.makeText(this, R.string.this_function_is_only_use_by_system_app, Toast.LENGTH_SHORT).show();
//            }

        }
        onBack();
    }
}