package com.assistivetouch.easytouch.homebutton.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionConfig;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityScreenRecorderBinding;
import com.assistivetouch.easytouch.homebutton.service.ScreenRecordService;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.permission.PermissionActivity;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

public class ScreenRecorderActivity extends BaseActivity<ActivityScreenRecorderBinding> {

    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1000;
    private static final int REQUEST_CODE_MEDIA_PROJECTION = 1010;
    private static final int REQUEST_CODE_AUDIO_PERMISSION = 100;
    private MediaProjectionManager mediaProjectionManager;

    @Override
    public ActivityScreenRecorderBinding getBinding() {
        return ActivityScreenRecorderBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION},
//                        REQUEST_CODE_MEDIA_PROJECTION);
//            }
//        }
        requestAudioPermission();
//        requestScreenCapturePermission();
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
            requestScreenCapturePermission();
        }
        if (requestCode == REQUEST_CODE_MEDIA_PROJECTION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
            requestScreenCapturePermission();
        }
    }

    public void requestScreenCapturePermission() {
        Log.e("check_record", "????");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
            MediaProjectionManager mediaProjectionManager =
                    (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            MediaProjectionConfig config = MediaProjectionConfig.createConfigForDefaultDisplay();
            Intent intent = mediaProjectionManager.createScreenCaptureIntent(config);
            startActivityForResult(intent, REQUEST_CODE_SCREEN_CAPTURE);
        } else {
            // Android 13 trở xuống dùng cách cũ
            mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent intent = mediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(intent, REQUEST_CODE_SCREEN_CAPTURE);
        }

    }

    public void requestAudioPermission() {
        Log.e("check_record", "????");
        if (!PermissionManager.checkMicrophonePermission(this))
            ActivityCompat.requestPermissions(ScreenRecorderActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_PERMISSION);
        else requestScreenCapturePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE && resultCode == RESULT_OK) {
//            ServiceScreen.instance.mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
//            ServiceScreen.instance.setupMediaRecorder();
//            ServiceScreen.instance.startRecording();
            Log.e("check_record", "??");
            Intent serviceIntent = new Intent(this, ScreenRecordService.class);
            serviceIntent.putExtra("RESULT_CODE", resultCode);
            serviceIntent.putExtra("DATA_INTENT", data);

            if (Build.VERSION.SDK_INT < 34) {
                startService(serviceIntent); // Bắt đầu Service
            } else {
                startForegroundService(serviceIntent);
            }

        }
        onBack();
    }
}